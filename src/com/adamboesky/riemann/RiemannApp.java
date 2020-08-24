package com.adamboesky.reimann;

import com.adamboesky.reimann.circleMethods.CalculatePi;
import com.adamboesky.reimann.circleMethods.CircleRightHandRule;
import org.dalton.polyfun.Polynomial;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

// The trapezoid rule will be the most accurate rule of the three rules because it will reflect the shape of the top of
// the slice more effectively than the right or left hand rule. This means that its area will be the closest to the
// actual area of the slice.


public class ReimannApp {
    public static void main(String[] args) {

        // Declare the polynomial of the circle:
        Polynomial polyCircle = new Polynomial(new double[]{4, 0, -1});


        // Declare values that will be changed and used later for the plotting and obtaining of the Riemann sum as 0:
        int i = 1;
        int minX = 0;
        int maxX = 0;
        int subintervals = 0;


        // Ask for and set the user polynomial:
        System.out.println("You can at any point end the polynomial by typing \"done\".\nPlease enter the coefficients" +
                " of the x^0 term");
        Scanner scan = new Scanner(System.in);
        ArrayList<Double> polyCoefs = new ArrayList<>(0);
        while(scan.hasNext()) {
            String polyStr = scan.nextLine();
            if(polyStr.equals("done")){
                break;
            }
            System.out.println("Please enter the coefficients of the x^" + i + " term");
            i++;
            polyCoefs.add(Double.parseDouble(polyStr));
        }
        double[] polyCoefDbls = new double[polyCoefs.size()];
        for (int j = 0; j < polyCoefDbls.length; j++) {
            polyCoefDbls[j] = polyCoefs.get(j);
        }
        Polynomial poly = new Polynomial(polyCoefDbls);


        // Ask for and set the minimum and maximum X and Y and the number of subintervals:
        // Minimum X:
        System.out.println("What minimum X value would you like?");
        Scanner scanMinX = new Scanner(System.in);
        minX = Integer.parseInt(scanMinX.nextLine());
        // Maximum X:
        System.out.println("What maximum X value would you like?");
        Scanner scanMaxX = new Scanner(System.in);
        maxX = Integer.parseInt(scanMaxX.nextLine());
        // Subintervals:
        System.out.println("How many subintervals would you like to use to calculate the Riemann sum?");
        Scanner subInts = new Scanner(System.in);
        subintervals = Integer.parseInt(subInts.nextLine());


        // Use the minimum and maximum x values to iterate over the function and find the approximate max and min y:
        int minY = 0;
        int maxY = 0;
        for (int j = minX * 10; j <= maxX * 10; j++) {
            if (minY > poly.evaluateWith(j / 10)) {
                minY = (int) poly.evaluateWith(j / 10);
            }
        }
        for (int j = minX * 10; j <= maxX * 10; j++) {
            if (maxY < poly.evaluateWith(j / 10)) {
                maxY = (int) poly.evaluateWith(j / 10);
            }
        }


        // Plot all of the polynomials and their rules:
        // Right Hand:
        PlotPolynomial.plotPoly("X", "Y", "Right Hand Rule Plot", 800, 800, minX,
                maxX, minY, maxY, poly, subintervals, 0, false);
        // Left Hand:
        PlotPolynomial.plotPoly("X", "Y", "Left Hand Rule Plot", 800, 800, minX,
                maxX, minY, maxY, poly, subintervals, 1, false);
        // Trapezoid:
        PlotPolynomial.plotPoly("X", "Y", "Trapezoid Rule Plot", 800, 800, minX,
                maxX, minY, maxY, poly,subintervals,  2, false);
        // Simpson:
        PlotPolynomial.plotPoly("X", "Y", "Simpson Rule Plot", 800, 800, minX,
                maxX, minY, maxY, poly, subintervals, 3, false);
        // Circle:
        PlotPolynomial.plotPoly("X", "Y", "Circle Plot", 800, 800, -3,
                3, -3, 3, polyCircle, 100,  4, true);


        // Create objects of all of the rules:
        RightHandRule rightHandRule = new RightHandRule();
        LeftHandRule leftHandRule = new LeftHandRule();
        TrapezoidRule trapezoidRule = new TrapezoidRule();
        SimpsonRule simpsonRule = new SimpsonRule();
        CircleRightHandRule circleRightHandRule = new CircleRightHandRule();


        // Calculate the Reimann sum for all of the rules:
        double rightHandSum = rightHandRule.rs(poly, minX, maxX, subintervals);
        double leftHandSum = leftHandRule.rs(poly, minX, maxX, subintervals);
        double trapezoidSum = trapezoidRule.rs(poly, minX, maxX, subintervals);
        double simpsonSum = simpsonRule.rs(poly, minX, maxX, subintervals);
        double circleSum = circleRightHandRule.rs(polyCircle, -2, 2, 100000);


        // Plot the accumulation function:
        // A spacer to make the plot look pretty
        int xSpacer = Math.abs((maxX - minX)/16);
        if (xSpacer == 0) {
            xSpacer = 1;
        }
        PlotFrame accPlot = new PlotFrame("X", "Y", "Accumulation Function");
        accPlot.setSize(800, 800);
        int[] minMaxY = simpsonRule.rsAccMinMaxY(poly, minX, maxX);
        // Checking if the minimum or the maximum y value of the accumulation function is greater or less than that of
        // the polynomial in order for the plot to fit everything:
        if (minY > minMaxY[0]) {
            minY = minMaxY[0];
        }
        if (maxY < minMaxY[1]) {
            maxY = minMaxY[1];
        }
        int ySpacer = Math.abs((maxY - minY)/10);
        if (ySpacer == 0) {
            ySpacer = 1;
        }
        accPlot.setPreferredMinMax(minX - xSpacer, maxX + xSpacer, minY - ySpacer, maxY +
                ySpacer);
        accPlot.setConnected(true);
        accPlot.setDefaultCloseOperation(3);
        simpsonRule.rsAcc​(accPlot, poly, minX, maxX);
        // Plot the polynomial on the acc plot:
        Trail function = new Trail();
        function.color = Color.black;
        for (int j = ((minX - xSpacer) * 10); j <= ((maxX + xSpacer) * 10); j++) {
            function.addPoint(j / 10.0, poly.evaluateWith(j / 10.0));
        }
        accPlot.addDrawable(function);
        accPlot.setVisible(true);


        // Print the Reimann sum for all of the rules:
        System.out.println("\n\n\n\nPolynomial:\n" + poly);
        System.out.println("------------------");
        System.out.println("Right hand rule:" +
                "\n       Area using your subintervals: " + rightHandSum +
                "\n       Approximate integral based off of percent difference: " +
                rightHandRule.approxIntegralPD(poly, minX, maxX) +
                "\n       Approximate integral based off of the interval size: " +
                rightHandRule.approxIntegralIntervalSize(poly, minX, maxX));
        System.out.println("Left hand rule:" +
                "\n       Area using your subintervals: " + leftHandSum +
                "\n       Approximate integral based off of percent difference: " +
                leftHandRule.approxIntegralPD(poly, minX, maxX) +
                "\n       Approximate integral based off of the interval size: " +
                leftHandRule.approxIntegralIntervalSize(poly, minX, maxX));
        System.out.println("Trapezoid rule:" +
                "\n       Area using your subintervals: " + trapezoidSum +
                "\n       Approximate integral based off of percent difference: " +
                trapezoidRule.approxIntegralPD(poly, minX, maxX) +
                "\n       Approximate integral based off of the interval size: " +
                trapezoidRule.approxIntegralIntervalSize(poly, minX, maxX));
        System.out.println("Simpson rule:" +
                "\n       Area using your subintervals: " + simpsonSum +
                "\n       Approximate integral based off of percent difference: " +
                simpsonRule.approxIntegralPD(poly, minX, maxX) +
                "\n       Approximate integral based off of the interval size: " +
                simpsonRule.approxIntegralIntervalSize(poly, minX, maxX));
        System.out.println("------------------");
        System.out.println("Semi-circle with right hand est. area: " + circleSum);
        System.out.println("Value of Pi calculated with the area of the semi-circle: " + CalculatePi.calcPi(circleSum,
                2));
    }
}
