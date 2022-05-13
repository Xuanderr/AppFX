package com.student.appfx.entities.expertExperiments;

import com.student.appfx.Utils;
import com.student.appfx.cache.DataForExpertExperiments;
import com.student.appfx.cache.DataForFeedbackExperiments;
import com.student.appfx.cache.MainCache;
import com.student.appfx.entities.MatrixOrVector;
import com.student.appfx.exceptions.MismatchColumnsStringsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Experiment {

    private final static Logger LOGGER = LoggerFactory.getLogger(Experiment.class.getName());

    private final Integer seed;
    private final int expertsAmount;
    private final double accuracy;
    private int min;
    private int max;
    private final ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
    private final XYChart.Data<Number,Number> data = new XYChart.Data<>();
    private final Correction correctionData = new Correction();

    public Experiment(int expertsAmount, double accuracy, int genType, Integer seed) {
        this.expertsAmount = expertsAmount;
        this.accuracy = accuracy;
        this.seed = Objects.requireNonNullElse(seed, 10);
        switch (genType) {
            case 1 -> {
                min = 0;
                max = 10;
            }
            case 2 -> {
                min = -1;
                max = 10;
            }
            case 3 -> {
                min = -10;
                max = 10;
            }
        }
    }

    public MatrixOrVector getExpertMatrix() {
        Random random = new Random();
        if(seed != null) {
            random.setSeed(seed);
        }
        MatrixOrVector matrix = new MatrixOrVector(expertsAmount, expertsAmount);
        for (int i = 0; i < expertsAmount; i++) {
            for (int j = 0; j < expertsAmount; j++) {
                if( i == j & DataForExpertExperiments.zeroOnDiagonal) {
                    matrix.setElement(i, j, 0);
                } else {
                    matrix.setElement(i, j, random.nextInt(max + 1 - min) + min);
                }
            }
        }
        return matrix;
    }

    public MatrixOrVector getExpertDefaultVector() {
        MatrixOrVector vector = new MatrixOrVector(expertsAmount, 1);
        for (int i = 0; i < expertsAmount; i++) {
            for (int j = 0; j < 1; j++) {
                vector.setElement(i, j, 1);
            }
        }
        return vector;
    }

    public void launch() {
        switch (DataForExpertExperiments.experimentType) {
            case 1 -> launchTypeOne();
            case 2 -> launchTypeTwo();
            case 3 -> launchTypeThree();
            case 4 -> launchTypeFour();
        }
    }

    private void launchTypeOne() {
        for (int i = 0; i < expertsAmount; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.valueOf(i+1));
            seriesList.add(series);
        }
        MatrixOrVector matrix = getExpertMatrix();
        MatrixOrVector vector = getExpertDefaultVector();
        norm(matrix, 0);
        //Utils.normalizerMatrixOrVector(matrix.get(), 0);
        try {
            int counter = 0;
            while (true) {
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(matrix, vector);
                norm(mul, 0);
                double current = Utils.currentAccuracy(vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }
                for (int i = 0; i < expertsAmount; i++) {
                    seriesList.get(i).getData().add(new XYChart.Data<>(counter, vector.getElement(i, 0)));
                }
                vector = mul;
                counter++;
            }
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launchTypeOne() ", e);
        }
    }

    private void launchTypeTwo() {
        MatrixOrVector matrix = getExpertMatrix();
        MatrixOrVector vector = getExpertDefaultVector();
        norm(matrix, 0);
        //Utils.normalizerMatrixOrVector(matrix.get(), 0);
        try {
            int counter = 0;
            while (true) {
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(matrix, vector);
                norm(mul, 0);
                double current = Utils.currentAccuracy(vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }
                vector = mul;
                counter++;
            }
            if (counter < 500) {
                MainCache.expertsRatings = vector;
            }
            data.setXValue(expertsAmount);
            data.setYValue(counter);
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launchTypeTwo() ", e);
        }
    }

    public void addingLaunchTypeTwo() {
        try {
            int counter = 0;
            while (true) {
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(DataForFeedbackExperiments.matrix, DataForFeedbackExperiments.vector);
                norm(mul, 0);
                double current = Utils.currentAccuracy(DataForFeedbackExperiments.vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }
                DataForFeedbackExperiments.vector = mul;
                counter++;
            }
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launchTypeTwo() ", e);
        }
    }

    private void launchTypeThree() {
        for (int i = 0; i < expertsAmount; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.valueOf(i+1));
            seriesList.add(series);
        }
        MatrixOrVector matrix = getExpertMatrix();
        MatrixOrVector vector = getExpertDefaultVector();
        norm(matrix, 0);
        //Utils.normalizerMatrixOrVector(matrix.get(), 0);
        try {
            int counter = 0;
            while (true) {
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(matrix, vector);
                norm(mul, 0);
                double current = Utils.currentAccuracy(vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }

                for (int i = 0; i < expertsAmount; i++) {
                    seriesList.get(i).getData().add(new XYChart.Data<>(counter, vector.getElement(i, 0)));
                }
                vector = mul;
                counter++;
            }
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launchTypeThree() ", e);
        }
    }

    private void launchTypeFour() {
        correctionData.init(expertsAmount);
        MatrixOrVector matrix = getExpertMatrix();
        MatrixOrVector vector = getExpertDefaultVector();
        norm(matrix, 0);
        //Utils.normalizerMatrixOrVector(matrix.get(), 0);
        withoutCorrection(matrix, vector);
        withCorrection(matrix, vector);
    }

    private void withoutCorrection(MatrixOrVector matrix, MatrixOrVector vector) {
        try {
            int counter = 0;
            while (true) {
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(matrix, vector);
                norm(mul, 0);
                double current = Utils.currentAccuracy(vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }

                for (int i = 0; i < expertsAmount; i++) {
                    correctionData.beforeAdd(i, new XYChart.Data<>(counter, vector.getElement(i, 0)));
                }
                vector = mul;
                counter++;
            }
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launchTypeFour().withoutCorrection() ", e);
        }
    }

    private void withCorrection(MatrixOrVector matrix, MatrixOrVector vector) {
        try {
            int counter = 0;
            for (int i = 0; i < expertsAmount; i++) {
                correctionData.afterAdd(i, new XYChart.Data<>(counter, 1));
            }
            while (true) {
                counter++;
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(matrix, vector);
                norm(mul, 0);
                List<CorrectionElement> corrList = Utils.correction(mul, DataForExpertExperiments.correctionMultiplier);
                double current = Utils.currentAccuracy(vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }
                if (!corrList.isEmpty()) {
                    for (CorrectionElement element: corrList) {
                        correctionData.afterAdd(element.getSeries(), new XYChart.Data<>(counter, element.getValue()));
                    }
                }
                for (int i = 0; i < expertsAmount; i++) {
                    correctionData.afterAdd(i, new XYChart.Data<>(counter, mul.getElement(i, 0)));
                }
                vector = mul;
            }
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launchTypeFour().withCorrection() ", e);
        }
    }

    private void norm(MatrixOrVector matrixOrVector, int axis) {
        if(DataForExpertExperiments.normType == 0) {
            Utils.normalizerMatrixOrVector(matrixOrVector.get(), axis);
        } else {
            Utils.singleSumReduction(matrixOrVector.get(), axis);
        }
    }

    public XYChart.Data<Number, Number> getData() {
        return data;
    }

    public ObservableList<XYChart.Series<Number, Number>> getSeriesList() {
        return seriesList;
    }

    public Correction getCorrectionData() {
        return correctionData;
    }
}
