package com.student.appfx.entities.informationExperiments;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class ChartWrapper {

    private final Stack<XYChart.Series<Number, Number>> cuttingSeriesMax = new Stack<>();
    private final Stack<XYChart.Series<Number, Number>> cuttingSeriesMin = new Stack<>();
    private LineChart<Number, Number> chart;
    private List<Splitter> ratingsExperts;

    public ChartWrapper(LineChart<Number, Number> chart, List<Splitter> ratingsExperts) {
        this.chart = chart;
        this.ratingsExperts = ratingsExperts;
    }

    public boolean addHeight() {
        List<Integer> indexes = getNeutralIndexes();
        if (indexes.isEmpty()) {
            return false;
        }
        Random random = new Random();
        ratingsExperts.get(indexes.get(random.nextInt(indexes.size()))).setLabel("h");
        return true;
    }

    public boolean addLow() {
        List<Integer> indexes = getNeutralIndexes();
        if (indexes.isEmpty()) {
            return false;
        }
        Random random = new Random();
        ratingsExperts.get(indexes.get(random.nextInt(indexes.size()))).setLabel("l");
        return true;
    }

    public boolean removeHeight() {
        for (Splitter ratingsExpert : ratingsExperts) {
            if (ratingsExpert.getLabel().equals("h")) {
                ratingsExpert.setLabel("n");
                return true;
            }
        }
        return false;
    }

    public boolean removeLow() {
        for (Splitter ratingsExpert : ratingsExperts) {
            if (ratingsExpert.getLabel().equals("l")) {
                ratingsExpert.setLabel("n");
                return true;
            }
        }
        return false;
    }

    /* Cut/Return tools */
    public boolean cutMax() {
        double max = 0;
        int maxIndex = 0;
        for (int i = 0; i < chart.getData().size(); i ++) {
            double tmp = (double) chart.getData().get(i).getData().get(0).getYValue();
            if (max < tmp) {
                max = tmp;
                maxIndex = i;
            }
        }
        if (chart.getData().isEmpty()) {
            return false;
        }
        switch (chart.getData().get(maxIndex).getName()) {
            case "neutral" -> removeSplitter("n", getRating(chart.getData().get(maxIndex)));
            case "height" -> removeSplitter("h", getRating(chart.getData().get(maxIndex)));
            case "low" -> removeSplitter("l", getRating(chart.getData().get(maxIndex)));
        }
        cuttingSeriesMax.push(chart.getData().get(maxIndex));
        chart.getData().remove(maxIndex);
        return true;
    }

    public boolean cutMin() {
        double min = 100;
        int minIndex = 0;
        for (int i = 0; i < chart.getData().size(); i ++) {
            double tmp = (double) chart.getData().get(i).getData().get(0).getYValue();
            if (min > tmp) {
                min = tmp;
                minIndex = i;
            }
        }
        if (chart.getData().isEmpty()) {
            return false;
        }
        switch (chart.getData().get(minIndex).getName()) {
            case "neutral" -> removeSplitter("n", getRating(chart.getData().get(minIndex)));
            case "height" -> removeSplitter("h", getRating(chart.getData().get(minIndex)));
            case "low" -> removeSplitter("l", getRating(chart.getData().get(minIndex)));
        }
        cuttingSeriesMin.push(chart.getData().get(minIndex));
        chart.getData().remove(minIndex);
        return true;
    }

    public boolean returnMax() {
        if (cuttingSeriesMax.isEmpty()) {
            return false;
        }
        switch (cuttingSeriesMax.peek().getName()) {
            case "neutral" -> ratingsExperts.add(new Splitter("n", getRating(cuttingSeriesMax.peek())));
            case "height" -> ratingsExperts.add(new Splitter("h", getRating(cuttingSeriesMax.peek())));
            case "low" -> ratingsExperts.add(new Splitter("l", getRating(cuttingSeriesMax.peek())));
        }
        Tooltip tooltip = (Tooltip) cuttingSeriesMax.peek().getNode().getProperties().get("javafx.scene.control.Tooltip");
        chart.getData().add(cuttingSeriesMax.pop());
        for (XYChart.Series<Number, Number> series: chart.getData()) {
            if (series.getNode().getProperties().get("javafx.scene.control.Tooltip") == null) {
                Tooltip.install(series.getNode(), tooltip);
                break;
            }
        }
        return true;
    }

    public boolean returnMin() {
        if (cuttingSeriesMin.isEmpty()) {
            return false;
        }
        switch (cuttingSeriesMin.peek().getName()) {
            case "neutral" -> ratingsExperts.add(new Splitter("n", getRating(cuttingSeriesMin.peek())));
            case "height" -> ratingsExperts.add(new Splitter("h", getRating(cuttingSeriesMin.peek())));
            case "low" -> ratingsExperts.add(new Splitter("l", getRating(cuttingSeriesMin.peek())));
        }
        Tooltip tooltip = (Tooltip) cuttingSeriesMin.peek().getNode().getProperties().get("javafx.scene.control.Tooltip");
        chart.getData().add(cuttingSeriesMin.pop());
        for (XYChart.Series<Number, Number> series: chart.getData()) {
            if (series.getNode().getProperties().get("javafx.scene.control.Tooltip") == null) {
                Tooltip.install(series.getNode(), tooltip);
                break;
            }
        }
        return true;
    }
    /*------------------------------*/

    public int getNeutral() {
        int res = 0;
        for (Splitter splitter: ratingsExperts) {
            if (splitter.getLabel().equals("n")) {
                res+=1;
            }
        }
        return res;
    }

    public int getHeight() {
        int res = 0;
        for (Splitter splitter: ratingsExperts) {
            if (splitter.getLabel().equals("h")) {
                res+=1;
            }
        }
        return res;
    }

    public int getLow() {
        int res = 0;
        for (Splitter splitter: ratingsExperts) {
            if (splitter.getLabel().equals("l")) {
                res+=1;
            }
        }
        return res;
    }

    public LineChart<Number, Number> getChart() {
        return chart;
    }

    public void setChart(LineChart<Number, Number> chart) {
        this.chart = chart;
    }

    public List<Splitter> getRatingsExperts() {
        return ratingsExperts;
    }

    public void setRatingsExperts(List<Splitter> ratingsExperts) {
        this.ratingsExperts = ratingsExperts;
    }

    private void removeSplitter(String label, double rating) {
        int index = 0;
        boolean flag = false;
        for (int i = 0; i < ratingsExperts.size(); i++) {
            if (ratingsExperts.get(i).getLabel().equals(label) & ratingsExperts.get(i).getRating() == rating) {
                index = i;
                flag = true;
                break;
            }
        }
        if (flag) {
            ratingsExperts.remove(index);
        }
    }

    private double getRating(XYChart.Series<Number, Number> series) {
        String str = ((Tooltip) series.getNode().getProperties().get("javafx.scene.control.Tooltip")).getText().split(" ")[1];
        return Double.parseDouble(str);
    }

    private List<Integer> getNeutralIndexes() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < ratingsExperts.size(); i++) {
            if (ratingsExperts.get(i).getLabel().equals("n")) {
                list.add(i);
            }
        }
        return list;
    }
}
