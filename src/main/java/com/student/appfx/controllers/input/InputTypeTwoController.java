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

public class InputTypeTwoController {

    private static VBox paneLog;

    private final Pattern patternAccuracy = Pattern.compile("\\d+\\.\\d+");
    private final Pattern patternExpert = Pattern.compile("\\d+");

    @FXML
    private TextArea fieldAlert;

    @FXML
    private TextField accuracyMax;

    @FXML
    private TextField accuracyMin;

    @FXML
    private TextField expertMax;

    @FXML
    private TextField expertMin;


    public static void setPaneLog(VBox paneLog) {
        InputTypeTwoController.paneLog = paneLog;
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
        String minAc = accuracyMin.getText();
        if (Objects.equals(minAc, "")) {
            minAc = null;
        }
        String maxAC = accuracyMax.getText();
        if (Objects.equals(maxAC, "")) {
            maxAC = null;
        }
        String minEx = expertMin.getText();
        if (Objects.equals(minEx, "")) {
            minEx = null;
        }
        String maxEx = expertMax.getText();
        if (Objects.equals(maxEx, "")) {
            maxEx = null;
        }
        if (checkValues(minAc, maxAC, patternAccuracy,false) & checkValues(minEx, maxEx, patternExpert,true)) {
            close(actionEvent);
        }
    }

    private boolean checkValues(String min, String max, Pattern pattern, boolean experts) {
        if (min == null & max == null) {
            if (experts) {
                expertMax.setText("error");
                expertMin.setText("error");
            } else {
                accuracyMin.setText("error");
                accuracyMax.setText("error");
            }
            return false;
        }
        if(min == null | max == null) {
            if (min != null ) {
                if (match(min, pattern)) {
                    if (experts) {
                        DataCache.experts.add(Integer.parseInt(min));
                        Label label = new Label("Введено только min количество экспертов");
                        addRecordToLog(label);
                    } else {
                        DataCache.accuracy.add(Double.parseDouble(min));
                        Label label = new Label("Введена только min точность");
                        addRecordToLog(label);
                    }
                    DataCache.dataInput = true;
                    if (checkInputDiapason()) {
                        Label l = new Label("Диапазон по умолчанию [1, 10]");
                        addRecordToLog(l);
                    }
                    return true;
                } else {
                    if (experts) {
                        expertMin.setText("error");
                    } else {
                        accuracyMin.setText("error");
                    }
                    return false;
                }
            }
            if (match(max, pattern)) {
                if (experts) {
                    DataCache.experts.add(Integer.parseInt(max));
                    Label label = new Label("Введено только max количество экспертов");
                    addRecordToLog(label);
                } else {
                    DataCache.accuracy.add(Double.parseDouble(max));
                    Label label = new Label("Введена только max точность");
                    addRecordToLog(label);
                }
                DataCache.dataInput = true;
                if (checkInputDiapason()) {
                    Label l = new Label("Диапазон по умолчанию [1, 10]");
                    addRecordToLog(l);
                }
                return true;
            } else {
                if (experts) {
                    expertMax.setText("error");
                } else {
                    accuracyMax.setText("error");
                }
                return false;
            }
        }
        boolean minMatch = match(min, pattern);
        boolean maxMatch = match(max, pattern);
        if (minMatch & maxMatch) {
            if (Double.parseDouble(max) <= Double.parseDouble(min)) {
                fieldAlert.setVisible(true);
                return false;
            }
            if (experts) {
                DataCache.experts.add(Integer.parseInt(min));
                DataCache.experts.add(Integer.parseInt(max));
                Label label = new Label("Диапазон количества экспетов введен");
                addRecordToLog(label);
            } else {
                DataCache.accuracy.add(Double.parseDouble(min));
                DataCache.accuracy.add(Double.parseDouble(max));
                Label label = new Label("Диапазон точности введен");
                addRecordToLog(label);
            }
            DataCache.dataInput = true;
            if (checkInputDiapason()) {
                Label l = new Label("Диапазон по умолчанию [1, 10]");
                addRecordToLog(l);
            }
            return true;
        } else {
            if (!minMatch) {
                if (experts) {
                    expertMin.setText("error");
                } else {
                    accuracyMin.setText("error");
                }
            }
            if (!maxMatch) {
                if (experts) {
                    expertMax.setText("error");
                } else {
                    accuracyMax.setText("error");
                }
            }
            return false;
        }
    }

    private boolean match( String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }

    private boolean checkInputDiapason() {
        return DataCache.diapasons.isEmpty();
    }
}
