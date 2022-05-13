package com.student.appfx.cache;

import com.student.appfx.entities.informationExperiments.Splitter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataForInformationExperiments {

    public static boolean dataInput = false;

    public static int experts = 0;

    public static double level = 0;

    public static List<Splitter> expertsRatings = new ArrayList<>();

    public static int getGood() {
        int res = 0;
        for (Splitter splitter: expertsRatings) {
            if (splitter.getLabel().equals("n")) {
                res+=1;
            }
        }
        return res;
    }

    public static int getBad() {
        int res = 0;
        for (Splitter splitter: expertsRatings) {
            if (splitter.getLabel().equals("h") | splitter.getLabel().equals("l")) {
                res+=1;
            }
        }
        return res;
    }

    public static boolean containsGood() {
        for (Splitter splitter: expertsRatings) {
            if(splitter.getLabel().equals("n")) {
                return true;
            }
        }
        return false;
    }

    public static void addBad() {
        Splitter tmp = null;
        double min = 100;
        for (Splitter splitter: expertsRatings) {
            if(splitter.getLabel().equals("n") & min > splitter.getRating()) {
                min = splitter.getRating();
                tmp = splitter;
            }
        }
        if (tmp != null) {
            Random random = new Random();
            if (random.nextBoolean()) {
                tmp.setLabel("l");
            } else {
                tmp.setLabel("l");
            }
        }
    }

    public static List<Splitter> copyRatings(List<Splitter> splitterList) {
        List<Splitter> copy = new ArrayList<>();
        for (Splitter splitter: splitterList) {
            copy.add(new Splitter(splitter.getLabel(), splitter.getRating()));
        }
        return copy;
    }

    public static LineChart<Number, Number> copyChart(LineChart<Number, Number> chart) {
        ObservableList<XYChart.Series<Number, Number>> newSeriesList = FXCollections.observableArrayList();
        for (XYChart.Series<Number, Number> series: chart.getData()) {
            XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
            newSeries.setName(series.getName());
            for (XYChart.Data<Number, Number> data: series.getData()) {
                newSeries.getData().add(new XYChart.Data<>(data.getXValue(), data.getYValue()));
            }
            newSeriesList.add(newSeries);
        }
        LineChart<Number, Number> lineChart = createChart(newSeriesList, chart.getXAxis().getLabel(), chart.getYAxis().getLabel());
        if (chart.getData().get(0).getNode().getProperties().get("javafx.scene.control.Tooltip") != null) {
            for (int i = 0; i < lineChart.getData().size(); i++) {
                Tooltip.install(
                        lineChart.getData().get(i).getNode(),
                        new Tooltip(
                                ((Tooltip) chart.getData().get(i).getNode().getProperties().get("javafx.scene.control.Tooltip")).getText()));
            }
        }
        lineChart.setTitle(chart.getTitle());
        return lineChart;
    }

    private static LineChart<Number, Number> createChart(ObservableList<XYChart.Series<Number, Number>> seriesList,
                                                  String xLabel, String yLabel) {
        final NumberAxis xAxis = new NumberAxis();
        if (xLabel != null) {
            xAxis.setLabel(xLabel);
        }
        final NumberAxis yAxis = new NumberAxis();
        if (xLabel != null) {
            yAxis.setLabel(yLabel);
        }
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setData(seriesList);
        return chart;
    }

    public static void clearAll() {
        dataInput = false;
        experts = 0;
        expertsRatings.clear();
    }
    public static void clear() {
        dataInput = false;
        experts = 0;
    }
}
