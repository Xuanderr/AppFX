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

public class InputTypeOneController {

    private static VBox paneLog;

    private final Pattern pattern = Pattern.compile("\\d+\\.\\d+");

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
        DataCache.experimentType = 0;
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
        String min = accuracyMin.getText();
        if (Objects.equals(min, "")) {
            min = null;
        }
        String max = accuracyMax.getText();
        if (Objects.equals(max, "")) {
            max = null;
        }
        if (min == null & max == null) {
            accuracyMin.setText("error");
            accuracyMax.setText("error");
            return;
        }
        if(min == null | max == null) {
            if (min != null ) {
                if (checkValue(min)) {
                    DataCache.accuracy.add(Double.valueOf(min));
                    Label label = new Label("Введена только min точность");
                    addRecordToLog(label);
                    DataCache.dataInput = true;
                    if (checkInputDiapason()) {
                        Label l = new Label("Диапазон по умолчанию [1, 10]");
                        addRecordToLog(l);
                    }
                    close(actionEvent);
                } else {
                    accuracyMin.setText("error");
                }
                return;
            }
            if (checkValue(max)) {
                DataCache.accuracy.add(Double.valueOf(max));
                Label label = new Label("Введена только max точность");
                addRecordToLog(label);
                DataCache.dataInput = true;
                if (checkInputDiapason()) {
                    Label l = new Label("Диапазон по умолчанию [1, 10]");
                    addRecordToLog(l);
                }
                close(actionEvent);
            } else {
                accuracyMax.setText("error");
            }
            return;
        }
        boolean minMatch = checkValue(min);
        boolean maxMatch = checkValue(max);
        if (minMatch & maxMatch) {
            if (Double.parseDouble(max) <= Double.parseDouble(min)) {
                fieldAlert.setVisible(true);
                return;
            }
            DataCache.accuracy.add(Double.parseDouble(min));
            DataCache.accuracy.add(Double.parseDouble(max));
            Label label = new Label("Диапазон точности введен");
            addRecordToLog(label);
            DataCache.dataInput = true;
            if (checkInputDiapason()) {
                Label l = new Label("Диапазон по умолчанию [1, 10]");
                addRecordToLog(l);
            }
            close(actionEvent);
        } else {
            if (!minMatch) {
                accuracyMin.setText("error");
            }
            if (!maxMatch) {
                accuracyMax.setText("error");
            }
        }
    }

    private boolean checkValue(String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }

    private boolean checkInputDiapason() {
        boolean flag = DataCache.diapasons.isEmpty();
        if (flag) {
            DataCache.diapasons.add(1);
        }
        return flag;
    }
}
