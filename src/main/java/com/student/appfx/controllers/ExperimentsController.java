package com.student.appfx.controllers;

import com.student.appfx.cache.DataCache;
import com.student.appfx.entities.Experiment;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.math.BigDecimal;

public class ExperimentsController {

    private Integer expStart;
    private Integer expEnd;

    private Double acStart;
    private Double acEnd;
    private Double acStep;

    private Integer seedStart;
    private Integer seedEnd;

    public boolean doExperiment() {
        init();
        DataCache.graphics.clear();
        switch (DataCache.experimentType) {
            case 1 -> {
                launchTypeOne();
                return true;
            }
            case 2 -> {
                launchTypeTwo();
                return true;
            }
            case 3 -> {
                launchTypeThree();
                return true;
            }
            case 4 -> {
                launchTypeFour();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void launchTypeOne() {
        double acSize = ((acEnd == 0) ? acStart : acEnd);
        int expSize = ((expEnd == 0) ? expStart : expEnd);
        double xTics = 0.1;
        double yTics = 0.1;
        for (int d = 0; d < DataCache.diapasons.size(); d++) {
            for(double a = acStart; a <= acSize; a += acStep) {
                for (int e = expStart; e <= expSize; e += 1) {
                    Experiment experiment = new Experiment(e, a, DataCache.diapasons.get(d), null);
                    experiment.launch();
                    ObservableList<XYChart.Series<Number, Number>> seriesList = experiment.getSeriesList();
                    LineChart<Number,Number> lineChart = createChart(seriesList, "iteration", "expert value");
                    if(DataCache.diapasons.get(d) == 3) {
                        xTics = 50;
                    }
                    setAxisTicsAndLowerUpperBounds(seriesList, lineChart, xTics, yTics);
                    lineChart.setTitle(String.format(
                            "diapason: %s, accuracy: %f, experts: %d", genTypeToString(DataCache.diapasons.get(d)), a, e));
                    DataCache.graphics.add(lineChart);
                }
            }
        }
    }

    private void launchTypeTwo() {
        double acSize = ((acEnd == 0) ? acStart : acEnd);
        int expSize = ((expEnd == 0) ? expStart : expEnd);
        for(double a = acStart; a <= acSize; a += acStep) {
            for (int d = 0; d < DataCache.diapasons.size(); d++) {
                XYChart.Series<Number,Number> series = new XYChart.Series<>();
                series.setName(genTypeToString(DataCache.diapasons.get(d)));
                for (int e = expStart; e <= expSize; e += 1) {
                    Experiment experiment = new Experiment(e, a, DataCache.diapasons.get(d), null);
                    experiment.launch();
                    XYChart.Data<Number,Number> data = experiment.getData();
                    series.getData().add(data);
                }
                LineChart<Number,Number> chart = createAdditionalChart(series, "number of experts", "convergence");
                chart.setTitle(String.format("accuracy: %f", a));
                DataCache.graphics.add(chart);
            }
        }
    }

    private void launchTypeThree() {
        double acSize = ((acEnd == 0) ? acStart : acEnd);
        int expSize = ((expEnd == 0) ? expStart : expEnd);
        int seedSize = ((seedEnd == 0) ? seedStart : seedEnd);
        double xTics = 0.1;
        double yTics = 0.1;
        for (int d = 0; d < DataCache.diapasons.size(); d++) {
            for(double a = acStart; a <= acSize; a += acStep) {
                for (int e = expStart; e <= expSize; e += 1) {
                    for (int s = seedStart; s <= seedSize; s += 1) {
                        Experiment experiment = new Experiment(e, a, DataCache.diapasons.get(d), s);
                        experiment.launch();
                        ObservableList<XYChart.Series<Number, Number>> seriesList = experiment.getSeriesList();
                        LineChart<Number,Number> lineChart = createChart(seriesList, "iteration", "expert value");
                        if(DataCache.diapasons.get(d) == 3) {
                            xTics = 50;
                        }
                        setAxisTicsAndLowerUpperBounds(seriesList, lineChart, xTics, yTics);
                        lineChart.setTitle(String.format(
                                "diapason: %s, accuracy: %f, experts: %d, seed: %d",
                                genTypeToString(DataCache.diapasons.get(d)), a, e, s));
                        DataCache.graphics.add(lineChart);
                    }
                }
            }
        }
    }

    private void launchTypeFour() {
        double acSize = ((acEnd == 0) ? acStart : acEnd);
        int expSize = ((expEnd == 0) ? expStart : expEnd);
        int seedSize = ((seedEnd == 0) ? seedStart : seedEnd);
        for(double a = acStart; a <= acSize; a += acStep) {
            for (int e = expStart; e <= expSize; e += 1) {
                for (int s = seedStart; s <= seedSize; s += 1) {
                    Experiment experiment = new Experiment(e, a, 3, s);
                    experiment.launch();
                    ObservableList<XYChart.Series<Number, Number>> before = experiment.getCorrectionData().getBefore();
                    ObservableList<XYChart.Series<Number, Number>> after = experiment.getCorrectionData().getAfter();
                    LineChart<Number,Number> beforeChart = createChart(before, "iteration", "expert value");
                    LineChart<Number,Number> afterChart = createChart(after, "iteration", "expert value");
                    setAxisTicsAndLowerUpperBounds(before, beforeChart, 50, 0.1);
                    setAxisTicsAndLowerUpperBounds(after, afterChart, 50, 0.1);
                    beforeChart.setTitle(String.format(
                            "diapason: [-10, 10], accuracy: %f, experts: %d, seed: %d, correction: false", a, e, s));
                    afterChart.setTitle(String.format(
                            "diapason: [-10, 10], accuracy: %f, experts: %d, seed: %d, correction: multiplier = %f",
                            a, e, s, DataCache.correctionMultiplier));
                    DataCache.graphics.add(beforeChart);
                    DataCache.graphics.add(afterChart);
                }
            }
        }
    }

    private String genTypeToString(int genType) {
        switch (genType) {
            case 1 -> {
                return "[0, 10]";
            }
            case 2 -> {
                return "[-1, 10]";
            }
            case 3 -> {
                return "[-10, 10]";
            }
        }
        return "unknown";
    }

    private void init() {
        expStart = DataCache.experts.get(0);
        if(DataCache.experts.size() != 1) {
            expEnd = DataCache.experts.get(1);
        } else {
            expEnd = 0;
        }
        acStart = DataCache.accuracy.get(0);
        if(DataCache.accuracy.size() != 1) {
            acEnd = DataCache.accuracy.get(1);
        } else {
            acEnd = 0.0;;
        }
        int scale = BigDecimal.valueOf(acStart).scale();
        acStep = 1.0 / Math.pow(10, scale);
        if (!DataCache.seeds.isEmpty()) {
            seedStart = DataCache.seeds.get(0);
            if (DataCache.seeds.size() != 1) {
                seedEnd = DataCache.seeds.get(1);
            } else {
                seedEnd = 0;
            }
        }
    }

    private LineChart<Number, Number> createChart(ObservableList<XYChart.Series<Number, Number>> seriesList,
                                                  String xLabel, String yLabel) {
        final NumberAxis xAxis = createAxis(xLabel);
        final NumberAxis yAxis = createAxis(yLabel);
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setData(seriesList);
        return chart ;
    }

    private LineChart<Number, Number> createAdditionalChart(XYChart.Series<Number, Number> series, String xLabel, String yLabel) {
        final NumberAxis xAxis = createAxis(xLabel);
        final NumberAxis yAxis = createAxis(yLabel);
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.getData().add(series);
        return chart ;
    }

    private NumberAxis createAxis(String label) {
        final NumberAxis axis = new NumberAxis();
        axis.setLabel(label);
        return axis;
    }

    private void setAxisTicsAndLowerUpperBounds(ObservableList<XYChart.Series<Number, Number>> seriesList,
                                                LineChart<Number,Number> lineChart, double xTicks, double yTics) {
        double xUpper = 0;
        double yUpper = 0;
        for (XYChart.Series<Number, Number> series: seriesList) {
            for (XYChart.Data<Number, Number> data: series.getData()) {
                double tmpXUpper = Double.parseDouble(String.valueOf(data.getXValue()));
                if (xUpper < tmpXUpper) {
                    xUpper = tmpXUpper;
                }
                double tmpYUpper = Double.parseDouble(String.valueOf(data.getYValue()));
                if (yUpper < tmpYUpper) {
                    yUpper = tmpYUpper;
                }
            }
        }
        final NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(xUpper + 1);
        xAxis.setTickUnit(xTicks);
        final NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(yUpper + 1);
        yAxis.setTickUnit(yTics);
    }
}
