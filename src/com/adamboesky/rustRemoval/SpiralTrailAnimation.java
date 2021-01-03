package com.adamboesky.rustRemoval;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.frames.PlotFrame;

import java.util.Random;

public class SpiralTrailAnimation extends AbstractSimulation {
    // Set up global variables.
    PlotFrame plotFrame = new PlotFrame("x", "meters from ground", "Spiral Trail");
    double totalTime;

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {

    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */

    @Override
    public void initialize() {
        DrawableShape rect = DrawableShape.createRectangle(3,3, 4, 4);
        plotFrame.addDrawable(rect); //Add rectangle to plot frame
        plotFrame.setVisible(true); // Required to show plot frame.
    }
    public void doStep () {

    }
    /**
     * Optional method, tied to Stop button.
     */
    @Override
    public void stop(){

    }

    public static void main(String[] args){
        SimulationControl.createApp(new SpiralTrailAnimation());
    }
}
