package com.student.appfx.cache;

import com.student.appfx.entities.MatrixOrVector;
import javafx.scene.chart.LineChart;

import java.util.ArrayList;
import java.util.List;

public class MainCache {

    public static int partToShow = 0;

    public static List<LineChart<Number, Number>> graphics = new ArrayList<>();

    public static MatrixOrVector expertsRatings;

    public static LineChart<Number, Number> chartForResearch;

}
