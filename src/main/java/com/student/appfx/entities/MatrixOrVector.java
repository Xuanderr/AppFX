package com.student.appfx.entities;

public class MatrixOrVector {

    private final double[][] a;

    public MatrixOrVector(int strSize, int colSize) {
        this.a = new double[strSize][colSize];
    }

    public double[][] get() {
        return a;
    }

    public int getStringAmount() {
        return a.length;
    }

    public int getColumnAmount() {
        return a[0].length;
    }

    public double getElement(int i, int j) {
        return a[i][j];
    }

    public void setElement(int i, int j, double value) {
        a[i][j] = value;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("---" + "\n");
        for (double[] doubles : a) {
            s.append("| ");
            for (int j = 0; j < a[0].length; j++) {
                if (isInt(doubles[j])) {
                    s.append((int) doubles[j]);
                } else {
                    s.append(doubles[j]);
                }
                s.append(" ");
            }
            s.append("\n");
        }
        s.append("---");
        return s.toString();
    }

    private boolean isInt(double value) {
        int tmp = (int) value;
        double a = tmp - value;
        return (tmp - value) == 0;
    }
}
