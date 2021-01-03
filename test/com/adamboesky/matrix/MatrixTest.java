package com.adamboesky.matrix;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MatrixTest {
    Matrix testMatrix = new Matrix(10, 15);

    @Test
    public void scalarTimesRow(){

        // Fill the matrix:
        double counter = 0;
        Matrix goalMatrix = new Matrix(10,15);
        for (int i = 0; i < testMatrix.getRows(); i++) {
            counter++;
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                counter++;
                testMatrix.setEntry(i, j, counter);
                goalMatrix.setEntry(i, j, counter);
            }
        }

        // Make the matrix that is the goal matrix:
        for (int i = 0; i < testMatrix.getColumns(); i++) {
            goalMatrix.setEntry(3, i, goalMatrix.getEntry(3, i) * 2);
        }

        Matrix scalared = testMatrix.scalarTimesRow(2,3);

        assertThat(checkTwoMatrices(scalared, goalMatrix), is(true));
    }

    @Test
    public void getNonZeroRowBelow(){
        Matrix testMatrix = new Matrix(15, 1);
        testMatrix.setEntry(12, 0, 1);
        int rowNum = testMatrix.getNonZeroRowBelow(0, 13);

        assertThat(rowNum, is(12));
    }

    @Test
    public void linearComboRows​(){

        // Fill the matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, 1);
            }
        }
        testMatrix = testMatrix.linearComboRows​(2, 1, 3);

        // Make the matrix that is the goal matrix:
        Matrix goalMatrix = new Matrix(testMatrix.getRows(), testMatrix.getColumns());
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, 1);
            }
        }
        for (int i = 0; i < testMatrix.getColumns(); i++) {
            goalMatrix.setEntry(3, i, 3);
        }

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void plus(){

        // Fill the Matrix with ones:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, 1);
            }
        }
        testMatrix = testMatrix.plus(testMatrix);

        // Make the matrix that is the goal matrix:
        Matrix goalMatrix = new Matrix(testMatrix.getRows(), testMatrix.getColumns());
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, 2);
            }
        }

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void switchRows(){

        // Fill the Matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, i);
            }
        }

        // Make the matrix that is the goal matrix:
        Matrix goalMatrix = new Matrix(testMatrix.getRows(), testMatrix.getColumns());
        for (int i = 0; i < goalMatrix.getRows(); i++) {
            for (int j = 0; j < goalMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, i);
            }
        }

        // Switch the rows manually:
        for (int i = 0; i < goalMatrix.getColumns(); i++) {
            goalMatrix.setEntry(4, i, 9);
            goalMatrix.setEntry(9, i, 4);
        }

        testMatrix = testMatrix.switchRows(4, 9);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void times(){

        Matrix multMatrix = new Matrix(2,3);
        // Fill the matrix:
        double counter = 0;
        for (int i = 0; i < multMatrix.getRows(); i++) {
            counter++;
            for (int j = 0; j < multMatrix.getColumns(); j++) {
                counter++;
                multMatrix.setEntry(i, j, counter);
            }
        }

        // Make the matrix that is the goal matrix:
        Matrix multMatrix2 = new Matrix(3, 4);
        double counter2 = 0;
        for (int i = 0; i < multMatrix2.getRows(); i++) {
            counter2++;
            for (int j = 0; j < multMatrix2.getColumns(); j++) {
                counter2++;
                multMatrix2.setEntry(i, j, counter2);
            }
        }

        multMatrix = multMatrix.times(multMatrix2);

        // Make the matrix that is the goal matrix:
        Matrix goalMatrix = new Matrix(2, 4);
        goalMatrix.setEntry(0, 0, 73);
        goalMatrix.setEntry(0, 1, 82);
        goalMatrix.setEntry(0, 2, 91);
        goalMatrix.setEntry(0, 3, 100);
        goalMatrix.setEntry(1, 0, 157);
        goalMatrix.setEntry(1, 1, 178);
        goalMatrix.setEntry(1, 2, 199);
        goalMatrix.setEntry(1, 3, 220);

        assertThat(checkTwoMatrices(multMatrix, goalMatrix), is(true));
    }

    @Test
    public void setEntry(){
        Matrix entryMatrix = new Matrix(3,4);
        entryMatrix.setEntry(2, 1, 3.53633456);

        assertThat(entryMatrix.getEntry(2, 1), is(3.53633456));
    }

    @Test
    public void setIsPivot(){
        testMatrix.setIsPivot(1, 0, true);

        assertThat(testMatrix.getIsPivot(1, 0), is(true));
    }

    @Test
    public void getIsPivot(){
        testMatrix.setIsPivot(1, 0, true);

        assertThat(testMatrix.getIsPivot(1, 0), is(true));
    }

    @Test
    public void inversionZerosDown() {
        Matrix goalMatrix = new Matrix(10, 15);
        double counter = 1;
        // Fill the test and goal matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, counter);
                goalMatrix.setEntry(i, j, counter);
            }
            counter++;
        }

        // Adjust the goal Matrix:
        for (int i = 4; i < goalMatrix.getRows(); i++) {
            for (int j = 0; j < goalMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, 0);
            }
        }

        testMatrix = testMatrix.inversionZerosDown(3, 8, testMatrix);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void inversionZerosUp(){

        Matrix goalMatrix = new Matrix(10, 15);
        double counter = 1;
        // Fill the test and goal Matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, counter);
                goalMatrix.setEntry(i, j, counter);
            }
            counter++;
        }

        // Adjust the goal Matrix:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < goalMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, 0);
            }
        }

        testMatrix = testMatrix.inversionZerosUp(4,8, testMatrix);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void inversionMakePivot(){

        Matrix goalMatrix = new Matrix(10, 15);
        // Fill the test and goal matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, 2);
                goalMatrix.setEntry(i, j, 2);
            }
        }

        // Adjust the goal matrix:
        for (int i = 0; i < goalMatrix.getColumns(); i++) {
            goalMatrix.setEntry(3, i, 1);
        }

        testMatrix = testMatrix.inversionMakePivot(3, 5, testMatrix);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void zerosDown() {

        Matrix goalMatrix = new Matrix(10, 15);
        double counter = 1;
        // Fill the test and goal matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, counter);
                goalMatrix.setEntry(i, j, counter);
            }
            counter++;
        }

        // Adjust the goal Matrix:
        for (int i = 4; i < goalMatrix.getRows(); i++) {
            for (int j = 0; j < goalMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, 0);
            }
        }

        testMatrix = testMatrix.zerosDown(3, 8);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void zerosUp() {

        Matrix goalMatrix = new Matrix(10, 15);
        double counter = 1;
        // Fill the test and goal Matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, counter);
                goalMatrix.setEntry(i, j, counter);
            }
            counter++;
        }

        // Adjust the goal Matrix:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < goalMatrix.getColumns(); j++) {
                goalMatrix.setEntry(i, j, 0);
            }
        }

        testMatrix = testMatrix.zerosUp(4,8);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void replicate(){
        int counter = 1;
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, counter);
                counter++;
            }
        }

        Matrix replica = testMatrix.replicate();

        assertThat(checkTwoMatrices(testMatrix, replica), is(true));
    }

    @Test
    public void makePivot(){

        Matrix goalMatrix = new Matrix(10, 15);
        // Fill the test and goal matrix:
        for (int i = 0; i < testMatrix.getRows(); i++) {
            for (int j = 0; j < testMatrix.getColumns(); j++) {
                testMatrix.setEntry(i, j, 2);
                goalMatrix.setEntry(i, j, 2);
            }
        }

        // Adjust the goal matrix:
        for (int i = 0; i < goalMatrix.getColumns(); i++) {
            goalMatrix.setEntry(3, i, 1);
        }

        testMatrix = testMatrix.makePivot(3, 5);

        assertThat(checkTwoMatrices(testMatrix, goalMatrix), is(true));
    }

    @Test
    public void roundAll(){
        Matrix roundMatrix = new Matrix(3, 3);
        roundMatrix.setEntry(0, 0, 0.05);
        roundMatrix.setEntry(0, 1, .39999999999999997);
        roundMatrix.setEntry(0, 2, -0.15);
        roundMatrix.setEntry(1, 0, 0.35);
        roundMatrix.setEntry(1, 1, -0.19999999999999998);
        roundMatrix.setEntry(1, 2, -0.04999999999999999);
        roundMatrix.setEntry(2, 0, -0.15);
        roundMatrix.setEntry(2, 1, -0.19999999999999996);
        roundMatrix.setEntry(2, 2, 0.44999999999999996);

        Matrix goalMatrix = new Matrix(3, 3);
        goalMatrix.setEntry(0, 0, 0.05);
        goalMatrix.setEntry(0, 1, 0.4);
        goalMatrix.setEntry(0, 2, -0.15);
        goalMatrix.setEntry(1, 0, 0.35);
        goalMatrix.setEntry(1, 1, -0.2);
        goalMatrix.setEntry(1, 2, -0.05);
        goalMatrix.setEntry(2, 0, -0.15);
        goalMatrix.setEntry(2, 1, -0.2);
        goalMatrix.setEntry(2, 2, 0.45);

        roundMatrix = roundMatrix.roundAll(10);

        assertThat(checkTwoMatrices(roundMatrix, goalMatrix), is(true));
    }

    @Test
    public void rowReduce(){
        Matrix initialMatrix = new Matrix(4,3);
        initialMatrix.setEntry(0, 0, 2); initialMatrix.setEntry(0, 1, 3); initialMatrix.setEntry(0, 2, 1);
        initialMatrix.setEntry(1, 0, 3); initialMatrix.setEntry(1, 1, 0); initialMatrix.setEntry(1, 2, 1);
        initialMatrix.setEntry(2, 0, 2); initialMatrix.setEntry(2, 1, 1); initialMatrix.setEntry(2, 2, 3);
        initialMatrix.setEntry(3, 0, -4); initialMatrix.setEntry(3, 1, 0); initialMatrix.setEntry(3, 2, 7);

        Matrix finalMatrix = new Matrix(4, 3);

        finalMatrix.setEntry(0, 0, 1);
        finalMatrix.setEntry(1, 1, 1);
        finalMatrix.setEntry(2, 2, 1);

        initialMatrix = initialMatrix.rowReduce();

        assertThat(checkTwoMatrices(initialMatrix, finalMatrix), is(true));
    }

    @Test
    public void invert(){
        Matrix initialMatrix = new Matrix(3,3);
        initialMatrix.setEntry(0, 0, 2);
        initialMatrix.setEntry(0, 1, 3);
        initialMatrix.setEntry(0, 2, 1);
        initialMatrix.setEntry(1, 0, 3);
        initialMatrix.setEntry(1, 1, 0);
        initialMatrix.setEntry(1, 2, 1);
        initialMatrix.setEntry(2, 0, 2);
        initialMatrix.setEntry(2, 1, 1);
        initialMatrix.setEntry(2, 2, 3);

        Matrix identity = initialMatrix.rowReduce();
        Matrix inverse = initialMatrix.invert();

        assertThat(checkTwoMatrices(inverse.times(initialMatrix), identity), is(true));
    }

    @Test
    public void MatrixBasic(){
        Matrix constructorMatrix = new Matrix();
        assertThat(constructorMatrix.getColumns(), is(1));
        assertThat(constructorMatrix.getRows(), is(1));
        assertThat(constructorMatrix.getEntry(0, 0), is(0.0));
        assertThat(constructorMatrix.getIsPivot(0, 0), is(false));
    }

    @Test
    public void Matrix(){
        Matrix constructorMatrix = new Matrix(3, 4);
        constructorMatrix.setEntry(2,3, .235);
        constructorMatrix.setIsPivot(2, 1, true);

        assertThat(constructorMatrix.getColumns(), is(4));
        assertThat(constructorMatrix.getRows(), is(3));
        assertThat(constructorMatrix.getEntry(2,3), is(.235));
        assertThat(constructorMatrix.getIsPivot(2, 1), is(true));
    }

    @Test
    public void getRows(){
        assertThat(testMatrix.getRows(), is(10));
    }

    @Test
    public void getColumns(){
        assertThat(testMatrix.getColumns(), is(15));
    }

    @Test
    public void getEntry(){
        testMatrix.setEntry(7, 8, 139.34);
        assertThat(testMatrix.getEntry(7, 8), is(139.34));
    }

    @Test
    public void matrixToString(){
        Matrix stringMatrix = new Matrix(1, 3);
        for (int i = 0; i < 3; i++) {
            stringMatrix.setEntry(0, i, i);
        }
        assertThat(stringMatrix.toString(), is("0.0   1.0   2.0   "));
    }

    public boolean checkTwoMatrices(Matrix matrix1, Matrix matrix2) {
        boolean areTheSame = true;
        for (int i = 0; i < matrix1.getRows(); i++) {
            for (int j = 0; j < matrix2.getColumns(); j++) {
                if (matrix1.getEntry(i, j) != matrix2.getEntry(i, j)) {
                    areTheSame = false;
                }
            }
        }
        return areTheSame;
    }
}
