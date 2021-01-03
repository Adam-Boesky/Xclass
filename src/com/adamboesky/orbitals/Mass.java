package com.adamboesky.orbitals;

public class Mass {
    private double steps = 0;
    private double x;
    private double y;
    private double xV;
    private double yV;
    private double mass;
    private double xAcceleration;
    private double yAcceleration;

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
    Mass(double x, double y, double xV, double yV, double xAcceleration, double yAcceleration, double mass) {
        this.x = x;
        this.y = y;
        this.xV = xV;
        this.yV = yV;
        this.mass = mass;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
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
        steps++;
    }
}
