package com.student.appfx.entities.informationExperiments;

public class Splitter {

    private String label;
    private double rating;

    public Splitter(String label, double rating) {
        this.label = label;
        this.rating = rating;
    }

    public String getLabel() {
        return label;
    }

    public double getRating() {
        return rating;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
