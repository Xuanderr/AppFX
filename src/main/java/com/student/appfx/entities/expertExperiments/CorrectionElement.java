package com.student.appfx.entities.expertExperiments;

public class CorrectionElement {

    private int series;
    private double value;

    public CorrectionElement(int series, double value) {
        this.series = series;
        this.value = value;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
