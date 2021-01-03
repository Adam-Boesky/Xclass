package com.adamboesky.projectile;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;
import java.applet.*;
import java.net.*;

import java.awt.*;

public class BasketballSimulationApp extends AbstractSimulation {

    // Declare necessary components of field and simulation:
    private PlotFrame court = new PlotFrame("Court Width(m)", "Meters High(m)", "Basketball Court");
    private Circle ball = new Circle();
    private DrawableShape backboard = DrawableShape.createRectangle(27.532, 3.3525, .2, 2);
    private Trail rim = new Trail();
    private Particle ballParticle = new Particle(0, 1.9, 0, 0, 0, 0, .5, false);
    private double timeStep = .05;
    private Trail ballTrail = new Trail();
    private double initialBallVelocity = 18.5;
    private double theta = 50;
    private DrawableShape courtFloor = DrawableShape.createRectangle(15,-5, 100, 10);
    private double distRim1 = 0;
    private double distRim2 = 0;
    private double distBackboard = 0;
    private double stepsSinceBounce = 0;

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {
        // Reset values on the control for the user to change:
        control.setValue("Time step(s)", timeStep);
        control.setValue("Theta(°)", theta);
        control.setValue("Initial Ball Velocity(m/s)", initialBallVelocity);

        // Reset the particle, ball, and trail:
        ballParticle = new Particle(0, 1.9, 0, 0, 0, 0, .5, false);
        ballTrail = new Trail();
        court.removeDrawable(ball);
        ball = new Circle();
    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {

        // Set the ball and particle values and set the first point of the trail:
        ballParticle = new Particle(0, 1.9, 0, 0, 0, 0, .5, false);
        ball.setX(0);
        ball.setY(1.9);
        ballTrail.addPoint(ballParticle.getX(), ballParticle.getY());

        // Set size, range, and location of the field and graph:
        court.setPreferredMinMax(0, 30, 0, 16.8);
        court.setSize(1250, 700);
        court.setLocation(0,0);

        // Add points to the rim trail:
        rim.addPoint(26.7065, 3.048);
        rim.addPoint(27.3935, 3.048);
        rim.color = Color.RED;

        // Set the components visible and colored properly:
        court.addDrawable(ball);
        court.addDrawable(ballTrail);
        courtFloor.color = Color.BLACK;
        court.addDrawable(courtFloor);
        court.addDrawable(rim);
        backboard.edgeColor = Color.BLACK;
        court.addDrawable(backboard);

        // Get values from control:
        timeStep = control.getDouble("Time step(s)");
        theta = control.getDouble("Theta(°)");
        initialBallVelocity = control.getDouble("Initial Ball Velocity(m/s)");

        // Convert theta into radians and set velocities based off of theta:
        double thetaRadians = theta * (Math.PI / 180);
        ballParticle.setXV(initialBallVelocity * Math.cos(thetaRadians));
        ballParticle.setYV(initialBallVelocity * Math.sin(thetaRadians));
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {

        // Hold the previous ball coordinates for later use:
        double xPrev = ballParticle.getX();
        double yPrev = ballParticle.getY();

        // Get the distance that the center of the ball is from the front and back of the rim:
        distRim1 = Math.sqrt(((ballParticle.getX() - 26.7065) * (ballParticle.getX() - 26.7065)) +
                ((ballParticle.getY() - 3.048) * (ballParticle.getY() - 3.048)));
        distRim2 = Math.sqrt(((ballParticle.getX() - 27.3935) * (ballParticle.getX() - 27.3935)) +
                ((ballParticle.getY() - 3.048) * (ballParticle.getY() - 3.048)));

        // Get the distance that the center of the ball is from the backboard:
        if (ballParticle.getY() > 3.3525 - 1 && ballParticle.getY() < 3.3525 + 1) {
            // Find the distance if the ball is at the same height as the backboard:
            distBackboard = 27.432 - ballParticle.getX();
        } else if (ballParticle.getY() <= 3.3525 - 1) {
            // Find the distance if the ball is below the backboard:
            distBackboard = Math.sqrt(((ballParticle.getX() - 27.432) * (ballParticle.getX() - 27.432)) +
                    ((ballParticle.getY() - (3.3525 - 1)) * (ballParticle.getY() - (3.3525 - 1))));
        } else if (ballParticle.getY() >= 3.3525 + 1) {
            // Find the distance if the ball is above the backboard:
            distBackboard = Math.sqrt(((ballParticle.getX() - 27.432) * (ballParticle.getX() - 27.432)) +
                    ((ballParticle.getY() - (3.3525 + 1)) * (ballParticle.getY() - (3.3525 + 1))));
        }

        // STEP THE PARTICLE AND THEN CHECK FOR NEEDED BOUNCE OR RESET:

        // Update particle values. We will change the time step if the ball is within .75/.5/1m of the backboard or rim so as
        // to increase accuracy and avoid time step issues with the ball bouncing:
        if (distRim1 < 1 || distRim2 < .5 || distBackboard < .75) {
            ballParticle.stepResistanceProp(.001, ballParticle.getMass(), .062, 9.8);
        } else {
            ballParticle.stepResistanceProp(timeStep, ballParticle.getMass(), .062, 9.8);
        }

        // Check if the ball is in an area where it is able to go through the hoop so that we can check if the shot is made:
        if (ballParticle.getX() > 26.5 && ballParticle.getY() <= 3.048 && yPrev > 3.048) {

            // Calculate the percent of the weighted average that will be used:
            double percent = (3.048 - ballParticle.getY()) / (yPrev - ballParticle.getY());

            // Calculate the location of the x coordinate of the ball when it through passed the hoop:
            double xPass = (percent * xPrev) + ((1 - percent) * ballParticle.getX());

            // See if it went through the hoop:
            if ((xPass > 26.7065 && xPass < 27.3935) ||
                    (ballParticle.getY() == 3.048 && (ballParticle.getX() > 26.7065 && ballParticle.getX() < 27.3935))) {
                control.println("Swish");
                System.out.println(initialBallVelocity);

                // Move the basketball according to particle values:
                ball.setX(ballParticle.getX());
                ball.setY(ballParticle.getY());

                // Add a trail point at the updated location of the ball:
                ballTrail.addPoint(ballParticle.getX(), ballParticle.getY());

                // Increase initial velocity by .1 and reset/initialize:
                initialBallVelocity+=.1;
                reset();
                initialize();

                // Make the noise of the ball going through the hoop:
                try {
                    AudioClip clip = Applet.newAudioClip(
                            new URL("file:///Users/student/Downloads/Swish+2%20(1).wav"));
                    clip.play();
                } catch (MalformedURLException murle) {
                    System.out.println(murle);
                }
            }
        }

        // Check if the ball hits the rim. We will be making sure that the ball has not bounced too recently (using the
        // stepsSinceBounce) so as to avoid a time step issue where the ball ends up inside of the rim:
        if ((stepsSinceBounce > 20 && distRim1 <= .125) || (stepsSinceBounce > 20 && distRim2 <= .125)) {
            control.println("rim");
            try {
                AudioClip clip = Applet.newAudioClip(
                        new URL("file:///Users/student/IdeaProjects/XClassProjects/src/com/adamboesky/projectile/soundEffects/Back+Board.wav"));
                clip.play();
            } catch (MalformedURLException murle) {
                System.out.println(murle);
            }

            // Bounce the ball according to the rim that it hits:
            if (stepsSinceBounce > 20 && distRim1 <= .125) {
                ballParticle.bounce(.7, 26.7065, 3.048);
            } else if (stepsSinceBounce < 20 && distRim2 <= .125) {
                ballParticle.bounce(.7, 27.3935, 3.048);
            }
            stepsSinceBounce = 0;
        }

        // Check if the ball hits the backboard:
        if (distBackboard <= .125) {
            control.println("backboard");

            // Bounce the ball:
            ballParticle.bounce(.7, 27.432, ballParticle.getY());

            // Move the ball so that it is definitively not inside of the backboard:
            ballParticle.setX(27.306);

            // Make the sound of the ball hitting the backboard:
            try {
                AudioClip clip = Applet.newAudioClip(
                        new URL("file:///Users/student/IdeaProjects/XClassProjects/src/com/adamboesky/projectile/soundEffects/Back+Board.wav"));
                clip.play();
            } catch (MalformedURLException murle) {
                System.out.println(murle);
            }
        }

        // Check if the ball has hit the floor or went out of bounds:
        if (ballParticle.getX() > 27.532 || ballParticle.getX() < 0 || ballParticle.getY() < 0) {
            // Update the values of the velocity and theta:
            initialBallVelocity+=.1;
            reset();
            initialize();

            // Move the basketball according to particle values:
            ball.setX(ballParticle.getX());
            ball.setY(ballParticle.getY());

            // Add a trail point at the updated location of the ball:
            ballTrail.addPoint(ballParticle.getX(), ballParticle.getY());
        }

        // Make sure that the ball is still moving
        if (Math.abs(ballParticle.getXV()) + Math.abs(ballParticle.getYV()) < 1 &&
                (distRim1 < .125 || distRim2 < .125 || distBackboard < .125)) {
            initialBallVelocity+=.1;
            reset();
            initialize();
            ballParticle.setStopped();

            // Move the basketball according to particle values:
            ball.setX(ballParticle.getX());
            ball.setY(ballParticle.getY());

            // Add a trail point at the updated location of the ball:
            ballTrail.addPoint(ballParticle.getX(), ballParticle.getY());
        }

        // Move the basketball according to particle values:
        ball.setX(ballParticle.getX());
        ball.setY(ballParticle.getY());

        // Add a trail point at the updated location of the ball:
        ballTrail.addPoint(ballParticle.getX(), ballParticle.getY());

        // Increase the number of steps since the last bounce by 1:
        stepsSinceBounce++;
    }

    /**
     * Required main method, runs the simulation.
     * @param args - required argument component
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new BasketballSimulationApp());
    }
}
