package com.student.appfx.controllers;

import com.student.appfx.cache.DataCache;
import com.student.appfx.controllers.input.InputTypeFourController;
import com.student.appfx.controllers.input.InputTypeOneController;

import com.student.appfx.controllers.input.InputTypeThreeController;
import com.student.appfx.controllers.input.InputTypeTwoController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class.getName());

    @FXML
    private ToggleGroup groupExp;
    @FXML
    private Button btnResults;
    @FXML
    private Button btnLaunch;
    @FXML
    private VBox paneLog;

    private Stage inputStage;
    private Stage resultStage;
    private ObservableList<Toggle> toggles;


    @FXML
    private void initialize() {
        InputTypeOneController.setPaneLog(paneLog);
        InputTypeTwoController.setPaneLog(paneLog);
        InputTypeThreeController.setPaneLog(paneLog);
        InputTypeFourController.setPaneLog(paneLog);
        SaveController.setPaneLog(paneLog);
        toggles = groupExp.getToggles();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }

    public void radioButtonSelect(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof RadioButton)) {
            return;
        }

        switch (((RadioButton) source).getText()) {
            case "Эксперимент 1" -> typeOneInputForm(actionEvent);
            case "Эксперимент 2" -> typeTwoInputForm(actionEvent);
            case "Эксперимент 3" -> typeThreeInputForm(actionEvent);
            case "Эксперимент 4" -> typeFourInputForm(actionEvent);
            default -> {
                Label label = new Label("ОШИБКА: выбор эксперимента не удался: IN MainController--71");
                addRecordToLog(label);
            }
        }
    }

    private void typeOneInputForm(ActionEvent actionEvent) {
        DataCache.clear();
        Label label = new Label("Тип эксперимента 1");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/input/exp1-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.typeOneInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void typeTwoInputForm(ActionEvent actionEvent) {
        DataCache.clear();
        Label label = new Label("Тип эксперимента 2");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/input/exp2-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.typeTwoInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void typeThreeInputForm(ActionEvent actionEvent) {
        DataCache.clear();
        Label label = new Label("Тип эксперимента 3");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/input/exp3-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.typeThreeInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void typeFourInputForm(ActionEvent actionEvent) {
        DataCache.clear();
        Label label = new Label("Тип эксперимента 4");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/input/exp4-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.typeFourInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void setInputStage(ActionEvent actionEvent) {
        if (inputStage == null) {
            inputStage = new Stage();
            inputStage.setTitle("input");
            inputStage.centerOnScreen();
            inputStage.setResizable(false);
            inputStage.initModality(Modality.WINDOW_MODAL);
            inputStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        }
    }

    public void actionResult(ActionEvent actionEvent) {
        if (DataCache.graphics.isEmpty()) {
            Label label = new Label("Эксперимент не проводился");
            addRecordToLog(label);
            return;
        }
        if (resultStage == null) {
            resultStage = new Stage();
            resultStage.setTitle("result");
            resultStage.centerOnScreen();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/results.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.actionResult()", e);
        }

        resultStage.setScene(scene);
        resultStage.show();
    }

    public void actionLaunch(ActionEvent actionEvent) {
        if (!DataCache.dataInput) {
            Label label = new Label("Данные не введены");
            addRecordToLog(label);
            return;
        }
        btnLaunch.setDisable(true);
        btnResults.setDisable(true);
        for (Toggle toggle : toggles) {
            RadioButton radioButton = (RadioButton) toggle;
            radioButton.setDisable(true);
        }
        ExperimentsController experimentsController = new ExperimentsController();
        if(experimentsController.doExperiment()) {
            Label label = new Label("Данные получены");
            addRecordToLog(label);
        } else {
            Label label = new Label("Тип эксперимента не распознан");
            addRecordToLog(label);
        }
        DataCache.clear();
        for (Toggle toggle : toggles) {
            RadioButton radioButton = (RadioButton) toggle;
            radioButton.setSelected(false);
            radioButton.setDisable(false);
        }
        btnResults.setDisable(false);
        btnLaunch.setDisable(false);
    }
}
