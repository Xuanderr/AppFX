package com.student.appfx.entities.feedbackExperiments;

import com.student.appfx.cache.DataForFeedbackExperiments;
import com.student.appfx.entities.informationExperiments.MarksRatings;
import com.student.appfx.entities.informationExperiments.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Experiment {

    private double level;
    private List<ExpertWrapper> experts;
    private double mark;

    public Experiment(double level, List<ExpertWrapper> experts) {
        this.experts = experts;
        this.level = level;
    }

    public void launch() {
        for (ExpertWrapper wrapper: experts) {
            switch (wrapper.getLabel()) {
                case "n" -> wrapper.setMark(getNeutralMark());
                case "h" -> wrapper.setMark(getHeightMark());
                case "l" -> wrapper.setMark(getLowMark());
            }
        }
        mark = getInfoRatingByModifiedAlg(experts);
        //setAbsoluteDeviation();
        setRelativeDeviation();
        feedback();
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
            mark = 1;
        }
        return mark;
    }

    private double getLowMark() {
        double mark;
        mark = level - (Math.random() * (0.5 - 0.3) + 0.3);
        if (mark < 0) {
            mark = 0;
        }
        return mark;
    }

    private double getInfoRatingByModifiedAlg(List<ExpertWrapper> wrappers) {
        double VU = 0;
        double U = 0;
        double max = getMaxRating(wrappers);
        for (ExpertWrapper wrapper: wrappers) {
            double Ui = getU(wrapper, max);
            VU += wrapper.getMark() * Ui;
            U += Ui;
        }
        return VU / U;
    }

    private double getMaxRating(List<ExpertWrapper> wrappers) {
        double tmp = 0;
        for (ExpertWrapper wrapper: wrappers) {
            if (tmp < wrapper.getRating()) {
                tmp = wrapper.getRating();
            }
        }
        return tmp;
    }

    // EPSILON
    private double getU(ExpertWrapper wrapper, double max) {
        return (wrapper.getRating()) / (max - wrapper.getRating() + 0.001); //epsilon
    }

    private void setAbsoluteDeviation() {
        for (ExpertWrapper wrapper: experts) {
            wrapper.setDeviation(Math.abs(wrapper.getMark() - mark));
        }
    }

    private void setRelativeDeviation() {
        for (ExpertWrapper wrapper: experts) {
            wrapper.setDeviation(Math.abs((wrapper.getMark() / mark) - 1));
        }
    }

    private void feedback() {
        for (int i = 0; i < experts.size(); i++) {
            for (int j = 0; j < DataForFeedbackExperiments.matrix.getColumnAmount(); j++) {
                DataForFeedbackExperiments.matrix.setElement(i, j,
                        getNewElement(DataForFeedbackExperiments.matrix.getElement(i, j), experts.get(i).getDeviation()));
            }
        }
    }

    // ALPHA
    private double getNewElement(double element, double deviation) {
        return element * (1 - 0.1 * deviation);
    }

    public List<ExpertWrapper> getExperts() {
        return experts;
    }

    public double getMark() {
        return mark;
    }
}
