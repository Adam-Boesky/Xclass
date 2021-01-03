package com.adamboesky.orbitals;

import com.adamboesky.matrix.Matrix;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import javax.swing.*;
import java.awt.*;

public class OrbitalSimulationApp extends AbstractSimulation {
    private PlotFrame orbitalPlotFrame = new PlotFrame("X", "Y", "Orbital Simulation");

    // Masses and their circles:
    private Mass mass1 = new Mass(151.86E9, 0, 0, 30000, 0, 0, 5.972E24);
    private Mass mass2 = new Mass(0, 0, 0, 0, 0, 0, 1.989E30);
    private Mass mass3 = new Mass(100E9, 0, 0, -36524, 0, 0, 6E24);

    private Circle mass1Circle = new Circle();
    private Circle mass2Circle = new Circle();
    private Circle mass3Circle = new Circle();
    private Circle[] massCircles = new Circle[]{mass1Circle, mass2Circle, mass3Circle};

    private Trail mass1Trail = new Trail();
    private Trail mass2Trail = new Trail();
    private Trail mass3Trail = new Trail();
    private Trail[] trails = new Trail[]{mass1Trail, mass2Trail, mass3Trail};

    private double massMass1 = 5.972E24;
    private double massMass2 = 1.989E30;
    private double massMass3 = 6E24;

    private double mass1X = 151.86E9;
    private double mass2X = 0;
    private double mass3X = 100E9;

    private double mass1Y = 0;
    private double mass2Y = 0;
    private double mass3Y = 0;

    private double mass1VX = 0;
    private double mass2VX = 0;
    private double mass3VX = 0;

    private double mass1VY = 30000;
    private double mass2VY = 0;
    private double mass3VY = -36524;

    private double deltaTime = 10000;
    Mass[] masses = new Mass[3];

    private double G = 6.67E-11; // Gravitational constant

    /**
     * Technically optional, but the simulation won't work without it.
     * Adds options to the Control Panel.
     */
    @Override
    public void reset() {

        // Retake the beta values for the particles.
        control.setValue("Duration of timeStep", deltaTime);
        control.setValue("Mass of mass1", massMass1);
        control.setValue("Mass of mass2", massMass2);
        control.setValue("Mass of mass3", massMass3);

        control.setValue("X of mass1", mass1X);
        control.setValue("X of mass2", mass2X);
        control.setValue("X of mass3", mass3X);

        control.setValue("Y of mass1", mass1Y);
        control.setValue("Y of mass2", mass2Y);
        control.setValue("Y of mass3", mass3Y);

        control.setValue("vX of mass1", mass1VX);
        control.setValue("vX of mass2", mass2VX);
        control.setValue("vX of mass3", mass3VX);

        control.setValue("vY of mass1", mass1VY);
        control.setValue("vY of mass2", mass2VY);
        control.setValue("vY of mass3", mass3VY);

        // Reset the mass values.
        mass1 = new Mass(151.86E9, 0, 0, 30000, 0, 0, 5.972E24);
        mass2 = new Mass(0, 0, 0, 0, 0, 0, 1.989E30);
        mass3 = new Mass(100E9, 0, 0, -36524, 0, 0, 6E24);

        // Remove the trails:
        for(Trail trail: trails){
            orbitalPlotFrame.removeDrawable(trail);
        }

        // Remove masses from the plotframe:
        for (Circle circle: massCircles) {
            orbitalPlotFrame.removeDrawable(circle);
        }
    }

    /**
     * Technically optional, but the simulation won't work without it.
     * Tied to the "Initialize" button. Gets information from the Control Panel and
     * configures the plot frame.
     */
    @Override
    public void initialize() {
        // Get control values:
        deltaTime = control.getDouble("Duration of timeStep");

        massMass1 = control.getDouble("Mass of mass1");
        massMass2 = control.getDouble("Mass of mass2");
        massMass3 = control.getDouble("Mass of mass3");

        mass1X = control.getDouble("X of mass1");
        mass2X = control.getDouble("X of mass2");
        mass3X = control.getDouble("X of mass3");

        mass1Y = control.getDouble("Y of mass1");
        mass2Y = control.getDouble("Y of mass2");
        mass3Y = control.getDouble("Y of mass3");

        mass1VX = control.getDouble("vX of mass1");
        mass2VX = control.getDouble("vX of mass2");
        mass3VX = control.getDouble("vX of mass3");

        mass1VY = control.getDouble("vY of mass1");
        mass2VY = control.getDouble("vY of mass2");
        mass3VY = control.getDouble("vY of mass3");;

        mass1 = new Mass(mass1X, mass1Y, mass1VX, mass1VY, 0, 0, massMass1);
        mass2 = new Mass(mass2X, mass2Y, mass2VX, mass2VY, 0, 0, massMass2);
        mass3 = new Mass(mass3X, mass3Y, mass3VX, mass3VY, 0, 0, massMass3);

        // Get information from the control panel.
        mass1Circle.setY(mass1.getY());
        mass1Circle.setX(mass1.getX());
        mass1Circle.color = Color.RED;


        // Get information from the control panel.
        mass2Circle.setY(mass2.getY());
        mass2Circle.setX(mass2.getX());
        Color niceGreen = new Color(3, 131, 0);
        mass2Circle.color = niceGreen;

        // Get information from the control panel.
        mass3Circle.setY(mass3.getY());
        mass3Circle.setX(mass3.getX());
        mass3Circle.color = Color.BLUE;

        // Instead of appending x, y coordinates to plot frame, add the Circle which maintains its own x, y.
        for(Circle circle: massCircles){
            orbitalPlotFrame.addDrawable(circle);
        }

        // Plot the first trail points:
        mass1Trail = new Trail();
        mass2Trail = new Trail();
        mass3Trail = new Trail();
        trails = new Trail[]{mass1Trail, mass2Trail, mass3Trail};

        // Configure plot frame
        orbitalPlotFrame.setPreferredMinMax(-200E9, 200E9, -200E9, 200E9); // Scale of graph.
        orbitalPlotFrame.setLocation(0,0); // Set where the plot frame will appear on the screen.
        orbitalPlotFrame.setSize(1000, 1000);
        orbitalPlotFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Make it so x'ing out of the graph stops the program.
        orbitalPlotFrame.setVisible(true); // Required to show plot frame.
        masses = new Mass[]{mass1, mass2, mass3};
    }

