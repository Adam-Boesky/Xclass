package com.adamboesky;

public class Quadratic {
    private double a;
    private double b;
    private double c;

    //constructor
    public Quadratic(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    //gets A term
    public double getA() {
        return a;
    }

    //gets B term
    public double getB() {
        return b;
    }

    //gets C term
    public double getC(){
        return c;
    }

    //checks if the quadratic has real roots
    public boolean hasRealRoots(){
        if((b * b - 4 * a * c) > 0){
            return true;
        }
        else {
            return false;
        }
    }

    //returns the number of roots
    public int numberOfRoots(){
        if(b * b - 4 * a * c == 0) {
            return 1;
        }
        if(b * b - 4 * a * c > 0) {
            return 2;
        }
        else {
            return 0;
        }
    }

    //returns the roots of a quadratic
    public double[] getRootArray(){
        double[] roots = new double[2];
        roots[0] = (-b + Math.sqrt(b * b - 4 * a * c))/2 * a;
        roots[1] = (-b - Math.sqrt(b * b - 4 * a * c))/2 * a;
        return roots;
    }

    //returns axis of symmetry
    public double getAxisOfSymmetry(){
        return (-b /( 2 * a));
    }

    //returns min or max
    public double getExtremeValue(){
        double aos = (-b/(2 * a));
        return ((a * Math.pow(aos, 2)) + (b * aos) + c);
    }

    //returns whether it is a min or max number
    public boolean isMax(){
        if (a > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public double evaluateWith(double x){
        return (a * Math.pow(x, 2) + (b * x) + c);
    }

}
