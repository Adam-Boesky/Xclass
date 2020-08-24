import org.dalton.polyfun.Polynomial;

import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.Color;

/**
 * GettingStarted class to show off Polynomials, Open Source Physics and JUnit test.
 */
public class GettingStarted {
    public static void main(String[] args) {
        /*
             Polynomial examples
         */
        Polynomial fx = new Polynomial(2);
        Polynomial vx = new Polynomial(new double[]{1, 2, 3});
        Polynomial gx = new Polynomial(new double[]{5, 0, 0, 0, 4});

        System.out.println("f(x) = " + fx);
        System.out.println("v(x) = " + vx);
        System.out.println("g(x) = " + gx);
        System.out.println("f(x) * v(x) = " + fx.times(vx));

        System.out.println();
        System.out.println("f(2) = " + fx.evaluateWith(2));
        System.out.println("v(2) = " + vx.evaluateWith(2));

        System.out.println();
        System.out.println("v(x) + f(x) = " + vx + fx);

        System.out.println();
        System.out.println(vx.getCoefficientAtTerm(2));
        System.out.println(vx.getCoefficientArray()[2]);
        System.out.println(vx.getCoefficientAtTerm(1));

        //print all of the coefficients
        for (int i = 0; i <= vx.getDegree(); i++) {
            System.out.println("Coefficient at term with x to the " + i + " " + vx.getCoefficientAtTerm(i));
        }

        /*
              Open Source Physics (OSP) Example
         */
        // Setup the graph
        PlotFrame plotFrame = new PlotFrame("x", "y", "Getting Started");
        plotFrame.setSize(400, 400); // window size
        plotFrame.setPreferredMinMax(0, 10, 0, 15); // x and y ranges
        plotFrame.setConnected(true); // if you want to connect the dots
        plotFrame.setDefaultCloseOperation(3);  // if you want closing the graph to end the program
        plotFrame.setVisible(true); // need this to show the graph, it is false by default

        // Data set 0 (red line)
        plotFrame.setLineColor(0, Color.RED);
        for (int i = 0; i < 10; i++) {
            plotFrame.append(0 , i, fx.evaluateWith(i));
        }
//        plotFrame.append(0, 0, 0);
//        plotFrame.append(0, 1, 1);
//        plotFrame.append(0, 2, 2);
//        plotFrame.append(0, 3, 3);

        // Data set 1 (green line)
        plotFrame.setLineColor(1, Color.GREEN);
        for (int i = 0; i < 10; i++) {
            plotFrame.append(1 , i, vx.evaluateWith(i));
        }
//        plotFrame.append(1, 0, 0);
//        plotFrame.append(1, 1, 1);
//        plotFrame.append(1, 1.5, 2.25);
//        plotFrame.append(1, 2, 4);
//        plotFrame.append(1, 2.5, 6.25);
//        plotFrame.append(1, 3, 9);
//        plotFrame.append(1, 3.5, 12.25);

        // Example of a Trail.
        Trail trail = new Trail();
        trail.color = Color.ORANGE;
        plotFrame.addDrawable(trail); // add the trail to the plot frame

//        // Add trail points.
//        trail.addPoint(0, 0);
//        trail.addPoint(4, 3);
//        trail.addPoint(8, 9);
//        trail.addPoint(16, 18);
        for (int i = 0; i < 10; i++) {
            trail.addPoint(i, gx.evaluateWith(i));
        }
    }

    /**
     * Go to /test/GettingStartedTest.java to see how to test this method with Junit.
     *
     * @param a first num to add
     * @param b second num to add
     * @return the sum
     */
    public static int add(int a, int b) {
        return a + b;
    }
}
