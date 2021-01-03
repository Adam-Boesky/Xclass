package com.adamboesky.projectile;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;
import java.applet.*;
import java.net.*;

import java.awt.*;

public class BasketballMultipleSimulationApp extends AbstractSimulation {

    // Declare necessary components of field and simulation:
    private PlotFrame court = new PlotFrame("Court Width(m)", "Meters High(m)", "Basketball Court");
    private PlotFrame madeShotsGraph = new PlotFrame("Theta(°)", "Initial Vecloity(m/s)", "Shots Made");
    private DrawableShape[] balls = new DrawableShape[180];
    private DrawableShape backboard = DrawableShape.createRectangle(27.532, 3.3525, .2, 2);
    private Trail rim = new Trail();
    private Particle[] ballParticles = new Particle[180];
    private double timeStep = .05;
    private double initialBallVelocity = 18;
    private DrawableShape courtFloor = DrawableShape.createRectangle(15,-5, 100, 10);
    private double distRim1 = 0;
    private double distRim2 = 0;
    private double distBackboard = 0;
    private double stepsSinceBounce = 0;
    private double[] anglesMade = new double[180];
    private boolean[] shotsDone = new boolean[180];
    private int[] numberOfTimeAngleIsMade = new int[180];

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {
        for (int i = 0; i < 180; i++) {
            shotsDone[i] = false;
        }
        control.setValue("Time step(s)", timeStep);
        ballParticles = new Particle[180];

        // See which angle is the best so far:
        int bestAngle = 0;
        for (int i = 0; i < 180; i++) {
            if(numberOfTimeAngleIsMade[i] > numberOfTimeAngleIsMade[bestAngle]){
                bestAngle = i;
            }
        }
        double bestAngleDouble = bestAngle;
        System.out.println("The best angle so far is " + (bestAngleDouble/2) + "°");
    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {
        // Reset the number of shots made per angle:
        for (int i = 0; i < 180; i++) {
            anglesMade[i] = 0;
        }

        // Set the ball and particle values for all 180 of them and set the first point of the trail:
        for (int i = 0; i < 180; i++) {
            ballParticles[i] = new Particle(0, 1.9, 0, 0, 0, 0, .5, false);
            balls[i] = balls[i].createCircle(ballParticles[i].getX(), ballParticles[i].getY(), .25);
        }

        // Set size, range, and location of the field and graph:
        court.setPreferredMinMax(0, 30, 0, 16.8);
        court.setSize(1250, 700);
        court.setLocation(0,0);
        madeShotsGraph.setPreferredMinMax(0, 95, 0, 95);
        madeShotsGraph.setLocation(980, 0);

        // Set the beginning values of the particle.
        for (int i = 0; i < 180; i++) {
            balls[i].setX(ballParticles[i].getX());
            balls[i].setY(ballParticles[i].getY());
        }

        // Set first point of the trail and add points to the rim trail:
        rim.addPoint(26.7065, 3.048);
        rim.addPoint(27.3935, 3.048);
        rim.color = Color.RED;

        // Set the components visible:
        for (int i = 0; i < 180; i++) {
            court.addDrawable(balls[i]);
        }
        courtFloor.color = Color.BLACK;
        court.addDrawable(courtFloor);
        court.addDrawable(rim);
        backboard.edgeColor = Color.BLACK;
        court.addDrawable(backboard);

        // Get values from control:
        timeStep = control.getDouble("Time step(s)");

        for (int i = 0; i < 180; i++) {
            // Convert theta into radians and set velocities based off of theta:
            double doubleI = (double)i;
            double thetaRadians = (doubleI/2) * (Math.PI / 180);
            ballParticles[i].setXV(initialBallVelocity * Math.cos(thetaRadians));
            ballParticles[i].setYV(initialBallVelocity * Math.sin(thetaRadians));
        }
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {
        // Make sure that the graph is in the correct position:
        if(ballParticles[0].getY() == 1.9){
            madeShotsGraph.setLocation(980, 0);
        }

        for (int i = 0; i < 180; i++) {
            if (shotsDone[i] == false) {

                // Hold the previous ball coordinates for later use:
                double xPrev = ballParticles[i].getX();
                double yPrev = ballParticles[i].getY();

                // Get the distance that the center of the ball is from the front and back of the rim:
                distRim1 = Math.sqrt(((ballParticles[i].getX() - 26.7065) * (ballParticles[i].getX() - 26.7065)) +
                        ((ballParticles[i].getY() - 3.048) * (ballParticles[i].getY() - 3.048)));
                distRim2 = Math.sqrt(((ballParticles[i].getX() - 27.3935) * (ballParticles[i].getX() - 27.3935)) +
                        ((ballParticles[i].getY() - 3.048) * (ballParticles[i].getY() - 3.048)));

                // Get the distance that the center of the ball is from the backboard:
                if (ballParticles[i].getY() > 3.3525 - 1 && ballParticles[i].getY() < 3.3525 + 1) {
                    // Find the distance if the ball is at the same height as the backboard:
                    distBackboard = 27.432 - ballParticles[i].getX();
                } else if (ballParticles[i].getY() <= 3.3525 - 1) {
                    // Find the distance if the ball is below the backboard:
                    distBackboard = Math.sqrt(((ballParticles[i].getX() - 27.432) * (ballParticles[i].getX() - 27.432)) +
                            ((ballParticles[i].getY() - (3.3525 - 1)) * (ballParticles[i].getY() - (3.3525 - 1))));
                } else if (ballParticles[i].getY() >= 3.3525 + 1) {
                    // Find the distance if the ball is above the backboard:
                    distBackboard = Math.sqrt(((ballParticles[i].getX() - 27.432) * (ballParticles[i].getX() - 27.432)) +
                            ((ballParticles[i].getY() - (3.3525 + 1)) * (ballParticles[i].getY() - (3.3525 + 1))));
                }

                // STEP THE PARTICLE AND THEN CHECK FOR NEEDED BOUNCE OR RESET:

                // Update particle values. We will change the time step if the ball is within .5/.75m of the backboard or rim so as
                // to increase accuracy and avoid time step issues with the ball bouncing:
                if (distRim1 < .75 || distRim2 < .5 || distBackboard < .75) {
                    ballParticles[i].stepResistanceProp(.001, ballParticles[i].getMass(), .062, 9.8);
                } else {
                    ballParticles[i].stepResistanceProp(timeStep, ballParticles[i].getMass(), .062, 9.8);
                }

                // Check if the ball is in an area where it is able to go through the hoop so that we can check if the shot is made:
                if (ballParticles[i].getX() > 26.5 && ballParticles[i].getY() <= 3.048 && yPrev > 3.048) {

                    // Calculate the percent of the weighted average that will be used:
                    double percent = (3.048 - ballParticles[i].getY()) / (yPrev - ballParticles[i].getY());

                    // Calculate the location of the x coordinate of the ball when it through passed the hoop:
                    double xPass = (percent * xPrev) + ((1 - percent) * ballParticles[i].getX());

                    // See if it went through the hoop:
                    if ((xPass > 26.7065 && xPass < 27.3935) ||
                            (ballParticles[i].getY() == 3.048 && (ballParticles[i].getX() > 26.7065 && ballParticles[i].getX() < 27.3935))) {
                        control.println("Swish");
                        numberOfTimeAngleIsMade[i]++;

                        shotsDone[i] = true;
                        ballParticles[i].stop();
                        ballParticles[i].setStopped();

                        anglesMade[i]++;

                        // Make the noise of the ball going through the hoop:
                        try {
                            AudioClip clip = Applet.newAudioClip(
                                    new URL("file:///Users/student/Downloads/Swish+2%20(1).wav"));
                            clip.play();
                        } catch (MalformedURLException murle) {
                            System.out.println(murle);
                        }

                        // Plot initial velocity and angle of the shots that went in:
                        double doubleI = i;
                        DrawableShape dataPoint = DrawableShape.createCircle(doubleI/2, initialBallVelocity, 1);
                        dataPoint.setMarkerColor(Color.BLACK, Color.BLACK);
                        madeShotsGraph.addDrawable(dataPoint);
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
                        ballParticles[i].bounce(.7, 26.7065, 3.048);
                    } else if (stepsSinceBounce < 20 && distRim2 <= .125) {
                        ballParticles[i].bounce(.7, 27.3935, 3.048);
                    }
                    stepsSinceBounce = 0;
                }

                // Check if the ball hits the backboard:
                if (distBackboard <= .125) {
                    control.println("backboard");

                    // Bounce the ball:
                    ballParticles[i].bounce(.7, 27.432, ballParticles[i].getY());

                    // Move the ball so that it is definitively not inside of the backboard:
                    ballParticles[i].setX(27.306);

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
                if (ballParticles[i].getX() > 27.532 || ballParticles[i].getX() < 0 || ballParticles[i].getY() < 0) {
                    // Update the values of the velocity and theta:
                    shotsDone[i] = true;
                    ballParticles[i].stop();
                    ballParticles[i].setStopped();
                }

                // Make sure that the ball is still moving
                if (Math.abs(ballParticles[i].getXV()) + Math.abs(ballParticles[i].getYV()) < 1 &&
                        (distRim1 < .125 || distRim2 < .125 || distBackboard < .125)) {
                    shotsDone[i] = true;
                    ballParticles[i].stop();
                    ballParticles[i].setStopped();
                }

                // Move the basketball according to particle values:
                balls[i].setX(ballParticles[i].getX());
                balls[i].setY(ballParticles[i].getY());

                // Increase the number of steps since the last bounce by 1:
                stepsSinceBounce++;
            }
        }

        // Check if the program is done by seeing if all the balls are stopped:
        int counter = 0;
        for (int i = 0; i < 180; i++) {
            if(ballParticles[i].getIsStopped()){
                counter++;
            }
        }
        if(counter == 180){
            initialBallVelocity += .5;
            reset();
            initialize();
        }
    }

    /**
     * Required main method, runs the simulation.
     * @param args - required argument component
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new BasketballMultipleSimulationApp());
    }
}
