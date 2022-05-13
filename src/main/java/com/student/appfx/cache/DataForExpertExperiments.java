package com.student.appfx.cache;

import com.student.appfx.entities.MatrixOrVector;
import javafx.scene.chart.LineChart;

import java.util.ArrayList;
import java.util.List;

public class DataForExpertExperiments {

    public static boolean dataInput = false;

    public static int experimentType = 0;

    public static int normType = 0;

    public static boolean zeroOnDiagonal = false;

    public static double correctionMultiplier = 0.0;

    public static List<Double> accuracy = new ArrayList<>();

    public static List<Integer> diapasons = new ArrayList<>();

    public static List<Integer> experts = new ArrayList<>();

    public static List<Integer> seeds = new ArrayList<>();

    public static void clear() {
        accuracy.clear();
        diapasons.clear();
        experts.clear();
        seeds.clear();
        dataInput = false;
        experimentType = 0;
        correctionMultiplier = 0.0;
    }
}
