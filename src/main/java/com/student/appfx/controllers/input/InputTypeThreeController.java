package com.student.appfx.controllers.input;

import com.student.appfx.cache.DataCache;
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

public class InputTypeThreeController {

    private static VBox paneLog;

    private final Pattern patternAccuracy = Pattern.compile("\\d+\\.\\d+");
    private final Pattern patternSeed = Pattern.compile("-?\\d+");

    @FXML
    private CheckBox checkBoxOne;
    @FXML
    private CheckBox checkBoxTwo;
    @FXML
    private CheckBox checkBoxThree;
    @FXML
    private TextField seedMin;
    @FXML
    private TextField seedMax;
    @FXML
    private TextArea fieldAlert;
    @FXML
    private TextField fieldAccuracy;

    public static void setPaneLog(VBox paneLog) {
        InputTypeThreeController.paneLog = paneLog;
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
        if (checkBoxOne.isSelected()) {
            DataCache.diapasons.add(1);
        }
        if (checkBoxTwo.isSelected()) {
            DataCache.diapasons.add(2);
        }
        if (checkBoxThree.isSelected()) {
            DataCache.diapasons.add(3);
        }
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
        if (checkSeedInput(minSeed, maxSeed, patternSeed) &
                checkAccuracyInput(accuracy, patternAccuracy) & checkDiapasonsInput()) {
            DataCache.experimentType = 3;
            DataCache.experts.add(7);
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

    private boolean checkDiapasonsInput() {
        if (DataCache.diapasons.isEmpty()) {
            fieldAlert.setText("Диапазоны генерации не заданы");
            fieldAlert.setVisible(true);
            return false;
        }
        Label label = new Label();
        String d1, d2, d3;
        switch (DataCache.diapasons.size()) {
            case 1 -> {
                d1 = genTypeToString(DataCache.diapasons.get(0));
                if (d1.equals("unknown")) {
                    DataCache.diapasons.clear();
                    DataCache.diapasons.add(3);
                    label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--165");
                    addRecordToLog(label);
                    return true;
                }
                label.setText("Диапазоны: " + d1);
                addRecordToLog(label);
                return true;
            }
            case 2 -> {
                d1 = genTypeToString(DataCache.diapasons.get(0));
                d2 = genTypeToString(DataCache.diapasons.get(1));
                if (d1.equals("unknown") | d2.equals("unknown")) {
                    DataCache.diapasons.clear();
                    DataCache.diapasons.add(3);
                    label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--179");
                    addRecordToLog(label);
                    return true;
                }
                label.setText("Диапазоны: " + d1 + ", " + d2);
                addRecordToLog(label);
                return true;
            }
            case 3 -> {
                d1 = genTypeToString(DataCache.diapasons.get(0));
                d2 = genTypeToString(DataCache.diapasons.get(1));
                d3 = genTypeToString(DataCache.diapasons.get(2));
                if (d1.equals("unknown") | d2.equals("unknown") | d3.equals("unknown")) {
                    DataCache.diapasons.clear();
                    DataCache.diapasons.add(3);
                    label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--193");
                    addRecordToLog(label);
                    return true;
                }
                label.setText("Диапазоны: " + d1 + ", " + d2 + ", " + d3);
                addRecordToLog(label);
                return true;
            }
            default -> {
                DataCache.diapasons.clear();
                DataCache.diapasons.add(3);
                label.setText("Предоставлен диапазон по умолчанию [-10, 10]. IN InputTypeThreeController--203");
                addRecordToLog(label);
                return true;
            }
        }
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

    private boolean match(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }
}





















