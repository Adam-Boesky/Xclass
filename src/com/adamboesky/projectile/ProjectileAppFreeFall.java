package com.adamboesky.projectile;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;

public class ProjectileApp extends AbstractSimulation {
    // Declare the plot frames and trails that will be plotting the data of the free fall particles.
    PlotFrame plotFrameDrop = new PlotFrame("X", "Y", "Falling Ball Simulation");
    PlotFrame pVT = new PlotFrame("Time (Seconds)", "Distance From Ground (Meters)",
            "Position Versus Time");
    Trail trailPVT1 = new Trail();
    Trail trailPVT2 = new Trail();
    Trail trailPVT3 = new Trail();
    PlotFrame vVT = new PlotFrame("Time (Seconds)", "Velocity (Meters/Second)",
            "Velocity Versus Time");
    Trail trailVVT1 = new Trail();
    Trail trailVVT2 = new Trail();
    Trail trailVVT3 = new Trail();
    PlotFrame aVT = new PlotFrame("Time (Seconds)", "Acceleration (Meters/Second^2)",
            "Acceleration Versus Time");
    Trail trailAVT1 = new Trail();
    Trail trailAVT2 = new Trail();
    Trail trailAVT3 = new Trail();

    // Declare the particles and the circles for each particle for the free fall simulation.
    Particle particle1 = new Particle(0, 100, 0, 0, 0, 0, 10);
    Circle pCircle1 = new Circle();

    Particle particle2 = new Particle(-10, 100, 0, 0, 0, 0, 10);
    Circle pCircle2 = new Circle();

    Particle particle3 = new Particle(10, 100, 0, 0, 0, 0, 10);
    Circle pCircle3 = new Circle();

    // Set time equal to 0.
    double totalTime = 0;

    // Declare the Betas whose values will be established by the user.
    double betaGreen = 0;
    double betaBlue = 0;

