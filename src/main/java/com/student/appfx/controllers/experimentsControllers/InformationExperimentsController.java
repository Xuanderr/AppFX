package com.student.appfx.controllers.experimentsControllers;
import com.student.appfx.cache.DataForInformationExperiments;
import com.student.appfx.cache.MainCache;
import com.student.appfx.entities.informationExperiments.Experiment;
import com.student.appfx.entities.informationExperiments.MarksRatings;
import com.student.appfx.entities.informationExperiments.Splitter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;


public class InformationExperimentsController {

    public void doExperiment() {
        MainCache.graphics.clear();
        launch();
    }

    public LineChart<Number, Number> recalculation(List<Splitter> experts, double outLevel, double epsilon, int alg, int type) {
        switch (type) {
            case 1 -> {
                int step = 1;
                ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
                for (double level = 0; level <= 1; level += 0.01) {
                    Experiment experiment =
                            new Experiment(experts, level, epsilon, alg, type, step);
                    experiment.launchOne();
                    getExperimentResult(experiment, seriesList, true);
                    step++;
                }
                LineChart<Number, Number> lineChart1 = createChart(seriesList, null, null);
                lineChart1.setTitle(String.format("type: %d, alg: %s, epsilon: %e", type, getStringAlg(alg), epsilon));
                return lineChart1;
            }
            case 2 -> {
                Experiment experiment =
                        new Experiment(experts, outLevel, epsilon, alg, type, 1);
                experiment.launchTwo();
                LineChart<Number, Number> lineChart2 = createChart(experiment.getSeriesList(), "steps", "marks");
                for (int i = 0; i < experiment.getSeriesList().size(); i++) {
                    Tooltip.install(
                            experiment.getSeriesList().get(i).getNode(),
                            new Tooltip(experiment.getTooltipList().get(i)));
                }
                lineChart2.setTitle(String.format("type: %d, alg: %s, epsilon: %e, level: %f",
                        experiment.getType(), getStringAlg(experiment.getAlg()),
                        experiment.getEpsilon(), experiment.getLevel()));
                return lineChart2;
            }
            default -> {
                return null;
            }
        }
    }

    public void recalculationMark(LineChart<Number, Number> chart, int alg) {
        List<MarksRatings> marksRatings = new ArrayList<>();
        for (XYChart.Series<Number, Number> series: chart.getData()) {
            marksRatings.add(new MarksRatings((double) series.getData().get(0).getYValue(), getRating(series)));
        }
        double mark = 0;
        Experiment experiment = new Experiment();
        mark = switch (alg) {
            case 1 -> experiment.getInfoRatingBySimpleAlg(marksRatings);
            case 2 -> experiment.getInfoRatingByModifiedAlg(marksRatings);
            default -> mark;
        };
        for (XYChart.Series<Number, Number> series: chart.getData()) {
            series.getData().get(1).setYValue(mark);
        }
    }

    private double getRating(XYChart.Series<Number, Number> series) {
        String str = ((Tooltip) series.getNode().getProperties().get("javafx.scene.control.Tooltip")).getText().split(" ")[1];
        return Double.parseDouble(str);
    }

    private void launch() {
        double epsilon = 0.01;
        //double epsilon = 0.000001;
        //double epsilon = 0.0000001;
        while (DataForInformationExperiments.containsGood()) {
            Experiment experiment = new Experiment(
                    DataForInformationExperiments.expertsRatings,
                    DataForInformationExperiments.level, epsilon);
            experiment.launch();
            LineChart<Number, Number> lineChart = createChart(experiment.getSeriesList(), "ratings", "marks");
            lineChart.setTitle(String.format("level: %f, good: %d, bad: %d, epsilon: %e",
                    DataForInformationExperiments.level,
                    DataForInformationExperiments.getGood(),
                    DataForInformationExperiments.getBad(),
                    epsilon));
            MainCache.graphics.add(lineChart);
            DataForInformationExperiments.addBad();
        }
    }

//    private void launch() {
//        for (int type = 1; type <= 2; type++) {
//            for (int alg = 1; alg <= 2; alg++) {
//                for (double epsilon = 0.00001; epsilon >= 0.0000001; epsilon /= 10) {
//                    int step = 1;
//                    ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
//                    for (double level = 0; level <= 1; level += 0.01) {
//                        Experiment experiment =
//                                new Experiment(DataForInformationExperiments.expertsRatings, level, epsilon, alg, type, step);
//                        experiment.launch();
//                        getExperimentResult(experiment, seriesList, false);
//                        step++;
//                    }
//                    if (type == 1) {
//                        LineChart<Number, Number> lineChart = createChart(seriesList, null, null);
//                        lineChart.setTitle(String.format("type: %d, alg: %s, epsilon: %e", type, getStringAlg(alg), epsilon));
//                        MainCache.graphics.add(lineChart);
//                    }
//                }
//            }
//        }
//    }

    private void getExperimentResult(Experiment experiment, ObservableList<XYChart.Series<Number, Number>> seriesList,
                                     boolean isRecalculation) {
        if (experiment.getType() == 1) {
            if (seriesList.isEmpty()) {
                XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
                series1.setName("real");
                seriesList.add(series1);
                XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
                series2.setName("marked");
                seriesList.add(series2);
            }

            seriesList.get(0).getData().add(experiment.getDataList().get(0));
            seriesList.get(1).getData().add(experiment.getDataList().get(1));
        } else {
            if (isRecalculation) {
                return;
            }
            LineChart<Number,Number> lineChart = createChart(experiment.getSeriesList(), "steps", "marks");
            for (int i = 0; i < experiment.getSeriesList().size(); i++) {
                Tooltip.install(
                        experiment.getSeriesList().get(i).getNode(),
                        new Tooltip(experiment.getTooltipList().get(i)));
            }
            lineChart.setTitle(String.format("type: %d, alg: %s, epsilon: %e, level: %f",
                                    experiment.getType(), getStringAlg(experiment.getAlg()),
                                    experiment.getEpsilon(), experiment.getLevel()));
            MainCache.graphics.add(lineChart);
        }
    }

    private String getStringAlg(int type) {
        return switch (type) {
            case 1 -> "Simple";
            case 2 -> "Modified";
            default -> "Undefined";
        };
    }

    private LineChart<Number, Number> createChart(ObservableList<XYChart.Series<Number, Number>> seriesList,
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

}
