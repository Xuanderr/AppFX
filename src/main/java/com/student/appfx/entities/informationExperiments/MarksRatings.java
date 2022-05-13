package com.student.appfx.entities.informationExperiments;

public class MarksRatings {
    private double mark;
    private double rating;

    public MarksRatings() { }

    public MarksRatings(double mark, double rating) {
        this.mark = mark;
        this.rating = rating;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getMark() {
        return mark;
    }

    public double getRating() {
        return rating;
    }
}
