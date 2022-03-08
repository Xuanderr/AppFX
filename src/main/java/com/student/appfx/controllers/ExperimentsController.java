package com.student.appfx.controllers;

import com.student.appfx.cache.DataCache;
import com.student.appfx.entities.Experiment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;

public class ExperimentsController {

    private static VBox paneLog;

    private Integer expStart;
    private Integer expEnd;
    private final int expStep = 1;

    private Double acStart;
    private Double acEnd;
    private Double acStep;

    public static void setPaneLog(VBox paneLog) {
        ExperimentsController.paneLog = paneLog;
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }

    public void doExperiments() {
        if (DataCache.experimentType == 0) {
            Label label = new Label("Отмена эксперимента");
            addRecordToLog(label);
            return;
        }
        if (!DataCache.dataInput) {
            Label label = new Label("Данные не введены");
            addRecordToLog(label);
            return;
        }
        init();
        DataCache.graphics.clear();
        switch (DataCache.experimentType) {
            case 1 -> launchTypeOne();
            case 2 -> launchTypeTwo();
            default -> def();
        }

    }

    private void launchTypeOne() {
        double acSize = ((acEnd == 0) ? acStart : acEnd);
        int expSize = ((expEnd == 0) ? expStart : expEnd);
        double xTics = 0.1;
        double yTics = 0.1;
        for (int d = 0; d < DataCache.diapasons.size(); d++) {
            for(double a = acStart; a <= acSize; a += acStep) {
                for (int e = expStart; e <= expSize; e += expStep) {
                    Experiment experiment = new Experiment(e, a, DataCache.diapasons.get(d));
                    ObservableList<XYChart.Series<Number, Number>> seriesList = experiment.launch();
                    LineChart<Number,Number> lineChart = createChart(seriesList, "iteration", "expert value");
                    if(d == 3) {
                        xTics = 50;
                    }
                    setAxisTicsAndLowerUpperBounds(seriesList, lineChart, xTics, yTics);
                    lineChart.setTitle(String.format(
                            "diapason: %s, accuracy: %f, experts: %d", genTypeToString(DataCache.diapasons.get(d)), a, e));
                    DataCache.graphics.add(lineChart);
                }
            }
        }
        Label label = new Label("Результаты получены");
        addRecordToLog(label);
    }

    private void launchTypeTwo() {
        double acSize = ((acEnd == 0) ? acStart : acEnd);
        int expSize = ((expEnd == 0) ? expStart : expEnd);
        for(double a = acStart; a <= acSize; a += acStep) {
            ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
            for (int i = 0; i < DataCache.diapasons.size(); i++) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(genTypeToString(DataCache.diapasons.get(i)));
                seriesList.add(series);
            }
            for (int e = expStart; e <= expSize; e += expStep) {
                for (int d = 0; d < DataCache.diapasons.size(); d++) {
                    Experiment experiment = new Experiment(e, a, d+1);
                    experiment.launch();
                    XYChart.Data<Number,Number> data = experiment.getData();
                    seriesList.get(d).getData().add(data);
                }
            }
            LineChart<Number,Number> lineChart = createChart(seriesList, "number of experts", "convergence");
            lineChart.setTitle(String.format("accuracy: %f", a));
            LineChart<Number,Number> chart1 = createAdditionalChart(seriesList.get(0), "number of experts", "convergence");
            chart1.setTitle(String.format("accuracy: %f", a));
            LineChart<Number,Number> chart2 = createAdditionalChart(seriesList.get(1), "number of experts", "convergence");
            chart2.setTitle(String.format("accuracy: %f", a));
            LineChart<Number,Number> chart3 = createAdditionalChart(seriesList.get(2), "number of experts", "convergence");
            chart3.setTitle(String.format("accuracy: %f", a));
            DataCache.graphics.add(lineChart);
            DataCache.graphics.add(chart1);
            DataCache.graphics.add(chart2);
            DataCache.graphics.add(chart3);
        }
        Label label = new Label("Результаты получены");
        addRecordToLog(label);
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
    }

    private LineChart<Number, Number> createChart(ObservableList<XYChart.Series<Number, Number>> seriesList,
                                                  String xLabel, String yLabel) {
        final NumberAxis xAxis = createAxis(xLabel);
        final NumberAxis yAxis = createAxis(yLabel);
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        //chart.setCreateSymbols(false);
        chart.setData(seriesList);
        return chart ;
    }

    private LineChart<Number, Number> createAdditionalChart(XYChart.Series<Number, Number> series, String xLabel, String yLabel) {
        final NumberAxis xAxis = createAxis(xLabel);
        final NumberAxis yAxis = createAxis(yLabel);
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
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


    private void def() {
        Label label = new Label("Тип не распознан");
        addRecordToLog(label);
    }
}
