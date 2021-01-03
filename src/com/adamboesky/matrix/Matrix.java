package com.adamboesky.matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Matrix {
    private int rows;
    private int columns;
    private double[][] matrix;
    private boolean[][] isPivot;

    /**
     * A constructor method
     *
     * @param rows - the number of rows in the matrix
     * @param columns - the number of columns in this matrix
     */
    public Matrix(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.matrix = new double[rows][columns];
        this.isPivot = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                isPivot[i][j] = false;
            }
        }
    }

    /**
     * A constructor method
     */
    public Matrix(){
        this.rows = 1;
        this.columns = 1;
        this.matrix = new double[rows][columns];
        this.matrix = new double[1][1];
        this.isPivot = new boolean[1][1];
        this.matrix[0][0] = 0;
        this.isPivot[0][0] = false;
    }

    /**
     * Get the number of rows of the matrix.
     *
     * @return - the number of rows of a given matrix.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the number of columns of the matrix.
     *
     * @return - the number of columns of a given matrix.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the value at a specific spot in the matrix.
     *
     * @param rowNumber - the row number of the value that the user desires to retrieve.
     * @param columnNumber - the column number of the value that the user desires to retrieve.
     * @return - the value at a specific spot in the matrix.
     */
    public double getEntry(int rowNumber, int columnNumber){
        return this.matrix[rowNumber][columnNumber];
    }

    /**
     * Returns a Matrix that has multiplied a scalar by a row of this Matrix.
     *
     * @param scalar - number by which each entry in the specified row will be multiplied.
     * @param rowNumber - row being multiplied by a scalar.
     * @return - Matrix with the modified row.
     */
    public Matrix scalarTimesRow(double scalar, int rowNumber){
        Matrix outputMatrix = this.replicate();

        for (int i = 0; i < this.columns; i++) {
            outputMatrix.setEntry(rowNumber, i, this.getEntry(rowNumber, i) * scalar);
        }

        return outputMatrix;
    }

    /**
     * A helper method that will allow us to round the entries to a desired decimal place.
     *
     * @param value - the value that will be rounded
     * @param places - the number of desired decimal places
     * @return - A number that rounded to the desired decimal place
     */
    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * A method that rounds all of the entries in the matrix to the desired decimal place.
     *
     * @param precision - the desired number of decimal places.
     * @return - A matrix with all of its entries rounded to the desired decimal place.
     */
    public Matrix roundAll(int precision){
        Matrix outputMatrix = this.replicate();

        // Iterate through the matrix rounding each number to the tenth decimal place
        for (int i = 0; i < outputMatrix.getRows(); i++) {
            for (int j = 0; j < outputMatrix.getColumns(); j++) {
                outputMatrix.setEntry(i, j, round(outputMatrix.getEntry(i, j), precision));
            }
        }

        return outputMatrix;
    }

    /**
     * Returns a matrix that has added a scalar multiple of the first row to the second row of this Matrix.
     *
     * @param scalar - number by which each entry in the first row will be multiplied.
     * @param firstRow - row being multiplied by a scalar and added to the second row.
     * @param secondRow - row to which the first row is added.
     * @return - Matrix with the modified secondRow.
     */
    public Matrix linearComboRows​(double scalar, int firstRow, int secondRow) {
        Matrix outputMatrix = this.replicate();
        for (int i = 0; i < outputMatrix.getColumns(); i++) {
            outputMatrix.setEntry(secondRow, i, (outputMatrix.getEntry(secondRow, i) + (scalar * outputMatrix.getEntry(firstRow, i))));
        }

        return outputMatrix;
    }

    /**
     * A method that converts the matrix to a string.
     *
     * @return - The matrix in a string format.
     */
    public String toString(){
        String output = new String();
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                if(j == this.columns - 1 && i != this.rows - 1) {
                    output = output + this.getEntry(i, j) + "\n";
                } else {
                    output = output + this.getEntry(i, j) + "   ";
                }
            }
        }

        return output;
    }

    /**
     * Sets the value at a specific spot in this Matrix.
     *
     * @param rowNumber - the row number of the value that is to be set.
     * @param columnNumber - the column number of the value that is to be set.
     * @param value - the value of the place that is to be set.
     */
    public void setEntry(int rowNumber, int columnNumber, double value){
        this.matrix[rowNumber][columnNumber] = value;
    }

    /**
     * Make the boolean in the isPivot array at a desired entry true or false.
     *
     * @param row - the row of the entry.
     * @param column - the column of the entry.
     * @param isPivot - whether the entry is or is not a pivot.
     */
    public void setIsPivot(int row, int column, boolean isPivot){
        this.isPivot[row][column] = isPivot;
    }

    /**
     * Check if a certain entry is or is not a pivot. This is done by checking if the boolean at that coordinate is or
     * is not true.
     *
     * @param row - the row of the entry.
     * @param column - the column of the entry.
     * @return - whether the entry is or is not a pivot.
     */
    public boolean getIsPivot(int row, int column){
        return this.isPivot[row][column];
    }

    /**
     * Returns a Matrix that is the sum of a given Matrix and this Matrix.
     *
     * @param matrix - the matrix that is to be added with.
     * @return - a matrix that is the sum of a given matrix and this matrix.
     */
    public Matrix plus(Matrix matrix){

        // Create a Matrix that we will return:
        Matrix output = new Matrix(this.rows, this.columns);

        // Add all the places and fill in the output matrix accordingly:
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                output.setEntry(i, j, matrix.getEntry(i, j) + this.matrix[i][j]);
            }
        }

        // Return a matrix that is the sum of the two matrices
        return output;
    }

    /**
     * Returns a Matrix that has switched two rows in this Matrix.
     *
     * @param firstRowNum - first row being switched.
     * @param secondRowNum - second row being switched.
     * @return - matrix with the switched rows.
     */
     public Matrix switchRows(int firstRowNum, int secondRowNum){
         Matrix outputMatrix = this.replicate();
         for (int i = 0; i < outputMatrix.getColumns(); i++) {
             outputMatrix.setEntry(firstRowNum, i, this.getEntry(secondRowNum, i));
             outputMatrix.setEntry(secondRowNum, i, this.getEntry(firstRowNum, i));
         }

         return outputMatrix;
     }

    /**
     * Row reduces a matrix.
     *
     * @return - a row reduced matrix
     */
    public Matrix rowReduce(){
        int pivotNumber = 0;
        Matrix outputMatrix = this.replicate();

        for (int i = 0; i < outputMatrix.getColumns(); i++) {

            // Make sure that we need to have a pivot in this row:
            boolean alreadyZeros = true;
            for (int j = pivotNumber; j < outputMatrix.getRows(); j++) {
                if (round(outputMatrix.getEntry(j, i), 10) != 0){
                    alreadyZeros = false;
                    break;
                }
            }

            // Make a pivot in the desired position. We will make sure that the position is not zero to make things easier.
            // If it is zero we switch its rows with one that has a nonzero entry in the column.
            if(pivotNumber >= outputMatrix.getRows()){
                break;
            }
            else if(alreadyZeros == true){

            }
            else if(outputMatrix.getEntry(pivotNumber, i) != 0) {
                outputMatrix = outputMatrix.makePivot(pivotNumber, i);
                outputMatrix.setIsPivot(pivotNumber, i, true);

                // Make zeros down from the pivot:
                outputMatrix = outputMatrix.zerosDown(pivotNumber, i);
                pivotNumber++;
            }
            else{
                // Make entry nonzero and restart process:
                outputMatrix = outputMatrix.switchRows(pivotNumber, outputMatrix.getNonZeroRowBelow(i, pivotNumber));
                i = -1;
                pivotNumber = 0;
                for (int j = 0; j < outputMatrix.getRows(); j++) {
                    for (int k = 0; k < outputMatrix.getColumns(); k++) {
                        outputMatrix.setIsPivot(j, k, false);
                    }
                }
            }
        }

        // Iterate through the matrix making zeros above all the pivots
        for (int i = outputMatrix.getRows() - 1; i >= 0; i--) {
            for (int j = outputMatrix.getColumns() - 1; j >= 0; j--) {
                if(outputMatrix.getIsPivot(i, j) == true){
                    outputMatrix = outputMatrix.zerosUp(i, j);
                }
            }
        }

        // Round all of the entries to the tenth decimal place:
        outputMatrix = outputMatrix.roundAll(10);

        return outputMatrix;
    }

    /**
     * Multiplies this Matrix by a given Matrix - remember that Matrix multiplication is not commutative - and returns
     * the product Matrix.
     *
     * @param matrix - matrix being multiplied by this Matrix.
     * @return - product of the two matrices.
     */
     public Matrix times(Matrix matrix) {
         double input = 0;
         Matrix outputMatrix = new Matrix(rows, matrix.getColumns());
         for (int i = 0; i < this.rows; i++) {
             for (int j = 0; j < matrix.getColumns(); j++) {
                 for (int k = 0; k < this.getColumns(); k++) {
                     input = input + (this.getEntry(i,k) * matrix.getEntry(k,j));
                 }
                 outputMatrix.setEntry(i,j, input);
                 input = 0;
             }
         }

         outputMatrix = outputMatrix.roundAll(10);
         return outputMatrix;
     }

    /**
     * A matrix that makes every entry in the column below a given entry equal to zero.
     *
     * @param row - the row number of the entry above where you want to zero down.
     * @param column - the column number of the entry above where you want to zero down.
     * @return - matrix that has been zeroed down.
     */
     public Matrix zerosDown(int row, int column) {
         Matrix outputMatrix = this.replicate();
         // Make sure that not every entry below the entry is a zero already:
         boolean allZeros = true;
         for (int i = row + 1; i < outputMatrix.getRows(); i++) {
             if (outputMatrix.getEntry(i, column) != 0) {
                 allZeros = false;
                 break;
             }
         }

         int nonZeroRowNum = row;
         // Make all the entries equal to zero:
         if(allZeros == false) {
             for (int i = row + 1; i < outputMatrix.getRows(); i++) {
                 if (nonZeroRowNum > -1) {
                     outputMatrix = outputMatrix.linearComboRows​(-outputMatrix.getEntry(i, column) / outputMatrix.getEntry(nonZeroRowNum, column), nonZeroRowNum, i);
                 }
             }
         }

         return outputMatrix;
     }


    /**
     * A method mirrors the row operations of a zeros down on a given reference matrix.
     *
     * @param row - the row number of the entry above where you want to zero down.
     * @param column - the column number of the entry above where you want to zero down.
     * @return - matrix that has been zeroed down.
     */
    public Matrix inversionZerosDown(int row, int column, Matrix referenceMatrix) {
        Matrix outputMatrix = this.replicate();
        // Make sure that not every entry below the entry is a zero already:
        boolean allZeros = true;
        for (int i = row + 1; i < referenceMatrix.getRows(); i++) {
            if (referenceMatrix.getEntry(i, column) != 0) {
                allZeros = false;
                break;
            }
        }

        int nonZeroRowNum = row;
        // Make all the entries equal to zero:
        if(allZeros == false) {
            for (int i = row + 1; i < outputMatrix.getRows(); i++) {
                if (nonZeroRowNum > -1) {
                    outputMatrix = outputMatrix.linearComboRows​(-referenceMatrix.getEntry(i, column) / referenceMatrix.getEntry(nonZeroRowNum, column), nonZeroRowNum, i);
                }
            }
        }

        return outputMatrix;
    }


    /**
     * A matrix that makes every entry in the column above a given entry equal to zero.
     *
     * @param row - the row number of the entry above where you want to zero up.
     * @param column - the column number of the entry above where you want to zero up.
     * @return - matrix that has been zeroed up.
     */
    public Matrix zerosUp(int row, int column){
        Matrix outputMatrix = this.replicate();

        // Make sure that every entry above the the entry is not zero already:
        boolean allZeros = true;
        for (int i = 0; i < row; i++) {
            if(outputMatrix.getEntry(i, column) != 0){
                allZeros = false;
                break;
            }
        }

        // Make all the entries equal to zero:
        int nonZeroRowNum = row;
        if(allZeros == false) {
            for (int i = 0; i < row; i++) {
                if (nonZeroRowNum > -1) {
                    outputMatrix = outputMatrix.linearComboRows​(-outputMatrix.getEntry(i, column) / outputMatrix.getEntry(nonZeroRowNum, column), nonZeroRowNum, i);
                }
            }
        }

        return outputMatrix;
    }


    /**
     * A method mirrors the row operations of a zeros up on a given reference matrix.
     *
     * @param row - the row number of the entry above where you want to zero up.
     * @param column - the column number of the entry above where you want to zero up.
     * @return - matrix that has been zeroed up.
     */
    public Matrix inversionZerosUp(int row, int column, Matrix referenceMatrix){
        Matrix outputMatrix = this.replicate();

        // Make sure that every entry above the the entry is not zero already:
        boolean allZeros = true;
        for (int i = 0; i < row; i++) {
            if(referenceMatrix.getEntry(i, column) != 0){
                allZeros = false;
                break;
            }
        }

        // Make all the entries equal to zero:
        int nonZeroRowNum = row;
        if(allZeros == false) {
            for (int i = 0; i < row; i++) {
                if (nonZeroRowNum > -1) {
                    outputMatrix = outputMatrix.linearComboRows​(-referenceMatrix.getEntry(i, column) / referenceMatrix.getEntry(nonZeroRowNum, column), nonZeroRowNum, i);
                }
            }
        }

        return outputMatrix;
    }


    /**
     * Make a pivot at a given entry location.
     *
     * @param row - the row number of the entry that you want to make a pivot.
     * @param column - the column number of the entry that you want to make a pivot.
     * @return - a matrix that has a pivot at the given entry location.
     */
     public Matrix makePivot(int row, int column){

         // Fill an output matrix with the same values as the given matrix:
         Matrix outputMatrix = this.replicate();

         // Make a pivot by multiplying the row by one over the value of the entry that is to become a pivot:
         outputMatrix = outputMatrix.scalarTimesRow(1 / outputMatrix.getEntry(row, column), row);
         return outputMatrix;
     }


    /**
     * Mimic the row operations that are being done on a matrix attempting to be made into an identity matrix for the
     * matrix being inverted.
     *
     * @param row - the row number of the entry that you want to make a pivot.
     * @param column - the column number of the entry that you want to make a pivot.
     * @param referenceMatrix - the matrix who's row operations are being mimicked.
     * @return - a matrix that has had the same row operations done on the matrix that is going to have a pivot created.
     */
    public Matrix inversionMakePivot(int row, int column, Matrix referenceMatrix){

        // Fill an output matrix with the same values as the given matrix:
        Matrix outputMatrix = this.replicate();

        // Get the scalar that would be used to make a pivot in the reference matrix and then scalarTimesRow the output
        // matrix with it.
        double mimickingScalar = 1 / referenceMatrix.getEntry(row, column);
        outputMatrix = outputMatrix.scalarTimesRow(mimickingScalar, row);
        return outputMatrix;
    }


    /**
     * Method that returns the row number of a nonzero entry in the matrix. It will search for a row with a nonzero
     * entry in the column below a given row number and then, if it is unsuccessful, try looking above the given row
     * number.
     *
     * @param column - the column number that you are looking for a non-zero entry in.
     * @return - the row number of a non-zero entry in the given column.
     */
     public int getNonZeroRowBelow(int column, int row){
         int nonZeroRowNum = -1;
         for (int i = row + 1; i < this.getRows(); i++) {
             if (this.getEntry(i, column) != 0) {
                 nonZeroRowNum = i;
             }
         }
         if(nonZeroRowNum == -1){
             for (int i = 0; i < row; i++) {
                 if (this.getEntry(i, column) != 0) {
                     nonZeroRowNum = i;
                 }
             }
         }
         return nonZeroRowNum;
     }


    /**
     * A method that replicates a matrix and returns the same matrix.
     *
     * @return - the replica of the given matrix.
     */
    public Matrix replicate(){

        // Fill an output matrix with the same values as the given matrix:
        Matrix outputMatrix = new Matrix(this.getRows(), this.getColumns());
        for (int i = 0; i < outputMatrix.getRows(); i++) {
             for (int j = 0; j < outputMatrix.getColumns(); j++) {
                 outputMatrix.setEntry(i, j, this.getEntry(i, j));
                 if(this.getIsPivot(i, j) == true){
                     outputMatrix.setIsPivot(i, j, true);
                 }
             }
        }

        return outputMatrix;
    }


    /**
     * Inverts this matrix.
     *
     * @return - the inversion of the matrix.
     */
    public Matrix invert(){
        Matrix thisMatrix = this.replicate(); // The Matrix that will turn into the identity Matrix.
        Matrix invertingMatrix = new Matrix(thisMatrix.getRows(), thisMatrix.getColumns()); // The matrix that will ultimately be the inversion
        for (int i = 0; i < thisMatrix.getRows(); i++) {
            invertingMatrix.setEntry(i, i, 1);
        }

        int pivotNumber = 0;
        for (int i = 0; i < thisMatrix.getColumns(); i++) {

            // Make sure that we need to have a pivot in this row:
            boolean alreadyZeros = true;
            for (int j = pivotNumber; j < thisMatrix.getRows(); j++) {
                if (thisMatrix.getEntry(j, i) != 0) {
                    alreadyZeros = false;
                    break;
                }
            }

            // Make a pivot in the desired position and mimic the row operations for the matrix that will be the
            // inversion. We will make sure that the position is not zero to make things easier. If it is zero we switch
            // its rows with one that has a nonzero entry in the column.
            if (pivotNumber >= thisMatrix.getRows()) {
                break;
            } else if (alreadyZeros == true) {

            } else if (thisMatrix.getEntry(pivotNumber, i) != 0) {
                invertingMatrix = invertingMatrix.inversionMakePivot(pivotNumber, i, thisMatrix);
                thisMatrix = thisMatrix.makePivot(pivotNumber, i);
                thisMatrix.setIsPivot(pivotNumber, i, true);

                // Make zeros down from the pivot for both Matrices:
                invertingMatrix = invertingMatrix.inversionZerosDown(pivotNumber, i, thisMatrix);
                thisMatrix = thisMatrix.zerosDown(pivotNumber, i);
                pivotNumber++;
            } else {
                // Make entry nonzero and restart process for both matrices:
                int rowSwitch = thisMatrix.getNonZeroRowBelow(i, pivotNumber);
                thisMatrix = thisMatrix.switchRows(pivotNumber, rowSwitch);
                invertingMatrix = invertingMatrix.switchRows(pivotNumber, rowSwitch);
                i = -1;
                pivotNumber = 0;
                for (int j = 0; j < thisMatrix.getRows(); j++) {
                    for (int k = 0; k < thisMatrix.getColumns(); k++) {
                        thisMatrix.setIsPivot(j, k, false);
                    }
                }
            }
        }

        // Iterate through the matrix making zeros above all the pivots and mimic operations for the matrix being
        // inverted.
        for (int i = thisMatrix.getRows() - 1; i >= 0; i--) {
            for (int j = thisMatrix.getColumns() - 1; j >= 0; j--) {
                if(thisMatrix.getIsPivot(i, j) == true){
                    invertingMatrix = invertingMatrix.inversionZerosUp(i, j, thisMatrix);
                    thisMatrix = thisMatrix.zerosUp(i, j);
                }
            }
        }

        // Round all of the entries to the tenth decimal place:
        invertingMatrix = invertingMatrix.roundAll(10);

        return invertingMatrix;
    }
}
