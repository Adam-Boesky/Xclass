package com.adamboesky.matrices;

public class Matrix {
    private int rows;
    private int columns;
    private double[][] matrix;

    /**
     * A constructor method
     *
     * @param rows - the number of rows in the matrix
     * @param columns - the number of columns in this matrix
     */
    Matrix(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.matrix = new double[rows][columns];
    }

    /**
     * A constructor method
     */
    Matrix(){
        this.rows = 1;
        this.columns = 1;
        this.matrix = new double[rows][columns];
        this.matrix[0][0] = 0;
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
     * Set the number of rows of the matrix.
     *
     * @param rows - the number of rows that the matrix will be.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Set the number of columns of the matrix.
     *
     * @param columns - the number of columns that the matrix will be.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Set the values of an entire row of the matrix given an array and the number of the row that is to be set.
     *
     * @param row - the array of doubles that hold the values of the row that is to be set.
     * @param rowNumber - the number of the row that is to be set.
     */
    public void setRowValues(double[] row, int rowNumber){
        this.matrix[rowNumber] = row;
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
        Matrix endMatrix = new Matrix(this.rows, this.columns);

        for (int i = 0; i < this.columns; i++) {
            endMatrix.setEntry(rowNumber, i, this.getEntry(rowNumber, i) * scalar);
        }

        return endMatrix;
    }

    /**
     * Returns a matrix that has added a scalar multiple of the first row to the second row of this Matrix.
     *
     * @param scalar - number by which each entry in the first row will be multiplied.
     * @param firstRow - row being multiplied by a scalar and added to the second row.
     * @param secondRow - row to which the first row is added.
     * @return - Matrix with the modified secondRow
     */
    public Matrix linearComboRows​(double scalar, int firstRow, int secondRow) {
        Matrix outputMatrix = this;
        for (int i = 0; i < outputMatrix.getColumns(); i++) {
            outputMatrix.setEntry(secondRow, i, (outputMatrix.getEntry(secondRow, i) + (scalar * outputMatrix.getEntry(firstRow, i))));
        }

        return outputMatrix;
    }

    public void print(){
        for (int i = 0; i < this.rows; i++) {
            System.out.print("(");
            for (int j = 0; j < this.columns; j++) {
                if(j != columns){
                    System.out.print(this.getEntry(i, j) + "  ");
                }
                else{
                    System.out.println(this.getEntry(i, j) + ")");
                }
            }
        }
    }

    /**
     * Sets the value at a specific spot in this Matrix.
     *
     * @param rowNumber - the row number of the value that is to be set.
     * @param columnNumber - the column number of the value that is to be set.
     * @param value - the value of the place that is to be set/
     */
    public void setEntry(int rowNumber, int columnNumber, double value){
        this.matrix[rowNumber][columnNumber] = value;
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
         Matrix outputMatrix = this;
         double[] secondRow = this.matrix[secondRowNum];
         double[] firstRow = this.matrix[firstRowNum];
         outputMatrix.setRowValues(secondRow, firstRowNum);
         outputMatrix.setRowValues(firstRow, secondRowNum);
         return outputMatrix;
     }

    /**
     * Multiplies this Matrix by a given Matrix - remember that Matrix multiplication is not commutative - and returns
     * the product Matrix.
     *
     * @param matrix - matrix being multiplied by this Matrix
     * @return - product of the two matrices
     */
     public Matrix times(Matrix matrix) {
         Matrix outputMatrix = new Matrix(this.getRows(), matrix.getColumns());
         for (int i = 0; i < this.rows; i++) {
             for (int j = 0; j < matrix.getColumns(); j++) {
                 double input = 0;
                 for (int k = 0; k < this.columns; k++) {
                     input = input + (this.getEntry(i, k) * matrix.getEntry(j, k));
                 }
                 outputMatrix.setEntry(i,j, input);
             }
         }
         return outputMatrix;
     }
}
