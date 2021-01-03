package com.adamboesky.projectile;

public class Particle {
    private double times = 0;
    private double x;
    private double y;
    private double xV;
    private double yV;
    private double mass;
    private double xAcceleration;
    private double yAcceleration;
    private boolean isStopped;

    // Getters:
    /**
     * Get the X location of the particle.
     *
     * @return X location of the particle.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the Y location of the particle.
     *
     * @return Y location of the particle.
     */
    double getY() {
        return y;
    }

    /**
     * Get the mass of the particle.
     *
     * @return Mass of the particle.
     */
    double getMass() {
        return mass;
    }

    /**
     * Get the horizontal velocity of the particle.
     *
     * @return Horizontal velocity of the particle.
     */
    double getXV() {
        return xV;
    }

    /**
     * Get the vertical velocity of the particle.
     *
     * @return Vertical velocity of the particle.
     */
    double getYV() {
        return yV;
    }

    /**
     * Get the horizontal acceleration of the particle.
     *
     * @return The horizontal acceleration of the particle.
     */
    double getXAcceleration() {
        return xAcceleration;
    }

    /**
     * Get the vertical acceleration of the particle.
     *
     * @return The vertical acceleration of the particle.
     */
    double getYAcceleration() {
        return yAcceleration;
    }

    //Setters:
    /**
     * Set the X location of the particle.
     *
     * @param x - the desired X location
     */
    void setX(double x) {
        this.x = x;
    }

    /**
     * Set the Y location of the particle.
     *
     * @param y - the desired Y location
     */
    void setY(double y) {
        this.y = y;
    }

    /**
     * Set the mass of the particle.
     *
     * @param mass - the desired mass of the particle.
     */
    void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Set the horizontal velocity of the particle.
     *
     * @param xV - the desired horizontal velocity of the particle.
     */
    void setXV(double xV) {
        this.xV = xV;
    }

    /**
     * Set the vertical velocity of the particle.
     *
     * @param yV - the desired vertical velocity of the particle.
     */
    void setYV(double yV) {
        this.yV = yV;
    }

