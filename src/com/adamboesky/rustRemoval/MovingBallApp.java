package com.adamboesky.rustRemoval;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.frames.PlotFrame;

/***
 * com.adamboesky.rustRemoval.MovingBallApp is an extension of AbstractSimulation (an abstract class).
 *
 * An abstract class is a class that almost works except that the author intentionally left some methods empty. It is up
 * to the programmer who uses it to complete those empty methods for the class to fully work.
 *
 * In AbstractSimulation, these methods need to be completed:
 *     1. reset (adds options to the Control Panel)
 *     2. initialize (gets data from the Control Panel and sets up the graph)
 *     3. doStep (actions to take to do the animation, invoked every 1/10 second)
 *
 * You also need a main method to run the simulation (scroll to bottom).
 */
public class MovingBallApp extends AbstractSimulation {
    // Set up global variables.
    PlotFrame plotFrame = new PlotFrame("x", "meters from ground", "Moving Ball");
    Circle circle = new Circle();
    double totalTime;

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {
        control.setValue("Starting Y position", 100);
        control.setValue("Starting X position", 100);
    }


    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {
        // Get information from the control panel.
        double startingY = control.getDouble("Starting Y position");
        circle.setY(startingY);
        double startingX = control.getDouble("Starting X position");
        circle.setX(startingX);

        // Instead of appending x, y coordinates to plot frame,
        //    add the Circle which maintains its own x, y.
        plotFrame.addDrawable(circle);

        // Configure plot frame
        plotFrame.setPreferredMinMax(-25, 25, 0, startingY); // Scale of graph.
        plotFrame.setDefaultCloseOperation(3); // Make it so x'ing out of the graph stops the program.
        plotFrame.setVisible(true); // Required to show plot frame.
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {
        // Change y. (It will re-draw itself.)
            circle.setY(circle.getY() - 1);
        circle.setX(circle.getX() + 1);

        totalTime++;
    }

    /**
     * Optional method, tied to Stop button.
     */
    @Override
    public void stop(){
        System.out.println(totalTime/10 + " secs to travel "
                + Math.abs(control.getDouble("Starting Y position") - circle.getY())+ " meters vertically.");
        }

    /**
     * Required main method, runs the simulation.
     * @param args
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new MovingBallApp());
    }
}
