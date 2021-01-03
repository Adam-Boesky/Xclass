package com.adamboesky.riemann;
import org.dalton.polyfun.Polynomial;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;
import java.awt.*;

abstract public class AbstractRiemann {


    public abstract double slice(Polynomial poly, double sleft, double sright);


    public abstract void slicePlot​(PlotFrame pframe, Polynomial poly, double sleft, double sright);


    /**
     * Calculates Δx based on the endpoints of the Riemann sum and the number of subintervals; in other words this
     * method finds the width of each rectangle.
     *
     * @param left - the left hand endpoint of the Riemann sum
     * @param right - the right hand endpoint of the Riemann sum
     * @param subintervals - the number of subintervals into which to divide the interval
     * @return Δx
     */
    public static double calculateDeltaX(double left, double right, int subintervals){
        return Math.abs(right-left)/subintervals;
    }


    /**
     * Evaluates polynomials that are in the form of y = r^2 - x^2 but treats it as y = sqrt(r^2 - x^2) in order to make
     * it behave like a circle.
     *
     * @param poly - the polynomial that will be evaluated with
     * @param x - the value that you are evaluating the polynomial with
     * @return  the value of the polynomial at point x
     */
    public static double evaluateWithCircle(Polynomial poly, double x) {
        return Math.sqrt(poly.evaluateWith(x));
    }


    /**
     * Gets the Riemann sum by adding the slices of any given polynomial
     *
     * @param poly - the polynomial whose Riemann sum is to be calculated
     * @param left - the left hand endpoint of the Riemann sum
     * @param right - the right hand endpoint of the Riemann sum
     * @param subintervals - the number of subintervals into which to divide the interval
     * @return An approximation of the Riemann sum over the interval [left, right].
     */
    public double rs(Polynomial poly, double left, double right, int subintervals){
        double area = 0;
        double widthRect = calculateDeltaX(left, right, subintervals);


        for (int i = 0; i < subintervals; i++) {
            area +=  (slice(poly, left + (i * widthRect), left + ((i + 1) * widthRect)));
        }
        return area;
    }


    /**
     * Plots a Riemann sum in a given PlotFrame.
     *
     * @param pframe - the PlotFrame on which the polynomial and the Riemann sum are drawn
     * @param poly - the polynomial whose Riemann sum is to be calculated
     * @param precision - the difference between the x-coordinates of two adjacent points on the graph of the function
     * @param left - the left hand endpoint of the Riemann sum
     * @param right - the right hand endpoint of the Riemann sum
     * @param subintervals - the number of subintervals into which to divide the interval
     */
    public void rsPlot​(PlotFrame pframe, org.dalton.polyfun.Polynomial poly, double precision, double left, double
            right, int subintervals) {
        double widthRect = calculateDeltaX(left, right, subintervals);


        for (int i = 0; i < subintervals; i++) {
            slicePlot​(pframe, poly, left + ((widthRect) * i), left + ((widthRect) * (i + 1)));
        }
    }


    /**
     * Graphs the accumulation function corresponding to the input polynomial and the endpoints. This method uses the
     * specific implementation of slicePlot(PlotFrame, Polynomial, double, double) defined in the subclass.
     *
     * @param pframe - the PlotFrame on which the polynomial and the Riemann sum are drawn
     * @param poly - the polynomial whose accumulation function is to be calculated
     * @param xLeft - the left hand endpoint of the accumulation function
     * @param xRight - the right hand endpoint of the accumulation function
     */
    public void rsAcc​(PlotFrame pframe, Polynomial poly, double xLeft, double xRight){


        // Find important values:
        double area = 0;


        // Make and begin trail:
        Trail trail = new Trail();
        trail.color = Color.BLUE;
        trail.addPoint(xLeft, 0);
        // Add area incrementally:
        for (double i = xLeft + .1; i <= xRight + .001; i = i + .1) {
                area = area + slice(poly, i - .1, i);
                trail.addPoint(i, area);

        }
        pframe.addDrawable(trail);
    }


    /**
     * This method finds what the integral converges on for any given polynomial and interval. It does so by quadrupling
     * the number of subintervals until the difference between the sum and the sum with the next quadruple of
     * subintervals is less than 1% of the length of the interval. It double checks by checking the quadrupled sum with
     * the quadrupled sum quadrupled again to ensure that there will be no cases that don't work.
     *
     *
     * @param poly - the polynomial that the approximate integral is calculated for
     * @param sLeft - the left hand endpoint of the interval
     * @param sRight - the right hand endpoint of the interval
     * @return The approximate integral from the interval [sLeft, sRight].
     */
    public double approxIntegralIntervalSize(Polynomial poly, double sLeft, double sRight) {
        int subints = 100;
        double approxIntegral = 0;


        // Multiply the subintervals by 4 until the riemann sum is within one percent of the next increase of 100
        // subintervals
        for (int i = 0; i < 2; i++) {
            double sum = rs(poly, sLeft, sRight, subints);
            double increasedSum = rs(poly, sLeft, sRight, subints * 4);
            // Check if the percent difference between the rs and the rs with quadruple subintervals differ by more than
            // 1% of the interval length. If not, use that value as the integral that the rs converges on.
            if(Math.abs(increasedSum - sum) > (.01 * (sRight - sLeft))) {
                i = 0;
                subints = subints * 4;
            }
            else{
                approxIntegral = sum;
                break;
            }
        }
        return (approxIntegral);
    }
}
