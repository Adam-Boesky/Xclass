package com.adamboesky.reimann;

import com.adamboesky.reimann.circleMethods.CircleRightHandRule;
import org.dalton.polyfun.Polynomial;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;

public class PlotPolynomial {

    /**
     * Plots a given polynomial and plots the Riemann sum of a given rule and number of subintervals. This method
     * creates a plot frame that will have dimensions based off of given parameters.
     *
     * @param xlabel - label of the X-axis
     * @param ylabel - label of the Y-axis
     * @param frameLabel - label of the frame
     * @param width - width of the frame
     * @param height - height of the frame
     * @param xmin - minimum X value
     * @param xmax - maximum X value
     * @param ymin - minimum Y value
     * @param ymax - maximum Y value
     * @param poly - the polynomial used in the plot
     * @param subintervals - the number of subintervals
     * @param ruleType - the rule to be used in the plot
     * @param iscircle - if the polynomial is meant to be a semi circle or not
     */
    public static void plotPoly(String xlabel, String ylabel, String frameLabel, int width, int height, int xmin,
                                int xmax, int ymin, int ymax, Polynomial poly, int subintervals, int ruleType,
                                boolean iscircle){


        // Assigns the rule that will be used:
        AbstractRiemann rule;
        if(ruleType == 0){
            rule = new RightHandRule();
        }
        else if(ruleType == 1){
            rule = new LeftHandRule();
        }
        else if(ruleType == 2){
            rule = new TrapezoidRule();
        }
        else if(ruleType == 3){
            rule = new SimpsonRule();
        }
        else {
            rule = new CircleRightHandRule();
        }


        // Plots the polynomial:
        // Spacers to make the plots look pretty
        int ySpacer = Math.abs((ymax - ymin)/10);
        if (ySpacer == 0) {
            ySpacer = 1;
        }
        int xSpacer = Math.abs((xmax - xmin)/10);
        if (xSpacer == 0) {
            xSpacer = 1;
        }
        PlotFrame plot = new PlotFrame(xlabel, ylabel, frameLabel);
        plot.setSize(width, height);
        plot.setPreferredMinMax(xmin - xSpacer, xmax + xSpacer, ymin - ySpacer,
                ymax + ySpacer);
        plot.setConnected(true);
        plot.setDefaultCloseOperation(3);
        Trail function = new Trail();
        function.color = Color.black;
        if (iscircle == true){
            for (int i = -20; i < 21; i++) {
                function.addPoint(i/10.0, AbstractRiemann.evaluateWithCircle(poly,i/10.0));
            }
        }else {
            for (int i = (((xmin - xSpacer) * 10)); i <= ((xmax + xSpacer) * 10); i++) {
                function.addPoint(i / 10.0, poly.evaluateWith(i / 10.0));
            }
        }
        plot.addDrawable(function);


        // Plots the Riemann sum:
        rule.rsPlot​(plot, poly, 3, xmin, xmax, subintervals);


        plot.setVisible(true);
    }
}
