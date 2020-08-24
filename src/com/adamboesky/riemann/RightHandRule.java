package com.adamboesky.reimann;

import org.dalton.polyfun.Polynomial;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;

public class RightHandRule extends AbstractRiemann {

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
        double rectWidth = sright - sleft;
        double rectHeight = poly.evaluateWith(sright);
        return rectHeight * rectWidth;
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


        // Define the dimensions and location of the rectangle used in the slice:
        double rectCenterX = sright - ((sright - sleft)/2);
        double rectCenterY = poly.evaluateWith(sright)/2;
        double rectWidth = Math.abs(calculateDeltaX(sleft, sright, 1));
        double rectHeight = Math.abs(poly.evaluateWith(sright));


        // Plot the rectangle:
        DrawableShape rect = DrawableShape.createRectangle(rectCenterX,rectCenterY, rectWidth, rectHeight);
        rect.color = Color.blue;
        pframe.addDrawable(rect);
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
