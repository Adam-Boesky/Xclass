package com.adamboesky.reimann;

import org.dalton.polyfun.Polynomial;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;

public class SimpsonRule extends AbstractRiemann{

    /**
     * Approximates a slice of the (signed) area between the graph of a polynomial and the x-axis, over a given interval
     * on the x-axis. The specific Riemann sum rule used in the calculation is defined by the subclass. This method
     * should be used in rs(Polynomial, double, double, int) to find the total area.
     *
     * @param poly - the polynomial whose area (over or under the x-axis), over the interval from sleft to sright, is to
     *            be calculated
     * @param sleft - the left hand endpoint of the interval
     * @param sright - the right hand endpoint of the interval
     * @return - A slice of the riemann sum
     */
    @Override
    public double slice(Polynomial poly, double sleft, double sright) {

        // Define the coordinates of three points on the given polynomial:
        double x1 = sleft;
        double y1 = poly.evaluateWith(x1);
        double x2 = (sleft + sright) / 2;
        double y2 = poly.evaluateWith(x2);
        double x3 = sright;
        double y3 = poly.evaluateWith(x3);


        // Define coefficients a b and c in terms of the three general coordinates
        double a = ((y1) / ((x1 - x2) * (x1 - x3))) + ((y2) / ((x2 - x1) * (x2 - x3))) + ((y3) / ((x3 - x1) * (x3 - x2)));
        double b = (y2 - y1 - a * (Math.pow(x2, 2) - Math.pow(x1, 2))) / (x2 - x1);
        double c = y1 - (a * Math.pow(x1, 2)) - (b * x1);


        // The polynomial that reflects the slice:
        Polynomial polyQuad = new Polynomial(new double[]{c, b, a});


        return ((x3 - x1) / 6) * (polyQuad.evaluateWith(x1) + (4 * polyQuad.evaluateWith(x2)) + polyQuad.evaluateWith(x3));
    }

    /**
     * Plots a slice of the (signed) area between the graph of a polynomial and the x-axis, over a given interval on the
     * x-axis. The specific Riemann sum rule used is defined by the subclass. This method should be used in
     * rsPlot(PlotFrame, Polynomial, int, double, double, double, int) to find the total area.
     *
     * @param pframe - the PlotFrame on which the slicePlot is to be drawn
     * @param poly - the polynomial whose area (over or under the x-axis), over the interval from sleft to sright, is to
     *            be drawn
     * @param sleft - the left hand endpoint of the interval
     * @param sright - the right hand endpoint of the interval
     */
    @Override
    public void slicePlot​(PlotFrame pframe, Polynomial poly, double sleft, double sright) {


        // Define the coordinates of three points on the given polynomial:
        double x1 = sleft;
        double y1 = poly.evaluateWith(x1);
        double x2 = (sleft + sright) / 2;
        double y2 = poly.evaluateWith(x2);
        double x3 = sright;
        double y3 = poly.evaluateWith(x3);


        // Calculate the distance between the 10 points that will be used to plot the slice of the simpson rule:
        double simpsonPointIntervals = calculateDeltaX(x3, x1, 10);


        // Define coefficients a b and c in terms of the three general coordinates:
        double a = ((y1) / ((x1 - x2) * (x1 - x3))) + ((y2) / ((x2 - x1) * (x2 - x3))) + ((y3) / ((x3 - x1) * (x3 - x2)));
        double b = (y2 - y1 - a * (Math.pow(x2, 2) - Math.pow(x1, 2))) / (x2 - x1);
        double c = y1 - (a * Math.pow(x1, 2)) - (b * x1);
        Polynomial simpsonParabola = new Polynomial(new double[]{c, b, a});


        // Plot the function that is meant to reflect a slice of the given polynomial:
        Trail trail = new Trail();
        trail.color = Color.BLUE;
        trail.addPoint(x1, 0);
        for (int i = 0; i <= 10; i++) {
            trail.addPoint((x1) + (i * simpsonPointIntervals), simpsonParabola.evaluateWith((x1) + (i * simpsonPointIntervals)));
        }
        trail.addPoint(x3, 0);
        trail.addPoint(x1, 0);
        pframe.addDrawable(trail);
    }


    /**
     * Finds the approximate minimum and maximum y coordinates on the accumulation of a given function and range of x
     * values. This method iterates over the accumulation function in order to find that maximum and minimum y values.
     *
     * @param poly - the polynomial whose accumulation function's maximum and minimum y value is being found.
     * @param xLeft - the left hand endpoint of the accumulation function
     * @param xRight - the right hand endpoint of the accumulation function
     * @return An int array with a length of 2. The first spot in the array is the approximate minimum y value and the
     * second is the approximate maximum.
     */
    public int[] rsAccMinMaxY(Polynomial poly, double xLeft, double xRight){


        double area = 0;
        int yMin = 0;
        int yMax = 0;
        // Find important values:
        int subintervals = (int) ((xRight - xLeft) * 10); //This value is used in order to plot 10 points per one x
        double subIntWidth = calculateDeltaX(xLeft, xRight, subintervals);


        // Iterate over the accumulation function and find the minimum and maximum y value:
        for (double i = xLeft; i < xRight; i = i + subIntWidth) {
            area = area + slice(poly, i, i + subIntWidth);
            if (area > yMax) {
                yMax = (int) area;
            }
            if (area < yMin) {
                yMin = (int) area;
            }
        }
        int[] output = new int[2];
        output[0] = yMin;
        output[1] = yMax;
        return output;
    }
}
