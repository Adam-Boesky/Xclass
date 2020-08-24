package com.adamboesky;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.frames.PlotFrame;

import java.util.Random;

public class RandomWalkApp extends AbstractSimulation {
    // Set up global variables.
    PlotFrame plotFrame = new PlotFrame("x", "meters from ground", "Moving Ball");
    Circle[] circle = new Circle[50];
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
        for (int i = 0; i < 50; i++) {
            circle[i] = new Circle();
            // Get information from the control panel.
            double startingY = control.getDouble("Starting Y position");
            circle[i].setY(startingY);
            double startingX = control.getDouble("Starting X position");
            circle[i].setX(startingX);

            // Instead of appending x, y coordinates to plot frame,
            //    add the Circle which maintains its own x, y.
            plotFrame.addDrawable(circle[i]);

            // Configure plot frame
            plotFrame.setPreferredMinMax(-50, 50, 0, 100); // Scale of graph.
            plotFrame.setDefaultCloseOperation(3); // Make it so x'ing out of the graph stops the program.
            plotFrame.setVisible(true); // Required to show plot frame.

            /**
             * Required method, invoked once every 1/10 second until Stop is pressed.
             * The doStep method is also called when the Step button is pressed.
             */

            /**
             * Required main method, runs the simulation.
             * @param args
             */
        }
    }
    public void doStep () {
        for (int i = 0; i < 50; i++) {
            // Change y. (It will re-draw itself.)
            Random rand = new Random();
            circle[i].setY(circle[i].getY() + (-1 + rand.nextInt(3)));
            circle[i].setX(circle[i].getX() + (-1 + rand.nextInt(3)));

            totalTime++;
        }
    }
    /**
     * Optional method, tied to Stop button.
     */
    @Override
    public void stop(){
        for (int i = 0; i < 50; i++) {
            System.out.println(totalTime / 10 + " secs to travel "
                    + Math.abs(control.getDouble("Starting Y position") - circle[i].getY()) + " meters vertically.");
        }
    }

    public static void main(String[] args){
        SimulationControl.createApp(new RandomWalkApp());
    }
}