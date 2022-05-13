package com.student.appfx.entities.informationExperiments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Experiment {
    private final static Logger LOGGER = LoggerFactory.getLogger(Experiment.class.getName());

    private List<Splitter> experts;
    private double level;
    private int step;
    private int alg;
    private int type;
    private double epsilon;
    private final List<String> tooltipList = new ArrayList<>();
    private final ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
    private final List<XYChart.Data<Number, Number>> dataList = new ArrayList<>();

    public Experiment(List<Splitter> experts, double level, double epsilon, int alg, int type, int step) {
        this.experts = experts;
        this.level = level;
        this.step = step;
        this.epsilon = epsilon;
        this.alg = alg;
        this.type = type;
    }

    public Experiment(List<Splitter> experts, double level, double epsilon) {
        this.experts = experts;
        this.level = level;
        this.epsilon = epsilon;
    }

    public Experiment() { }

    public void launch() {
//        switch (type) {
//            case 1 -> launchOne();
//            case 2 -> launchTwo();
//        }
        innerLaunch();

    }

    private void innerLaunch() {
        List<MarksRatings> marks = new ArrayList<>();
        XYChart.Series<Number, Number> seriesMarksRatings = new XYChart.Series<>();
        seriesMarksRatings.setName("ratings/marks");
        for (Splitter splitter: experts) {
            MarksRatings marksRatings = new MarksRatings();
            switch (splitter.getLabel()) {
                case "n" -> {
                    marksRatings.setMark(getNeutralMark());
                    marksRatings.setRating(splitter.getRating());
                    marks.add(marksRatings);
                }
                case "h" -> {
                    marksRatings.setMark(getHeightMark());
                    marksRatings.setRating(splitter.getRating());
                    marks.add(marksRatings);
                }
                case "l" -> {
                    marksRatings.setMark(getLowMark());
                    marksRatings.setRating(splitter.getRating());
                    marks.add(marksRatings);
                }
            }
            seriesMarksRatings.getData().add(new XYChart.Data<>(marksRatings.getRating(), marksRatings.getMark()));
        }
        XYChart.Series<Number, Number> seriesMarkSimple = new XYChart.Series<>();
        seriesMarkSimple.setName("simple");
        seriesMarkSimple.getData().add(new XYChart.Data<>(1.1, getInfoRatingBySimpleAlg(marks)));

        XYChart.Series<Number, Number> seriesMarkModified = new XYChart.Series<>();
        seriesMarkModified.setName("modified");
        seriesMarkModified.getData().add(new XYChart.Data<>(1.2, getInfoRatingByModifiedAlg(marks)));
        seriesList.add(seriesMarksRatings);
        seriesList.add(seriesMarkSimple);
        seriesList.add(seriesMarkModified);
    }

    public void launchOne() {
        List<MarksRatings> marks = new ArrayList<>();
        for (Splitter splitter: experts) {
            switch (splitter.getLabel()) {
                case "n" -> marks.add(new MarksRatings(getNeutralMark(), splitter.getRating()));
                case "h" -> marks.add(new MarksRatings(getHeightMark(), splitter.getRating()));
                case "l" -> marks.add(new MarksRatings(getLowMark(), splitter.getRating()));
            }
        }
        double mark = 0;
        switch (alg) {
            case 1 -> mark = getInfoRatingBySimpleAlg(marks);
            case 2 -> mark = getInfoRatingByModifiedAlg(marks);
        }
        dataList.add(new XYChart.Data<>(step, level));
        dataList.add(new XYChart.Data<>(step, mark));
    }

    public void launchTwo() {
        List<MarksRatings> marks = new ArrayList<>();
        for (Splitter splitter: experts) {
            switch (splitter.getLabel()) {
                case "n" -> {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName("neutral");
                    tooltipList.add("Rating: " + splitter.getRating());
                    marks.add(new MarksRatings(getNeutralMark(), splitter.getRating()));
                    series.getData().add(new XYChart.Data<>(1, getNeutralMark()));
                    seriesList.add(series);
                }
                case "h" -> {
                    XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
                    series1.setName("height");
                    tooltipList.add("Rating: " + splitter.getRating());
                    marks.add(new MarksRatings(getHeightMark(), splitter.getRating()));
                    series1.getData().add(new XYChart.Data<>(1, getHeightMark()));
                    seriesList.add(series1);
                }
                case "l" -> {
                    XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
                    series2.setName("low");
                    tooltipList.add("Rating: " + splitter.getRating());
                    marks.add(new MarksRatings(getLowMark(), splitter.getRating()));
                    series2.getData().add(new XYChart.Data<>(1, getLowMark()));
                    seriesList.add(series2);
                }
            }
        }
        double mark = 0;
        switch (alg) {
            case 1 -> mark = getInfoRatingBySimpleAlg(marks);
            case 2 -> mark = getInfoRatingByModifiedAlg(marks);
        }
        for(XYChart.Series<Number, Number> series: seriesList) {
            series.getData().add(new XYChart.Data<>(2, mark));
        }
    }

    private double getNeutralMark() {
        double mark;
        Random random = new Random();
        if (random.nextBoolean()) {
            mark = level + Math.random() * (0.15 - 0) + 0;
        } else {
            mark = level - Math.random() * (0.15 - 0) + 0;
        }
        if (mark > 1) {
            mark = 1 - Math.random() * (1 - level - 0) + 0;
        }
        if (mark < 0) {
            mark = 0 + Math.random() * (level - 0) + 0;
        }
        return mark;
    }

    private double getHeightMark() {
        double mark;
        mark = level + (Math.random() * (0.5 - 0.3) + 0.3);
        if (mark > 1) {
//            if ((1 - level) > 0.2) {
//                mark = 1 - Math.random() * (0.1 - 0) + 0;
//            } else {
//                mark = 1;
//            }
            mark = 1;
        }
        return mark;
    }

    private double getLowMark() {
        double mark;
        mark = level - (Math.random() * (0.5 - 0.3) + 0.3);
        if (mark < 0) {
//            if (level > 0.2) {
//                mark = Math.random() * (0.1 - 0) + 0;
//            } else {
//                mark = 0;
//            }
            mark = 0;
        }
        return mark;
    }

    public double getInfoRatingBySimpleAlg(List<MarksRatings> marks) {
        double VW = 0;
        double W = 0;
        for (MarksRatings marksRatings: marks) {
            VW += marksRatings.getMark() * marksRatings.getRating();
            W += marksRatings.getRating();
        }
        return VW / W;
    }

    public double getInfoRatingByModifiedAlg(List<MarksRatings> marks) {
        double VU = 0;
        double U = 0;
        double max = getMaxRating(marks);
        for (MarksRatings marksRatings: marks) {
            double Ui = getU(marksRatings, max);
            VU += marksRatings.getMark() * Ui;
            U += Ui;
        }
        return VU / U;
    }

    private double getMaxRating(List<MarksRatings> marks) {
        double tmp = 0;
        for (MarksRatings marksRatings: marks) {
            if (tmp < marksRatings.getRating()) {
                tmp = marksRatings.getRating();
            }
        }
        return tmp;
    }

    private double getU(MarksRatings marksRatings, double max) {
        return (marksRatings.getRating()) / (max - marksRatings.getRating() + epsilon);
    }

    public ObservableList<XYChart.Series<Number, Number>> getSeriesList() {
        return seriesList;
    }

    public List<String> getTooltipList() {
        return tooltipList;
    }

    public List<XYChart.Data<Number, Number>> getDataList() {
        return dataList;
    }

    public double getLevel() {
        return level;
    }

    public int getAlg() {
        return alg;
    }

    public int getType() {
        return type;
    }

    public double getEpsilon() {
        return epsilon;
    }
}

