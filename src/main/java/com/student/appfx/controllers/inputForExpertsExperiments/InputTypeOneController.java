package com.student.appfx.controllers.inputForExpertsExperiments;

import com.student.appfx.cache.DataForExpertExperiments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTypeOneController {

    private static VBox paneLog;

    private final Pattern patternAccuracy = Pattern.compile("\\d+\\.\\d+");

    @FXML
    private TextArea fieldAlert;
    @FXML
    private TextField accuracyMin;
    @FXML
    private TextField accuracyMax;
    @FXML
    private CheckBox checkBoxOne;
    @FXML
    private CheckBox checkBoxTwo;
    @FXML
    private CheckBox checkBoxThree;

    public static void setPaneLog(VBox paneLog) {
        InputTypeOneController.paneLog = paneLog;
    }

    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void actionClose(ActionEvent actionEvent) {
        Label label = new Label("Отмена ввода данных");
        addRecordToLog(label);
        DataForExpertExperiments.clear();
        close(actionEvent);
    }

    public void actionSave(ActionEvent actionEvent) {
        if (checkBoxOne.isSelected()) {
            DataForExpertExperiments.diapasons.add(1);
        }
        if (checkBoxTwo.isSelected()) {
            DataForExpertExperiments.diapasons.add(2);
        }
        if (checkBoxThree.isSelected()) {
            DataForExpertExperiments.diapasons.add(3);
        }
        String minAc = accuracyMin.getText();
        if (Objects.equals(minAc, "")) {
            minAc = null;
        }
        String maxAc = accuracyMax.getText();
        if (Objects.equals(maxAc, "")) {
            maxAc = null;
        }
        if (checkAccuracyInput(minAc, maxAc, patternAccuracy) & checkDiapasonsInput()) {
            DataForExpertExperiments.experimentType = 1;
            DataForExpertExperiments.experts.add(7);
            DataForExpertExperiments.dataInput = true;
            close(actionEvent);
        } else {
            DataForExpertExperiments.clear();
        }
    }

    private boolean checkAccuracyInput(String min, String max, Pattern pattern) {
        if (min == null & max == null) {
            accuracyMax.setText("error");
            accuracyMin.setText("error");
            return false;
        }
        if(min == null | max == null) {
            if (min != null) {
                if (match(min, pattern)) {
                    DataForExpertExperiments.accuracy.add(Double.valueOf(min));
                    Label label = new Label("Введена только min точность");
                    addRecordToLog(label);
                    return true;
                } else {
                    accuracyMin.setText("error");
                    return false;
                }
            }
            if (match(max, pattern)) {
                DataForExpertExperiments.accuracy.add(Double.valueOf(max));
                Label label = new Label("Введена только max точность");
                addRecordToLog(label);
                return true;
            } else {
                accuracyMax.setText("error");
                return false;
            }
        }
        boolean minMatch = match(min, pattern);
        boolean maxMatch = match(max, pattern);
        if (minMatch & maxMatch) {
            if (Double.parseDouble(max) <= Double.parseDouble(min)) {
                fieldAlert.setText("max <= min: Проверьте правильность ввода. При исследовании одного значения заполните одно из полей");
                fieldAlert.setVisible(true);
                return false;
            }
            DataForExpertExperiments.accuracy.add(Double.parseDouble(min));
            DataForExpertExperiments.accuracy.add(Double.parseDouble(max));
            Label label = new Label("Диапазон точности введен");
            addRecordToLog(label);
            return true;
        } else {
            if (!minMatch) {
                accuracyMin.setText("error");
            }
            if (!maxMatch) {
                accuracyMax.setText("error");
            }
            return false;
        }
    }

    private boolean checkDiapasonsInput() {
        if (DataForExpertExperiments.diapasons.isEmpty()) {
            fieldAlert.setText("Диапазоны генерации не заданы");
            fieldAlert.setVisible(true);
            return false;
        }
        Label label = new Label();
        String d1, d2, d3;
        switch (DataForExpertExperiments.diapasons.size()) {
            case 1 -> {
                d1 = genTypeToString(DataForExpertExperiments.diapasons.get(0));
                if (d1.equals("unknown")) {
                    DataForExpertExperiments.diapasons.clear();
                    DataForExpertExperiments.diapasons.add(3);
                    label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--165");
                    addRecordToLog(label);
                    return true;
                }
                label.setText("Диапазоны: " + d1);
                addRecordToLog(label);
                return true;
            }
            case 2 -> {
                d1 = genTypeToString(DataForExpertExperiments.diapasons.get(0));
                d2 = genTypeToString(DataForExpertExperiments.diapasons.get(1));
                if (d1.equals("unknown") | d2.equals("unknown")) {
                    DataForExpertExperiments.diapasons.clear();
                    DataForExpertExperiments.diapasons.add(3);
                    label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--179");
                    addRecordToLog(label);
                    return true;
                }
                label.setText("Диапазоны: " + d1 + ", " + d2);
                addRecordToLog(label);
                return true;
            }
            case 3 -> {
                d1 = genTypeToString(DataForExpertExperiments.diapasons.get(0));
                d2 = genTypeToString(DataForExpertExperiments.diapasons.get(1));
                d3 = genTypeToString(DataForExpertExperiments.diapasons.get(2));
                if (d1.equals("unknown") | d2.equals("unknown") | d3.equals("unknown")) {
                    DataForExpertExperiments.diapasons.clear();
                    DataForExpertExperiments.diapasons.add(3);
                    label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--193");
                    addRecordToLog(label);
                    return true;
                }
                label.setText("Диапазоны: " + d1 + ", " + d2 + ", " + d3);
                addRecordToLog(label);
                return true;
            }
            default -> {
                DataForExpertExperiments.diapasons.clear();
                DataForExpertExperiments.diapasons.add(3);
                label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--203");
                addRecordToLog(label);
                return true;
            }
        }
    }

    private boolean match(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private String genTypeToString(int genType) {
        switch (genType) {
            case 1 -> {
                return "[0, 10]";
            }
            case 2 -> {
                return "[-1, 10]";
            }
            case 3 -> {
                return "[-10, 10]";
            }
        }
        return "unknown";
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }
}
