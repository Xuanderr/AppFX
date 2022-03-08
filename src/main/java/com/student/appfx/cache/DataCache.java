package com.student.appfx.cache;

import javafx.scene.chart.LineChart;

import java.util.ArrayList;
import java.util.List;

public class DataCache {

    public static boolean dataInput = false;

    public static int experimentType;

    public static List<Double> accuracy = new ArrayList<>();

    public static List<Integer> diapasons = new ArrayList<>();

    public static List<Integer> experts = new ArrayList<>();

    public static List<LineChart<Number, Number>> graphics = new ArrayList<>();

    public static void clear() {
        accuracy.clear();
        diapasons.clear();
        experts.clear();
        dataInput = false;
        experimentType = 0;
    }
}