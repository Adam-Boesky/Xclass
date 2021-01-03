package com.adamboesky.projectile;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import java.awt.*;
import java.util.ArrayList;

public class BaseballSimulationApp extends AbstractSimulation {

    // Declare necessary components of field and simulation:
    private PlotFrame field = new PlotFrame("Meters From Homeplate", "Meters High", "Fenway Park");
    private DrawableShape monster = DrawableShape.createRectangle(101, 5.65, 2, 11.3);
    private DrawableShape ball = DrawableShape.createCircle(0, 1, .075);
    private Particle ballParticle = new Particle(0, 1, 0, 0, 0, 0, .145, false);
    private double timeStep = .1;
    private boolean homerun = false;
    private double theta = 30;
    private Trail ballTrail = new Trail();
    private ArrayList<Double> degreeList = new ArrayList<>();
    private double particleSpeedTester = 42;

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {
        control.setValue("Time step", timeStep);
        ballParticle.setX(0);
        ballParticle.setY(1);
        ballTrail = new Trail();
        ballParticle = new Particle(0, 1, 0, 0, 0, 0, .145, false);
    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {

        // Set size, range, and location of the field
        field.setPreferredMinMax(0, 110, 0, 110);
        field.setSize(1250, 700);
        field.setLocation(0,0);

        // Set the beginning values of the particle.
        ball.setX(ballParticle.getX());
        ball.setY(ballParticle.getY());

        // Set first point of the trail:
        ballTrail.addPoint(0, 1);

        // Set the wall and the baseball visible:
        field.addDrawable(ball);
        monster.color = new Color(0x037B00);
        monster.edgeColor = new Color(0x037B00);
        field.addDrawable(monster);
        field.addDrawable(ballTrail);

        // Get values from control:
        timeStep = control.getDouble("Time step");

        // Calculate theta in terms of radians and set the initial ball velocity:
        double thetaRadians = theta * (Math.PI / 180); // Calculate the theta value in terms of radians bc Math.trig uses radians
        ballParticle.setXV(particleSpeedTester * Math.cos(thetaRadians));
        ballParticle.setYV(particleSpeedTester * Math.sin(thetaRadians));
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {

        // Update particle values:
        ballParticle.stepResistanceProp(timeStep, ballParticle.getMass(), .03, 9.8);

        // Move the baseball according to particle values:
        ball.setX(ballParticle.getX());
        ball.setY(ballParticle.getY());

        // Plot trail points:
        ballTrail.addPoint(ballParticle.getX(), ballParticle.getY());

        if(ballParticle.getX() >= 100 && ballParticle.getY() >= 11.375) {
            theta = theta + .5;
            homerun = true;
            degreeList.add(theta);
            control.println("Home Run!\nDegrees: " + theta + "\nInitial Velocity:" + particleSpeedTester);
            reset();
            initialize();
        }
        // Find if the hit is not a homerun and increase theta if so:
        if(ballParticle.getX() > 101 || ballParticle.getY() <= 0) {
            theta = theta + .5;
            reset();
            initialize();
        }
        if(theta >= 60) {
            theta = 35;
            particleSpeedTester += .5;
            double thetaRadians = theta * (Math.PI / 180);
            ballParticle.setXV(particleSpeedTester * Math.cos(thetaRadians));
            ballParticle.setYV(particleSpeedTester * Math.sin(thetaRadians));
        }
    }

    /**
     * Required main method, runs the simulation.
     * @param args - required argument component
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new BaseballSimulationApp());
    }
}