package com.adamboesky.matrix;

import org.dalton.polyfun.Polynomial;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixApp {
    public static void main(String[] args) {
        System.out.println("If you want to invert a matrix enter \"0\". If you want to differentiate using the VDM enter \"1\". If you want to balance a chemical equation enter \"2\".");
        Scanner scan = new Scanner(System.in);
        int program = scan.nextInt();
        scan.nextLine();

        if(program == 0){
            // Create and invert a matrix:
            Matrix matrix = new Matrix(4, 4);
            matrix.setEntry(0, 0, 1);
            matrix.setEntry(0, 1, 4);
            matrix.setEntry(0, 2, -2);
            matrix.setEntry(0, 3, 5);
            matrix.setEntry(1, 0, -12);
            matrix.setEntry(1, 1, -1);
            matrix.setEntry(1, 2, 2);
            matrix.setEntry(1, 3, 0);
            matrix.setEntry(2, 0, 1);
            matrix.setEntry(2, 1, -3);
            matrix.setEntry(2, 2, 9);
            matrix.setEntry(2, 3, -4);
            matrix.setEntry(3, 0, 0);
            matrix.setEntry(3, 1, -6);
            matrix.setEntry(3, 2, 1);
            matrix.setEntry(3, 3, 4);

            Matrix inverse = matrix.invert();

            System.out.println("The matrix: \n" + matrix);
            System.out.println("\n The inverse matrix: \n" + inverse);
            System.out.println("\n The matrix multiplied by its inverse: \n" + inverse.times(matrix).roundAll(8));
        }
        else if(program == 1) {
            // DIFFERENTIATE USING THE VERTICAL DIFFERENCE METHOD:
            // Take in polynomial and x from thef user:
            System.out.println("Enter the degree of the polynomial that you would like to differentiate:");
            int degree = scan.nextInt();

            Polynomial poly = new Polynomial(degree + 1);
            double[] coefs = new double[poly.getDegree() + 1];

            for (int i = 0; i < poly.getDegree(); i++) {
                System.out.println("Enter the coefficient for the x^" + i + " term");
                coefs[i] = scan.nextDouble();
            }

            poly.setCoefs(coefs);

            System.out.println("Your polynonmial is :\n" + poly);
            System.out.println("Enter the x value for which you would like to find the derivative:");

            double a = scan.nextDouble();

            // Create a matrix array that is the comparison of coefficients. The second matrix will be the known
            // coefficients and the first will be the comparison:
            Matrix[] comparison = polyToMatrix(poly, a);

            // Invert the comparison matrix:
            Matrix invertedComparison = comparison[0].invert();

            // Multiply the inverse of the comparison by the coefficients matrix:
            Matrix derivativeMatrix = invertedComparison.times(comparison[1]);

            // Print the derivative of the function at x = a:
            System.out.println("f'(" + a + ") = " + derivativeMatrix.getEntry(1, 0));
        }
        else if(program == 2) {
            // BALANCE A CHEMICAL EQUATION:
            // Request and take in an equation from the user:
            System.out.println("Please eneter the chemcial equation. The equation must have one of every molecule and" +
                    " it must have \" + \" in between the molecules on each side of the euqation. It must \nhave \"" +
                    " -> \" to denote the change from reactants to products. Also, do not include the parenthesis in" +
                    "things line \"(OH)\", rather, distribute the number of the group to each element in the molecule");
            String unbalancedEquation = scan.nextLine();
            String balancedEquation = balance(unbalancedEquation);
            System.out.println(balancedEquation);
        }
    }


    /**
     * This method converts a polynomial to a matrix. It does so by following a pattern in the mathematical expanision
     * of (x - a)^2(q(x)). This will be used when differentiating polynomials.
     *
     * @param polynomial - the polynomial that the matrix will be compared to
     * @param a - the x value at which the user seeks the slope of the tangent line
     * @return - An array of matrices of length 2. The first matrix will be the comparison and the second will be the
     * coefficients matrix.
     */
    public static Matrix[] polyToMatrix(Polynomial polynomial, double a){
        // Make a matrix that we will fill in
        Matrix polyMatrix = new Matrix(polynomial.getDegree() + 1, polynomial.getDegree() + 1);

        // Fill the matrix according to the pattern found when expanding the comparison:
        for (int i = 0; i < polyMatrix.getRows(); i++) {
            polyMatrix.setEntry(i, i, 1);
        }

        for (int i = 0; i < polyMatrix.getRows() - 1; i++) {
            polyMatrix.setEntry(i, i + 1, -2 * a);
        }

        for (int i = 0; i < polyMatrix.getRows() - 2; i++) {
            polyMatrix.setEntry(i, i + 2, Math.pow(a, 2));
        }

        polyMatrix.setEntry(0, 1, 0);

        // Make a matrix of the coefficients of the polynomial:
        Matrix coefficientMatrix = new Matrix(polynomial.getDegree() + 1, 1);
        for (int i = 0; i < coefficientMatrix.getRows(); i++) {
            coefficientMatrix.setEntry(i, 0, polynomial.getCoefficientAtTerm(i));
        }

        // Format the two matrices into one output:
        Matrix[] output = new Matrix[]{polyMatrix, coefficientMatrix};

        return output;
    }


    /**
     * A method that balances a chemical equation. It is given an equation with one of all compounds in the form of a
     * String and then will return the balanced equation in the form of a string.
     *
     * @param equationString - the unbalanced chemical equation
     * @return - A balanced chemicalEquation.
     */
    public static String balance(String equationString){
        String[] splitEquation = equationString.split(" -> ");
        String[] reactants = splitEquation[0].split(" \\+ ");
        String[] products = splitEquation[1].split(" \\+ ");
        List<String> elements = new ArrayList<>();

        // Create list of elements in the equation:
        for (int i = 0; i < reactants.length; i++) {
            for (int j = 0; j < reactants[i].length(); j++) {
                if(Character.isUpperCase(reactants[i].charAt(j)) == true){
                    if(j + 1 < reactants[i].length() && Character.isLowerCase(reactants[i].charAt(j + 1))){
                        if(!elements.contains(reactants[i].substring(j, j + 2))) {
                            elements.add(reactants[i].substring(j, j + 2));
                        }
                    }
                    else{
                        if(!elements.contains(reactants[i].substring(j, j + 1))) {
                            elements.add(reactants[i].substring(j, j + 1));
                        }
                    }
                }
            }
        }

        String[] elementsArray = new String[elements.size()];
        int helper = 0;
        for(String element:elements){
            elementsArray[helper] = element;
            helper++;
        }

        // Create matrix with the number of columns as the number of reactants added to the number of products and the
        // number of rows as the number of elements:
        Matrix equationMatrix = new Matrix(elements.size(), reactants.length + products.length);

        // Find the number of elements in each reactant:
        String[] temporaryReactants = new String[reactants.length];
        for (int i = 0; i < reactants.length; i++) {
            int numRepeats = 0;
            for(int k = 0; k < elementsArray.length; k++){

                if(numRepeats == 0){
                    int count = 0;
                    for(String reactant: reactants){ // Here we replicate reactants so that we can edit each reactant without ruining the existing strings
                        temporaryReactants[count] = reactant;
                        count++;
                    }
                }

                int numberOfElement = 0;
                if(temporaryReactants[i].indexOf(elementsArray[k]) != -1){
                    boolean searchingForNumber = true;

                    // Make sure that one the compound does have an element like Br and we are looking for B and getting confused:
                    if(temporaryReactants[i].indexOf(elementsArray[k]) + elementsArray[k].length() < temporaryReactants[i].length() && Character.isLowerCase(temporaryReactants[i].charAt(temporaryReactants[i].indexOf(elementsArray[k]) + elementsArray[k].length()))){
                        searchingForNumber = false;
                    }
                    int index = temporaryReactants[i].indexOf(elementsArray[k]);
                    // Find number of element in the reactant:
                    int counter = 1;
                    // Look for a number following the element (the number can be of any length):
                    while(searchingForNumber){
                        if(index + elementsArray[k].length() + counter <= temporaryReactants[i].length() && isNumeric(temporaryReactants[i].substring(index + elementsArray[k].length(), index + elementsArray[k].length() + counter))){
                            numberOfElement = Integer.parseInt(temporaryReactants[i].substring(index + elementsArray[k].length(), index + elementsArray[k].length() + counter));
                        }
                        else if(numberOfElement > 1){
                            searchingForNumber = false;
                        }
                        else{
                            numberOfElement = 1;
                            searchingForNumber = false;
                        }

                        counter++;
                    }
                    // Enter the number of the element in the reactant into the matrix:
                    equationMatrix.setEntry(k, i, equationMatrix.getEntry(k, i) + numberOfElement);

                    // Cut out the element from the molecule and recheck for more of the same element:
                    temporaryReactants[i] = temporaryReactants[i].substring(0, temporaryReactants[i].indexOf(elementsArray[k])) + temporaryReactants[i].substring(temporaryReactants[i].indexOf(elementsArray[k]) + 1);
                    k--;
                    numRepeats++;
                } else{
                    numRepeats = 0;
                }
            }
        }

        // Find the number of elements in each product:
        String[] temporaryProducts = new String[products.length];
        for (int i = 0; i < products.length; i++) {
            int numRepeats = 0;
            for(int k = 0; k < elementsArray.length; k++){

                if(numRepeats == 0){ // If this is the first run through for this element
                    int count = 0;
                    for(String product: products){ // Here we replicate products so that we can edit each product without ruining the existing strings
                        temporaryProducts[count] = product;
                        count++;
                    }
                }

                int numberOfElement = 0;
                if(temporaryProducts[i].indexOf(elementsArray[k]) != -1){
                    int index = temporaryProducts[i].indexOf(elementsArray[k]);
                    // Find number of element in the product:
                    boolean searchingForNumber = true;

                    // Make sure that one the compound does have an element like Br and we are looking for B and getting confused:
                    if(temporaryProducts[i].indexOf(elementsArray[k]) + elementsArray[k].length() < temporaryProducts[i].length() && Character.isLowerCase(temporaryProducts[i].charAt(temporaryProducts[i].indexOf(elementsArray[k]) + elementsArray[k].length()))){
                        searchingForNumber = false;
                    }
                    int counter = 1;
                    // Look for a number following the element (the number can be of any length):
                    while(searchingForNumber){
                        if(index + elementsArray[k].length() + counter <= temporaryProducts[i].length() && isNumeric(temporaryProducts[i].substring(index + elementsArray[k].length(), index + elementsArray[k].length() + counter))){
                            numberOfElement = Integer.parseInt(temporaryProducts[i].substring(index + elementsArray[k].length(), index + elementsArray[k].length() + counter));
                        }
                        else if(numberOfElement > 1){
                            searchingForNumber = false;
                        }
                        else{
                            numberOfElement = 1;
                            searchingForNumber = false;
                        }

                        counter++;
                    }
                    // Enter the number of the element in the product into the matrix:
                    equationMatrix.setEntry(k, i + reactants.length, equationMatrix.getEntry(k, i + reactants.length) - numberOfElement);

                    // Cut out the element from the molecule and recheck for more of the same element:
                    temporaryProducts[i] = temporaryProducts[i].substring(0, temporaryProducts[i].indexOf(elementsArray[k])) + temporaryProducts[i].substring(temporaryProducts[i].indexOf(elementsArray[k]) + 1);
                    k--;
                    numRepeats++;
                } else{
                    numRepeats = 0;
                }
            }
        }

        // Row reduce the matrix that represents the equation:
        Matrix reducedEquationMatrix = equationMatrix.rowReduce();

        // Find an integer that when multiplied by the entries in the last column will cause them to all be whole numbers:
        boolean allWhole = false;
        int rowCounter = 0;
        int multiplier = 1;
        while(!allWhole){
            double checker = round(reducedEquationMatrix.getEntry(rowCounter, reducedEquationMatrix.getColumns() - 1) * multiplier, 5) % 1;
            if(round(reducedEquationMatrix.getEntry(rowCounter, reducedEquationMatrix.getColumns() - 1) * multiplier, 5) % 1 == 0){
                rowCounter++;
            }
            else{
                rowCounter = 0;
                multiplier++;
            }

            if(rowCounter == reducedEquationMatrix.getRows()){
                allWhole = true;
            }
        }

        // Multiply all of the rows by the scalar that was found above:
        for (int i = 0; i < reducedEquationMatrix.getRows(); i++) {
            reducedEquationMatrix = reducedEquationMatrix.scalarTimesRow(multiplier, i);
        }

        // Rename the matrix to keep track of where we are in the process and create an empty sting that we will add to and finally return:
        Matrix balancedEquationMatrix = reducedEquationMatrix;
        String balancedEquationString = "";

        // Make an array of all the compounds in the matrix:
        String[] compounds = new String[reactants.length + products.length];
        for (int i = 0; i < compounds.length; i++) {
            if(i < reactants.length){
                compounds[i] = reactants[i];
            }
            else{
                compounds[i] = products[i - reactants.length];
            }
        }

        // Build up the equation by adding the compound and its number:
        for (int i = 0; i < compounds.length; i++) {
            if(i == reactants.length - 1) { // If this is the last reactant and we must transition to products
                int moleculeNumber = (int) round((-1 * balancedEquationMatrix.getEntry(i, reducedEquationMatrix.getColumns() - 1)) , 4);
                if(moleculeNumber == 1){
                    balancedEquationString = balancedEquationString + compounds[i] +  " -> ";
                }else {
                    balancedEquationString = balancedEquationString + (int) round((-1 * balancedEquationMatrix.getEntry(i, reducedEquationMatrix.getColumns() - 1)), 4) + compounds[i] + " -> ";
                }
            }
            else if(balancedEquationMatrix.getColumns() > balancedEquationMatrix.getRows() && i == balancedEquationMatrix.getRows() - 1){ // If the matrix has more columns than rows and we are on the last row
                int moleculeNumber = (int) round((-1 * balancedEquationMatrix.getEntry(i, reducedEquationMatrix.getColumns() - 1)), 4);
                if(moleculeNumber == 1){ // These extra if statements are just a formatting component to ensure that the output doesn't have a '1' in front of all single molecules
                    balancedEquationString = balancedEquationString + compounds[i] + " + ";
                }else {
                    balancedEquationString = balancedEquationString + (int) round((-1 * balancedEquationMatrix.getEntry(i, reducedEquationMatrix.getColumns() - 1)), 4) + compounds[i] + " + ";
                }
            }
            else if(i == balancedEquationMatrix.getRows() - 1 || (balancedEquationMatrix.getColumns() > balancedEquationMatrix.getRows() && i == compounds.length - 1)){ // If this is the final thing to add onto the string (this one checks if it is for matrices with columns > rows)
                if(multiplier == 1){
                    balancedEquationString = balancedEquationString + compounds[i];
                }else {
                    balancedEquationString = balancedEquationString + multiplier + compounds[i];
                }
            }
            else if(i == balancedEquationMatrix.getColumns() - 1 || (balancedEquationMatrix.getColumns() < balancedEquationMatrix.getRows() && i == compounds.length - 1)){ // If this is the final thing to add onto the string (this one checks if it is for matrices with columns < rows)
                if(multiplier == 1){
                    balancedEquationString = balancedEquationString + compounds[i];
                }else {
                    balancedEquationString = balancedEquationString + multiplier + compounds[i];
                }
            }
            else{
                if((int) round((-1 * balancedEquationMatrix.getEntry(i, reducedEquationMatrix.getColumns() - 1)), 4) == 1){
                    balancedEquationString = balancedEquationString + compounds[i] + " + ";
                }else {
                    balancedEquationString = balancedEquationString + (int) round((-1 * balancedEquationMatrix.getEntry(i, reducedEquationMatrix.getColumns() - 1)), 4) + compounds[i] + " + ";
                }
            }
        }
        return balancedEquationString;
    }


    /**
     * A helper method that will allow us to round a double to a desired decimal place.
     *
     * @param value - the value that will be rounded
     * @param places - the number of desired decimal places
     * @return - A number that rounded to the desired decimal place
     */
    private static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    /**
     * A method that checks if a String is a number or not. It will return true if so and false if not.
     *
     * @param strNum - The string that we are checking
     * @return - Whether the String is a number or not.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