    /**
     * Set the horizontal acceleration of the particle.
     *
     * @param xAcceleration - the desired horizontal acceleration.
     */
    void setXAcceleration(double xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    /**
     * Set the vertical acceleration of the particle.
     *
     * @param yAcceleration - the desired vertical acceleration.
     */
    void setYAcceleration(double yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    /**
     * Set the boolean that denotes whether a particle is stopped or not true.
     *
     */
    public void setStopped() {
        isStopped = true;
    }

    /**
     * Get if the particle is stopped or not.
     *
     * @return Whether the particle is stopped or not
     */
    public boolean getIsStopped() {
        return isStopped;
    }

    /**
     * A constructor method.
     *
     * @param x - the initial X value for the particle
     * @param y - the initial Y value for the particle
     * @param xV - the initial X velocity for the particle
     * @param yV - the initial Y velocity for the particle
     * @param xAcceleration - the initial X acceleration for the particle
     * @param yAcceleration - the initial Y acceleration for the particle
     * @param mass - the mass of the particle
     */
    Particle(double x, double y, double xV, double yV, double xAcceleration, double yAcceleration, double mass, boolean
             isStopped) {
        this.x = x;
        this.y = y;
        this.xV = xV;
        this.yV = yV;
        this.mass = mass;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.isStopped = isStopped;
    }

    /**
     * A method that will be called in the app doStep method. This will update the values of a particle with no
     * resistance given an acceleration of gravity and a time step.
     *
     * @param deltaTime - the given time step
     * @param gravity - the acceleration of gravity
     */
    void step(double deltaTime, double gravity) {

        yAcceleration = -gravity;

        // Displacement equations:
        x = x + (xV * deltaTime) + ((xAcceleration / 2) * (Math.pow(deltaTime, 2)));
        y = y + (yV * deltaTime) + ((yAcceleration / 2) * (Math.pow(deltaTime, 2)));

        // Velocity equations:
        xV = xV + (xAcceleration * deltaTime);
        yV = yV + (yAcceleration * deltaTime);
        times++;
    }

    /**
     * A method that uses the coefficient of restitution to "bounce" the basketball. It does so by using the velocities
     * upon collision, the coordinates of the object that it collides with, and the coefficient of restitution to update
     * the new velocities.
     *
     * @param coefficientRestitution - the coefficient of restitution of the particle
     * @param xCoordinate - the x coordinate of the object that it is colliding with
     * @param yCoordinate - the y coordinate of the object that it is colliding with
     */
    void bounce(double coefficientRestitution, double xCoordinate, double yCoordinate) {
        // Calculate the angle from the center of the particle to the point of contact:
        double bounceAngle = Math.atan(Math.abs(y - yCoordinate) / Math.abs(x - xCoordinate));

        // Use the known velocities, coordinates of the point of collision, and coefficient of restitution to find the
        // x and y value in a new reference frame. This reference frame will have a y-axis that goes through both the
        // center of the ball and the point of collision. The x-axis is perpendicular to the y-axis.
        double referenceFrameXV = coefficientRestitution * ( (xV * Math.sin(bounceAngle)) +
                (yV * Math.sin((Math.PI / 2) - bounceAngle)) );
        double referenceFrameYV = -coefficientRestitution * ( (xV * Math.cos(bounceAngle)) +
                (yV * Math.cos((Math.PI / 2) - bounceAngle)) );

        xV = (referenceFrameXV * Math.cos((Math.PI / 2) - bounceAngle)) + (referenceFrameYV * Math.cos(bounceAngle));
        yV = (referenceFrameXV * Math.sin((Math.PI / 2) - bounceAngle)) + (referenceFrameYV * Math.sin(bounceAngle));
    }

    /**
     * A method that will be called in the app doStep method. This will update the values of a particle wit resistance
     * calculated in proportion to V given an acceleration of gravity and a time step.
     *
     * @param deltaTime - hte given time step
     * @param mass - the mass of the particle
     * @param beta - the drag coefficient
     * @param gravity - the acceleration of gravity
     */
    void stepResistanceProp(double deltaTime, double mass, double beta, double gravity) {

        // Variables that hold the former accelerations to make it easy to calculate the acceleration Riemann sum using
        // the trapezoid rule:
        double xAccelerationHolder = xAcceleration;
        double yAccelerationHolder = yAcceleration;

        // Calculate weights and resistances:
        double resistanceX = beta * xV;
        double resistanceY = beta * yV;
        double weightY = mass * -gravity;

        // Calculate acceleration while taking resistance into account and make sure that the signs are correct:
        // X:
            xAcceleration = -resistanceX / mass;
        // Y:
        if(weightY > 0) {
            yAcceleration = (weightY + resistanceY) / mass;
        }
        else{
            yAcceleration = (weightY - resistanceY) / mass;
        }

        // Make sure the initial velocity is going to be based off of the acceleration of gravity:
        if (times == 0) {
            yAccelerationHolder = -gravity;
        }
        if (times == 0) {
            xAccelerationHolder = xAcceleration;
        }
        double xVHolder = xV;
        double yVHolder = yV;

        // Calculate the velocity based off of acceleration:
        xV = xV + (deltaTime * ((xAcceleration + xAccelerationHolder) / 2));
        yV = yV + (deltaTime * ((yAcceleration + yAccelerationHolder) / 2));

        // Displacement equations:
        x = x + (((xVHolder + xV) / 2) * deltaTime);
        y = y + (((yVHolder + yV) / 2) * deltaTime);
        times++;
    }

    /**
     * A method that will be called in the app doStep method. This will update the values of a particle wit resistance
     * calculated in proportion to V given an acceleration of gravity and a time step.
     *
     * @param deltaTime - the given time step
     * @param mass - the mass of the particle
     * @param beta - the drag coefficient
     * @param gravity - the acceleration of gravity
     */
    void stepResistancePropSquared(double deltaTime, double mass, double beta, double gravity) {

        // Variables that hold the former accelerations to make it easy to calculate the acceleration Riemann sum using
        // the trapezoid rule:
        double xAccelerationHolder = xAcceleration;
        double yAccelerationHolder = yAcceleration;

        // Calculate weights and resistances:
        double resistanceX = beta * (xV * Math.abs(xV));
        double resistanceY = beta * (yV * Math.abs(yV));
        double weightY = mass * -gravity;

        // Calculate acceleration while taking resistance into account and make sure that the signs are correct:
        // X:
        xAcceleration = -resistanceX / mass;
        // Y:
        yAcceleration = (weightY - resistanceY) / mass;

        // Make sure the initial velocity is going to be based off of the acceleration of gravity:
        if(weightY > 0) {
            yAcceleration = (weightY + resistanceY) / mass;
        }
        else{
            yAcceleration = (weightY - resistanceY) / mass;
        }
        double xVHolder = xV;
        double yVHolder = yV;

        // Calculate the velocity based off of acceleration:
        xV = xV + (deltaTime * ((xAcceleration + xAccelerationHolder) / 2));
        yV = yV + (deltaTime * ((yAcceleration + yAccelerationHolder) / 2));

        // Displacement equations:
        x = x + (((xVHolder + xV) / 2) * deltaTime);
        y = y + (((yVHolder + yV) / 2) * deltaTime);
        times++;
    }

    /**
     * A method that will stop the particle in place and not allow it to move at all. It does so by setting the
     * accelerations and velocities to 0;
     */
    void stop(){
        xV = 0;
        yV = 0;
        xAcceleration = 0;
        yAcceleration = 0;
    }
}
