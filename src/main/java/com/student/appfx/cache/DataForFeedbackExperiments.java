package com.student.appfx.cache;

import com.student.appfx.entities.MatrixOrVector;

import java.util.Random;

public class DataForFeedbackExperiments {

    public static boolean dataInput = false;

    public static Integer seed = 10;

    public static int experts = 0;

    public static double level = 0;

    public static MatrixOrVector matrix;

    public static MatrixOrVector vector;

    public static MatrixOrVector getExpertDefaultVector() {
        MatrixOrVector vector = new MatrixOrVector(experts, 1);
        for (int i = 0; i < experts; i++) {
            for (int j = 0; j < 1; j++) {
                vector.setElement(i, j, 1);
            }
        }
        return vector;
    }

    public static MatrixOrVector getExpertMatrix() {
        Random random = new Random();
        if(seed != null) {
            random.setSeed(seed);
        }
        MatrixOrVector matrix = new MatrixOrVector(experts, experts);
        for (int i = 0; i < experts; i++) {
            for (int j = 0; j < experts; j++) {
                if( i == j & DataForExpertExperiments.zeroOnDiagonal) {
                    matrix.setElement(i, j, 0);
                } else {
                    matrix.setElement(i, j, random.nextInt(11));
                }
            }
        }
        return matrix;
    }


    public static void clear() {
        dataInput = false;
        experts = 0;
        level = 0;
        matrix = null;
        vector = null;
    }
}
