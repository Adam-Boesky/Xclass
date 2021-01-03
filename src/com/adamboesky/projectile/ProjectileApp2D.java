package com.adamboesky.projectile;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;

public class ProjectileApp2D extends AbstractSimulation {
    // Declare important values.
    private PlotFrame plotFrameProjectile = new PlotFrame("X", "Y", "2D Projectile Simulation");
    private Particle particle = new Particle(0, 0, 0, 0, 0, 0, 10, false);
    private Circle circle = new Circle();
    private Trail particleTrail = new Trail();
    private double beta = 0;
    private double theta = 45;
    private double speed = 20;
    private double mass = 10;
    private double totalTime = 0;
    private double timeStep = .1;

    // Declare the plot frames:
    private PlotFrame pVT = new PlotFrame("Time (Seconds)", "Distance From Ground (Meters)",
            "Position Versus Time");
    private Trail trailPVTX = new Trail();
    private Trail trailPVTY = new Trail();
    private PlotFrame vVT = new PlotFrame("Time (Seconds)", "Velocity (Meters/Second)",
            "Velocity Versus Time");
    private Trail trailVVTX = new Trail();
    private Trail trailVVTY = new Trail();
    private PlotFrame aVT = new PlotFrame("Time (Seconds)", "Acceleration (Meters/Second^2)",
            "Acceleration Versus Time");
    private Trail trailAVTX = new Trail();
    private Trail trailAVTY = new Trail();

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {
        // Get beta and theta values for the particle.
        control.setValue("Beta value for the particle", beta);
        control.setValue("Theta value for the particle", theta);
        control.setValue("Initial velocity of the particle", speed);
        control.setValue("Mass of the particle", mass);
        control.setValue("Time step", timeStep);

        // Remove the trail and circle from the plot.
        plotFrameProjectile.removeDrawable(particleTrail);
        plotFrameProjectile.removeDrawable(circle);

        // Reset the particle and trail.
        particle = new Particle(0, 0, 0, 0, 0, 0, 10, false);
        particleTrail = new Trail();

        // Remove trails to the velocity versus time graph.
        vVT.removeDrawable(trailVVTX);
        vVT.removeDrawable(trailVVTY);

        // Remove trails to the position versus time graph.
        pVT.removeDrawable(trailPVTX);
        pVT.removeDrawable(trailPVTY);

        // Remove trails from the acceleration versus time graph.
        aVT.removeDrawable(trailAVTX);
        aVT.removeDrawable(trailAVTY);

        // Reset the trails for the position versus time graph.
        trailPVTX = new Trail();
        trailPVTY = new Trail();

        // Reset the trails for the velocity versus time graph.
        trailVVTX = new Trail();
        trailVVTY = new Trail();

        // Reset the trails for the acceleration versus time graph.
        trailAVTX = new Trail();
        trailAVTY = new Trail();

        // Reset the total time:
        totalTime = 0;
    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {
        control.print("All of the red lines are the X values and the black lines are the Y values");
        // Set the size of the graph.
        plotFrameProjectile.setPreferredMinMax(0, 100, 0, 50);
        plotFrameProjectile.setLocation(0,0);
        plotFrameProjectile.setSize(500,300);

        // Retrieve the values from the control panel.
        theta = control.getDouble("Theta value for the particle");
        beta = control.getDouble("Beta value for the particle");
        speed = control.getDouble("Initial velocity of the particle");
        mass = control.getDouble("Mass of the particle");
        timeStep = control.getDouble("Time step");

        // Set the trail colors.
        trailPVTX.color = Color.RED;
        trailVVTX.color = Color.RED;
        trailAVTX.color = Color.RED;
        trailPVTY.color = Color.BLACK;
        trailVVTY.color = Color.BLACK;
        trailAVTY.color = Color.BLACK;

        // Set graph min and max:
        pVT.setPreferredMinMax(-1, 5, -1, 50);
        pVT.setLocation(plotFrameProjectile.getX() + plotFrameProjectile.getWidth(), plotFrameProjectile.getY());
        vVT.setPreferredMinMax(-1, 5, -10, 10);
        vVT.setLocation(pVT.getX() + pVT.getWidth(), pVT.getY());
        aVT.setPreferredMinMax(-1, 5, -11, 11);
        aVT.setLocation(plotFrameProjectile.getX(), plotFrameProjectile.getY() + plotFrameProjectile.getHeight() + 23);

        // Set the beginning values of the particle.
        circle.setX(particle.getX());
        circle.setY(particle.getY());
        double thetaRadians = theta * (Math.PI / 180); // Calculate the theta value in terms of radians bc Math.trig uses radians
        particle.setXV(speed * Math.cos(thetaRadians));
        particle.setYV(speed * Math.sin(thetaRadians));
        circle.color = Color.BLACK;
        particleTrail.addPoint(0, 0);

        plotFrameProjectile.addDrawable(circle);
        plotFrameProjectile.addDrawable(particleTrail);
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {
        // Update the values of the particle.
        particle.stepResistanceProp(timeStep, mass, beta, 9.8);

        // Move the particle and add trail points.
        circle.setX(particle.getX());
        circle.setY(particle.getY());
        particleTrail.addPoint(particle.getX(), particle.getY());

        // Add the particle and trail to the plot frame.
        plotFrameProjectile.addDrawable(circle);
        plotFrameProjectile.addDrawable(particleTrail);

        // Add data points for particle X.
        trailPVTX.addPoint(totalTime, particle.getX());
        trailVVTX.addPoint(totalTime, particle.getXV());
        trailAVTX.addPoint(totalTime, particle.getXAcceleration());

        // Add data points for Y.
        trailPVTY.addPoint(totalTime, particle.getY());
        trailVVTY.addPoint(totalTime, particle.getYV());
        trailAVTY.addPoint(totalTime, particle.getYAcceleration());

        // Add trails to the acceleration versus time graph.
        aVT.addDrawable(trailAVTX);
        aVT.addDrawable(trailAVTY);

        // Add trails to the velocity versus time graph.
        vVT.addDrawable(trailVVTX);
        vVT.addDrawable(trailVVTY);

        // Add trails to the position versus time graph.
        pVT.addDrawable(trailPVTX);
        pVT.addDrawable(trailPVTY);

        // Update the total time.
        totalTime = totalTime + timeStep;
    }

    /**
     * Required main method, runs the simulation.
     * @param args - required argument component
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new ProjectileApp2D());
    }
}
