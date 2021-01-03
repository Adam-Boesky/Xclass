package com.adamboesky.riemann.circleMethods;

import com.adamboesky.riemann.AbstractRiemann;
import org.dalton.polyfun.Polynomial;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;

public class CircleRightHandRule extends AbstractRiemann {

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
        double rectHeight = evaluateWithCircle(poly, sright);
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


        // Calculates the dimensions and coordinates of the rectangle to be plotted:
        double rectCenterX = sright - ((sright - sleft)/2);
        double rectCenterY = evaluateWithCircle(poly, sright)/2;
        double rectWidth = Math.abs(calculateDeltaX(sleft, sright, 1));
        double rectHeight = Math.abs(evaluateWithCircle(poly, sright));


        // Plots the rectangle:
        DrawableShape rect = DrawableShape.createRectangle(rectCenterX,rectCenterY, rectWidth, rectHeight);
        rect.color = Color.BLUE;
        pframe.addDrawable(rect);
    }

}
