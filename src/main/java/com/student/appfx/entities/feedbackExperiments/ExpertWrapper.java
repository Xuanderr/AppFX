package com.student.appfx.entities.feedbackExperiments;

import javafx.scene.chart.XYChart;

public class ExpertWrapper {

    private double rating;
    private String label;
    private double mark;
    private double deviation;
    private XYChart.Series<Number, Number> ratings;
    private XYChart.Series<Number, Number> deviations;


    public ExpertWrapper() {
        this.ratings = new XYChart.Series<>();
        this.ratings.setName("ratings");
        this.deviations = new XYChart.Series<>();
        this.deviations.setName("deviations");
    }

    public ExpertWrapper(double rating, String label, double mark, double deviation) {
        this.ratings = new XYChart.Series<>();
        this.ratings.setName("ratings");
        this.deviations = new XYChart.Series<>();
        this.deviations.setName("deviations");
        this.rating = rating;
        this.label = label;
        this.mark = mark;
        this.deviation = deviation;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public XYChart.Series<Number, Number> getRatings() {
        return ratings;
    }

    public void setRatings(XYChart.Series<Number, Number> ratings) {
        this.ratings = ratings;
    }

    public XYChart.Series<Number, Number> getDeviations() {
        return deviations;
    }

    public void setDeviations(XYChart.Series<Number, Number> deviations) {
        this.deviations = deviations;
    }
}
