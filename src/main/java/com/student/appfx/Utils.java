package com.student.appfx;

import com.student.appfx.entities.expertExperiments.CorrectionElement;
import com.student.appfx.entities.MatrixOrVector;
import com.student.appfx.exceptions.MismatchColumnsStringsException;

import java.util.ArrayList;
import java.util.List;


public class Utils {

    public static void normalizerMatrixOrVector(double[][] matrix, int axis) {
        switch (axis) {
            case 0 -> {
                double[] lengths = getColumnsLengths(matrix);
                for (int j = 0; j < matrix[0].length; j++) {
                    for (int i = 0; i < matrix.length; i++) {
                        matrix[i][j] = matrix[i][j] / lengths[j];
                    }
                }
            }
            case 1 -> {
                for (double[] d : matrix) {
                    normalizeVector(d);
                }
            }
        }
    }

    public static void singleSumReduction(double[][] matrix, int axis) {
        switch (axis) {
            case 0 -> {
                double[] sums = getColumnsSums(matrix);
                for (int j = 0; j < matrix[0].length; j++) {
                    for (int i = 0; i < matrix.length; i++) {
                        matrix[i][j] = matrix[i][j] / sums[j];
                    }
                }
            }
            case 1 -> {
                for (double[] d : matrix) {
                    vectorSingleSumReduction(d);
                }
            }
        }
    }

    public static MatrixOrVector multiply(MatrixOrVector x, MatrixOrVector y) throws MismatchColumnsStringsException {
        if (x.getColumnAmount() != y.getStringAmount()) {
            throw new MismatchColumnsStringsException("Rule of matrix multiply is not perform");
        }
        int str = x.getStringAmount();
        int col = y.getColumnAmount();
        int tmp = x.getColumnAmount();
        MatrixOrVector result = new MatrixOrVector(str, col);
        for (int i = 0; i < str; i++) {
            for (int j = 0; j < col; j++) {
                double sum = 0;
                for (int k = 0; k < tmp; k++) {
                    sum += x.getElement(i, k)*y.getElement(k, j);
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    public static double currentAccuracy(double[][] one, double[][] two) throws MismatchColumnsStringsException {
        if (one.length != two.length | one[0].length != two[0].length) {
            throw new MismatchColumnsStringsException("Columns or strings of comparing objects is mismatch");
        }
        double result = 0;
        double tmp;
        for (int i = 0; i < one.length; i++) {
            for (int j = 0; j < one[0].length; j++) {
                tmp = one[i][j] - two[i][j];
                if (Math.abs(tmp) > result)
                    result = Math.abs(tmp);
            }
        }
        return result;
    }

    public static List<CorrectionElement> correction(MatrixOrVector item, double multiplier) {
        List<CorrectionElement> res = new ArrayList<>();
        for(int i = 0; i < item.getStringAmount(); i++) {
            for (int j = 0; j < item.getColumnAmount(); j++) {
                if(item.get()[i][j] < 0) {
                    res.add(new CorrectionElement(i, item.get()[i][j]));
                    item.get()[i][j] *= multiplier;
                }
            }
        }
        return res;
    }

    //only for inner using
    private static double[] getColumnsSums(double[][] matrix) {
        double[] sums = new double[matrix[0].length];
        for (int j = 0; j < matrix[0].length; j++) {
            double sum = 0;
            for (double[] d : matrix) {
                sum += d[j];
            }
            sums[j] = sum;
        }
        return sums;
    }

    private static double[] getColumnsLengths(double[][] matrix) {
        double[] lengths = new double[matrix[0].length];
        for (int j = 0; j < matrix[0].length; j++) {
            double sum = 0;
            for (double[] d : matrix) {
                sum += d[j] * d[j];
            }
            lengths[j] = Math.sqrt(sum);
        }
        return lengths;
    }

    private static void vectorSingleSumReduction(double[] vector) {
        double sum = getVectorSum(vector);
        for(int i = 0; i < vector.length; i++) {
            vector[i] = vector[i]/sum;
        }
    }

    private static double getVectorSum(double[] vector) {
        double sum = 0;
        for (double d : vector) {
            sum += d;
        }
        return sum;
    }

    private static void normalizeVector(double[] vector) {
        double len = getVectorLength(vector);
        for(int i = 0; i < vector.length; i++) {
            vector[i] = vector[i]/len;
        }
    }

    private static double getVectorLength(double[] vector) {
        double sum = 0;
        for (double d : vector) {
            sum += d * d;
        }
        return Math.sqrt(sum);
    }
}
