package com.student.appfx.controllers.input;

import com.student.appfx.cache.DataCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTypeFourController {

    private static VBox paneLog;

    private final Pattern patternAccuracy = Pattern.compile("\\d+\\.\\d+");
    private final Pattern patternSeed = Pattern.compile("-?\\d+");
    private final Pattern patternMultiplier = Pattern.compile("0\\.?\\d*");

    @FXML
    private TextField fieldAccuracy;
    @FXML
    private TextField fieldMul;
    @FXML
    private TextField seedMin;
    @FXML
    private TextField seedMax;
    @FXML
    private TextArea fieldAlert;

    public static void setPaneLog(VBox paneLog) {
        InputTypeFourController.paneLog = paneLog;
    }

    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void actionClose(ActionEvent actionEvent) {
        Label label = new Label("Отмена ввода данных");
        addRecordToLog(label);
        DataCache.clear();
        close(actionEvent);
    }

    public void actionSave(ActionEvent actionEvent) {
        String minSeed = seedMin.getText();
        if (Objects.equals(minSeed, "")) {
            minSeed = null;
        }
        String maxSeed = seedMax.getText();
        if (Objects.equals(maxSeed, "")) {
            maxSeed = null;
        }
        String accuracy = fieldAccuracy.getText();
        if (Objects.equals(accuracy, "")) {
            accuracy = null;
        }
        String multiplier = fieldMul.getText();
        if (Objects.equals(multiplier, "")) {
            multiplier = null;
        }
        if (checkSeedInput(minSeed, maxSeed, patternSeed) &
                checkAccuracyInput(accuracy, patternAccuracy) & checkMultiplierInput(multiplier, patternMultiplier)) {
            DataCache.experimentType = 4;
            DataCache.experts.add(7);
            DataCache.diapasons.add(3);
            DataCache.dataInput = true;
            close(actionEvent);
        } else {
            DataCache.clear();
        }
    }

    private boolean checkSeedInput(String min, String max, Pattern pattern) {
        if (min == null & max == null) {
            seedMax.setText("error");
            seedMin.setText("error");
            return false;
        }
        if(min == null | max == null) {
            if (min != null) {
                if (match(min, pattern)) {
                    DataCache.seeds.add(Integer.parseInt(min));
                    Label label = new Label("Введен только min seed");
                    addRecordToLog(label);
                    return true;
                } else {
                    seedMin.setText("error");
                    return false;
                }
            }
            if (match(max, pattern)) {
                DataCache.seeds.add(Integer.parseInt(max));
                Label label = new Label("Введен только max seed");
                addRecordToLog(label);
                return true;
            } else {
                seedMax.setText("error");
                return false;
            }
        }
        boolean minMatch = match(min, pattern);
        boolean maxMatch = match(max, pattern);
        if (minMatch & maxMatch) {
            if (Integer.parseInt(max) <= Integer.parseInt(min)) {
                fieldAlert.setText("max <= min: Проверьте правильность ввода. При исследовании одного значения заполните одно из полей");
                fieldAlert.setVisible(true);
                return false;
            }
            DataCache.seeds.add(Integer.parseInt(min));
            DataCache.seeds.add(Integer.parseInt(max));
            Label label = new Label("Диапазон seed введен");
            addRecordToLog(label);
            return true;
        } else {
            if (!minMatch) {
                seedMin.setText("error");
            }
            if (!maxMatch) {
                seedMax.setText("error");
            }
            return false;
        }
    }

    private boolean checkAccuracyInput(String value, Pattern pattern) {
        if (value == null) {
            fieldAccuracy.setText("error");
            return false;
        }
        if (match(value, pattern)) {
            DataCache.accuracy.add(Double.parseDouble(value));
            Label label = new Label("Значение точности введено");
            addRecordToLog(label);
            return true;
        } else {
            fieldAccuracy.setText("error");
            return false;
        }
    }

    private boolean checkMultiplierInput(String value, Pattern pattern) {
        if (value == null) {
            fieldMul.setText("error");
            return false;
        }
        if (match(value, pattern)) {
            DataCache.correctionMultiplier = Double.parseDouble(value);
            Label label = new Label("Значение множителя коррекции введено");
            addRecordToLog(label);
            return true;
        } else {
            fieldMul.setText("error");
            return false;
        }
    }

    private boolean match(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }
}