    /**
     * Required method, invoked once every 1/10 second until Stop is pressed.
     * The doStep method is also called when the Step button is pressed.
     */
    public void doStep() {
        // Update the circles:
        for (int i = 0; i < masses.length; i++) {
            massCircles[i].setX(masses[i].getX());
            massCircles[i].setY(masses[i].getY());
        }

        // Update the values:
        step(masses);

        // Update the position and velocity of each mass:
        for (Mass mass: masses) {
            // Displacement equations:
            mass.setX(mass.getX() + (mass.getXV() * deltaTime) + ((mass.getXAcceleration() / 2) * (Math.pow(deltaTime, 2))));
            mass.setY(mass.getY() + (mass.getYV() * deltaTime) + ((mass.getYAcceleration() / 2) * (Math.pow(deltaTime, 2))));

            // Velocity equations:
            mass.setXV(mass.getXV() + (mass.getXAcceleration() * deltaTime));
            mass.setYV(mass.getYV() + (mass.getYAcceleration() * deltaTime));
        }

        // Plot trail points:
        int massCounter = 0;
        for(Trail trail: trails) {
            trail.addPoint(masses[massCounter].getX(), masses[massCounter].getY());
            orbitalPlotFrame.addDrawable(trail);
            massCounter++;
        }
    }

    public void step(Mass[] masses){

        // Iterate through the masses and update the accelerations:
        for (Mass mass: masses) {
            // The net acceleration that the mass is experience in the X and Y direction:
            double aNetX = 0;
            double aNetY = 0;

            // Iterate through every mass and find the acceleration that its force exerts on the mass of current focus:
            for (int i = 0; i < masses.length; i++) {
                if(mass != masses[i]) {
                    // Get the acceleration that the gravitational force vectors would cause for the force that the mass is exerting on the current mass of focus:
                    double distance = Math.sqrt(((masses[i].getX() - mass.getX()) * (masses[i].getX() - mass.getX())) + ((masses[i].getY() - mass.getY()) * (masses[i].getY() - mass.getY())));
                    double fG = (G * mass.getMass() * masses[i].getMass()) / (distance * distance);
                    double theta = 0;
                    if(mass.getX() < masses[i].getX()){
                        theta = Math.atan((masses[i].getY() - mass.getY()) / (masses[i].getX() - mass.getX())) + Math.PI;
                    }
                    else {
                        theta = Math.atan((masses[i].getY() - mass.getY()) / (masses[i].getX() - mass.getX()));
                    }

                    double aVectorX = 0;
                    aVectorX = -(fG / mass.getMass()) * Math.cos(theta);
                    double aVectorY = -(fG / mass.getMass()) * Math.sin(theta);

//                    if(mass.getX() > masses[i].getX()) {
//                        aVectorX = -(fG / mass.getMass()) * Math.cos(theta);
//                    }else{
//                        aVectorX = (fG / mass.getMass()) * Math.cos(theta);
//                    }
//                    double aVectorY = 0;
//                    if(mass.getY() > masses[i].getY()){
//                        aVectorY = -(fG / mass.getMass()) * Math.sin(theta);
//                    }else {
//                        aVectorY = (fG / mass.getMass()) * Math.sin(theta);
//                    }

//                    double xNumerator = G * masses[i].getMass();
//                    double xDenominator = (mass.getX() - masses[i].getX()) * (mass.getX() - masses[i].getX());
//                    double yNumerator = G * masses[i].getMass();
//                    double yDenominator = (masses[i].getX() - masses[i].getX()) * (masses[i].getX() - masses[i].getX());
//                    double aVectorX = xNumerator / xDenominator;
//                    double aVectorY = yNumerator / yDenominator;

                    // Update the net force vectors:
                    aNetX = aNetX + aVectorX;
                    aNetY = aNetY + aVectorY;
                }
            }

            // Set the accelerations equal to the previous calculated net acceleration:
            mass.setXAcceleration(aNetX);
            mass.setYAcceleration(aNetY);
        }
    }

    /**
     * Required main method, runs the simulation.
     * @param args - required argument component
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new OrbitalSimulationApp());
    }
}
