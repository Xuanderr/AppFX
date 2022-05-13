package com.student.appfx.entities.expertExperiments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;



public class Correction {

    private final ObservableList<XYChart.Series<Number, Number>> before = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Series<Number, Number>> after = FXCollections.observableArrayList();

    public void init(int amount) {
        for (int i = 0; i < amount; i++) {
            XYChart.Series<Number, Number> seriesBefore = new XYChart.Series<>();
            XYChart.Series<Number, Number> seriesAfter = new XYChart.Series<>();
            seriesBefore.setName(String.valueOf(i+1));
            seriesAfter.setName(String.valueOf(i+1));
            before.add(seriesBefore);
            after.add(seriesAfter);
        }
    }

    public void beforeAdd(int index, XYChart.Data<Number, Number> data) {
        before.get(index).getData().add(data);
    }

    public void afterAdd(int index, XYChart.Data<Number, Number> data) {
        after.get(index).getData().add(data);
    }

    public ObservableList<XYChart.Series<Number, Number>> getBefore() {
        return before;
    }

    public ObservableList<XYChart.Series<Number, Number>> getAfter() {
        return after;
    }
}
