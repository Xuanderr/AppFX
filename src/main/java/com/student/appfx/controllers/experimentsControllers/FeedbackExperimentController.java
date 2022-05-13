package com.student.appfx.controllers.experimentsControllers;

import com.student.appfx.Utils;
import com.student.appfx.cache.DataForFeedbackExperiments;
import com.student.appfx.cache.MainCache;

import com.student.appfx.entities.MatrixOrVector;
import com.student.appfx.entities.feedbackExperiments.Experiment;
import com.student.appfx.entities.feedbackExperiments.ExpertWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeedbackExperimentController {

    public void doExperiment() {
        MainCache.graphics.clear();
        launch();
    }

    private void launch() {
        com.student.appfx.entities.expertExperiments.Experiment exp =
                new com.student.appfx.entities.expertExperiments.Experiment(DataForFeedbackExperiments.experts, 0.001, 1, null);
        DataForFeedbackExperiments.matrix = DataForFeedbackExperiments.getExpertMatrix();
        DataForFeedbackExperiments.vector = DataForFeedbackExperiments.getExpertDefaultVector();
        Utils.normalizerMatrixOrVector(DataForFeedbackExperiments.matrix.get(), 0);
        exp.addingLaunchTypeTwo();
        List<ExpertWrapper> experts = new ArrayList<>();
        getLabels(experts);
        //getOrderLabels(experts);
        for (int i = 0; i < 50; i++) {
            Experiment experiment = new Experiment(DataForFeedbackExperiments.level, experts);
            experiment.launch();

            for (ExpertWrapper wrapper: experts) {
                wrapper.getRatings().getData().add(new XYChart.Data<>(i+1, wrapper.getRating()));
                wrapper.getDeviations().getData().add(new XYChart.Data<>(i+1, wrapper.getDeviation()));
            }
            DataForFeedbackExperiments.vector = DataForFeedbackExperiments.getExpertDefaultVector();
            exp.addingLaunchTypeTwo();
            for (int k = 0; k < experts.size(); k++) {
                experts.get(k).setRating(DataForFeedbackExperiments.vector.getElement(k, 0));
            }
        }
        ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
        int n = 0;
        int h = 0;
        int l = 0;
        for (ExpertWrapper wrapper: experts) {
            LineChart<Number, Number> lineChart = createChart("messages", null);
            lineChart.getData().addAll(wrapper.getRatings(), wrapper.getDeviations());
            lineChart.setTitle(String.format("level: %f, status: %s", DataForFeedbackExperiments.level, wrapper.getLabel()));
            MainCache.graphics.add(lineChart);
            if(wrapper.getLabel().equals("n") & n < 3) {
                seriesList.add(copySeries(wrapper.getRatings(), "n"));
                n +=1;
            }
            if(wrapper.getLabel().equals("h") & h < 3) {
                seriesList.add(copySeries(wrapper.getRatings(), "h"));
                h +=1;
            }
            if(wrapper.getLabel().equals("l") & l < 3) {
                seriesList.add(copySeries(wrapper.getRatings(), "l"));
                l +=1;
            }
        }
        LineChart<Number, Number> lineChart = createChart("messages", null);
        lineChart.setData(seriesList);
        MainCache.graphics.add(lineChart);
    }

    private void getLabels(List<ExpertWrapper> experts) {
        for (int i = 0; i < DataForFeedbackExperiments.experts; i++) {
            ExpertWrapper expertWrapper = new ExpertWrapper();
            expertWrapper.setRating(DataForFeedbackExperiments.vector.getElement(i, 0));
            if (i < DataForFeedbackExperiments.experts / 3) {
                expertWrapper.setLabel("n");
            } else {
                Random random = new Random();
                if (random.nextBoolean()) {
                    expertWrapper.setLabel("h");
                } else {
                    expertWrapper.setLabel("l");
                }
//                if (i < DataForFeedbackExperiments.experts / 3 * 2) {
//                    ExpertWrapper expertWrapper = new ExpertWrapper();
//                    expertWrapper.setRating(DataForFeedbackExperiments.vector.getElement(i, 0));
//                    expertWrapper.setLabel("h");
//                    experts.add(expertWrapper);
//                } else {
//                    ExpertWrapper expertWrapper = new ExpertWrapper();
//                    expertWrapper.setRating(DataForFeedbackExperiments.vector.getElement(i, 0));
//                    expertWrapper.setLabel("l");
//                    experts.add(expertWrapper);
//                }
            }
            experts.add(expertWrapper);
        }
    }

    private void getOrderLabels(List<ExpertWrapper> experts) {
        int tmp = DataForFeedbackExperiments.experts / 3;
        for (int i = 0; i < DataForFeedbackExperiments.experts; i++) {
            ExpertWrapper expertWrapper = new ExpertWrapper();
            expertWrapper.setRating(DataForFeedbackExperiments.vector.getElement(i, 0));
            if (counter(DataForFeedbackExperiments.vector, DataForFeedbackExperiments.vector.getElement(i, 0)) <= tmp) {
                expertWrapper.setLabel("n");
            } else {
                Random random = new Random();
                if (random.nextBoolean()) {
                    expertWrapper.setLabel("h");
                } else {
                    expertWrapper.setLabel("l");
                }
            }
            experts.add(expertWrapper);
        }
    }

    private int counter(MatrixOrVector vector, double value) {
        int res = 0;
        for (int i = 0; i < vector.getStringAmount(); i++) {
            if (vector.getElement(i, 0) > value) {
                res +=1;
            }
        }
        return res;
    }

    private LineChart<Number, Number> createChart(String xLabel, String yLabel) {
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
        return chart;
    }

    private XYChart.Series<Number, Number> copySeries(XYChart.Series<Number, Number> series, String name) {
        XYChart.Series<Number, Number> res = new XYChart.Series<>();
        for (XYChart.Data<Number, Number> data: series.getData()) {
            res.getData().add(new XYChart.Data<>(data.getXValue(), data.getYValue()));
        }
        res.setName(name);
        return res;
    }
}
