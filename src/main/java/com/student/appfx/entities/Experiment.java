package com.student.appfx.entities;

import com.student.appfx.Utils;
import com.student.appfx.cache.DataCache;
import com.student.appfx.exceptions.MismatchColumnsStringsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Random;

public class Experiment {

    private final static Logger LOGGER = LoggerFactory.getLogger(Experiment.class.getName());

    private int expertsAmount;
    private double accuracy;
    private int min;
    private int max;
    private final ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
    private XYChart.Data<Number,Number> data = new XYChart.Data<>();

    public Experiment(int expertsAmount, double accuracy, int genType) {
        this.expertsAmount = expertsAmount;
        this.accuracy = accuracy;
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
        MatrixOrVector matrix = new MatrixOrVector(expertsAmount, expertsAmount);
        for (int i = 0; i < expertsAmount; i++) {
            for (int j = 0; j < expertsAmount; j++) {
                matrix.setElement(i, j, random.nextInt(max + 1 - min) + min);
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

    public ObservableList<XYChart.Series<Number, Number>> launch() {
        if(DataCache.experimentType == 1) {
            for (int i = 0; i < expertsAmount; i++) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(String.valueOf(i+1));
                seriesList.add(series);
            }
        }
        MatrixOrVector matrix = getExpertMatrix();
        MatrixOrVector vector = getExpertDefaultVector();
        //System.out.println("Matrix\n" + matrix);
        Utils.normalizerMatrixOrVector(matrix.get(), 0);
        //System.out.println("Normalize Matrix\n" + matrix);
        //System.out.println("Vector default\n" + vector);
        try {
            int counter = 0;
            while (true) {
                if (counter > 500) {
                    break;
                }
                MatrixOrVector mul = Utils.multiply(matrix, vector);
                Utils.normalizerMatrixOrVector(mul.get(), 0);
                double current = Utils.currentAccuracy(vector.get(), mul.get());
                if (current <= accuracy) {
                    break;
                }
                if (DataCache.experimentType == 1) {
                    for (int i = 0; i < expertsAmount; i++) {
                        seriesList.get(i).getData().add(new XYChart.Data<Number, Number>(counter, vector.getElement(i, 0)));
                    }
                }
                //System.out.println("Vector after " + (counter + 1) + " iterations");
                //System.out.println(mul);
                vector = mul;
                counter++;
            }
            if (DataCache.experimentType == 2) {
                setData(new XYChart.Data<Number, Number>(expertsAmount, counter));
            }
            return seriesList;
        } catch (MismatchColumnsStringsException e) {
            LOGGER.info("IN Experiment.launch ", e);
            return null;
        }
    }

    public void setData(XYChart.Data<Number, Number> data) {
        this.data = data;
    }

    public XYChart.Data<Number, Number> getData() {
        return data;
    }

    public int getExpertsAmount() {
        return expertsAmount;
    }

    public void setExpertsAmount(int expertsAmount) {
        this.expertsAmount = expertsAmount;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
