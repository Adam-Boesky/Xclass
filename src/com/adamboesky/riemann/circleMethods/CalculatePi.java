package com.adamboesky.riemann.circleMethods;

public class CalculatePi {
    /**
     * Returns the value of pi based off of the area calculate with a Rieamnn sum and radius of any given semi circle
     *
     * @param area - the area of the semi circle
     * @param radius - the radius of the semi circle
     * @return - The value of pi as calculate by the function
     */
    public static double calcPi(double area, double radius){
        return (area * 2)/ Math.pow(radius, 2);
    }
}