    // Declare the mass of the earth and g equal to 0.
    double massEarth = 5.972;
    double g = 0;
    double forceGravity = 0;

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {
        // Retake the beta values for the particles.
        control.setValue("Beta value for green particle", betaGreen);
        control.setValue("Beta value for blue particle", betaBlue);
        control.setValue("Mass of the earth in terms of 10^24 kg(the mass of our earth would be 5.972", massEarth);

        // Reset the particle values.
        particle1 = new Particle(0, 100, 0, 0, 0, 0, 10);
        particle2 = new Particle(-10, 100, 0, 0, 0, 0, 1000000);
        particle3 = new Particle(10, 100, 0, 0, 0, 0, 10);

        // Remove trails to the velocity versus time graph.
        vVT.removeDrawable(trailVVT1);
        vVT.removeDrawable(trailVVT2);
        vVT.removeDrawable(trailVVT3);

        // Remove trails to the position versus time graph.
        pVT.removeDrawable(trailPVT1);
        pVT.removeDrawable(trailPVT2);
        pVT.removeDrawable(trailPVT3);

        // Remove trails from the acceleration versus time graph.
        aVT.removeDrawable(trailAVT1);
        aVT.removeDrawable(trailAVT2);
        aVT.removeDrawable(trailAVT3);

        // Reset the trails for the position versus time graph.
        trailPVT1 = new Trail();
        trailPVT2 = new Trail();
        trailPVT3 = new Trail();

        // Reset the trails for the velocity versus time graph.
        trailVVT1 = new Trail();
        trailVVT2 = new Trail();
        trailVVT3 = new Trail();

        // Reset the trails for the acceleration versus time graph.
        trailAVT1 = new Trail();
        trailAVT2 = new Trail();
        trailAVT3 = new Trail();

        // Reset the time.
        totalTime = 0;
    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {
        // Get information from the control panel.
        pCircle1.setY(particle1.getY());
        pCircle1.setX(particle1.getX());
        pCircle1.color = Color.RED;

        // Get information from the control panel.
        pCircle2.setY(particle2.getY());
        pCircle2.setX(particle2.getX());
        Color niceGreen = new Color(3, 131, 0);
        pCircle2.color = niceGreen;

        // Get information from the control panel.
        pCircle3.setY(particle3.getY());
        pCircle3.setX(particle3.getX());
        pCircle3.color = Color.BLUE;

        // Set the trail colors.
        trailPVT1.color = Color.RED;
        trailVVT1.color = Color.RED;
        trailAVT1.color = Color.RED;
        trailPVT2.color = niceGreen;
        trailVVT2.color = niceGreen;
        trailAVT2.color = niceGreen;
        trailPVT3.color = Color.BLUE;
        trailVVT3.color = Color.BLUE;
        trailAVT3.color = Color.BLUE;

        // Instead of appending x, y coordinates to plot frame, add the Circle which maintains its own x, y.
        plotFrameDrop.addDrawable(pCircle1);
        plotFrameDrop.addDrawable(pCircle2);
        plotFrameDrop.addDrawable(pCircle3);

        // Configure plot frame
        plotFrameDrop.setPreferredMinMax(-25, 25, -200, 200); // Scale of graph.
        plotFrameDrop.setDefaultCloseOperation(3); // Make it so x'ing out of the graph stops the program.
        plotFrameDrop.setVisible(true); // Required to show plot frame.

        // Set preferred min max for the plots
        pVT.setPreferredMinMax(0, 5, 0, 100);
        vVT.setPreferredMinMax(0, 5, -50, 0);
        aVT.setPreferredMinMax(0, 5, -10, 0);

        // Get the values from the control panel.
        betaGreen = control.getDouble("Beta value for green particle");
        betaBlue = control.getDouble("Beta value for blue particle");
        massEarth = control.getDouble("Mass of the earth in terms of 10^24 kg(the mass of our earth would be 5.972");

        forceGravity = (((6.67 * Math.pow(10, -11) * (massEarth * Math.pow(10, 24)) * particle1.getMass()) /
                (6371 * 6371)));
        g = forceGravity / particle1.getMass();
        System.out.println(g);
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {
        // Particle 1 free fall.
        pCircle1.setX(particle1.getX());
        pCircle1.setY(particle1.getY());

        // Particle 2 free fall.
        pCircle2.setX(particle2.getX());
        pCircle2.setY(particle2.getY());

        // Particle 3 free fall.
        pCircle3.setX(particle3.getX());
        pCircle3.setY(particle3.getY());

        // Update all of the particle attributes.
        particle1.step(.1, 9.8);
        particle2.stepResistanceProp(.1, 10, betaGreen, 9.8);
        particle3.stepResistancePropSquared(.1, 10, betaBlue, 9.8);

        // Add data points for particle 1.
        trailPVT1.addPoint(totalTime, particle1.getY());
        trailVVT1.addPoint(totalTime, particle1.getYV());
        trailAVT1.addPoint(totalTime, particle1.getYAcceleration());

        // Add data points for particle 2.
        trailPVT2.addPoint(totalTime, particle2.getY());
        trailVVT2.addPoint(totalTime, particle2.getYV());
        trailAVT2.addPoint(totalTime, particle2.getYAcceleration());

        // Add data points for particle 3.
        trailPVT3.addPoint(totalTime, particle3.getY());
        trailVVT3.addPoint(totalTime, particle3.getYV());
        trailAVT3.addPoint(totalTime, particle3.getYAcceleration());

        // Update the total time.
        totalTime = totalTime + .1;

        // Add trails to the acceleration versus time graph.
        aVT.addDrawable(trailAVT1);
        aVT.addDrawable(trailAVT2);
        aVT.addDrawable(trailAVT3);

        // Add trails to the velocity versus time graph.
        vVT.addDrawable(trailVVT1);
        vVT.addDrawable(trailVVT2);
        vVT.addDrawable(trailVVT3);

        // Add trails to the position versus time graph.
        pVT.addDrawable(trailPVT1);
        pVT.addDrawable(trailPVT2);
        pVT.addDrawable(trailPVT3);
    }

    /**
     * Required main method, runs the simulation.
     * @param args
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new ProjectileApp());
    }
}
