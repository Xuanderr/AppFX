package com.student.appfx.controllers;

import com.student.appfx.cache.DataCache;
import com.student.appfx.controllers.input.InputTypeOneController;

import com.student.appfx.controllers.input.InputTypeTwoController;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class MainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class.getName());

    @FXML
    private RadioButton rbOne;

    @FXML
    private RadioButton rbTwo;

    @FXML
    private Button btnResults;

    @FXML
    private Button btnLaunch;

    @FXML
    private VBox paneLog;

    private Stage inputStage;

    private Stage resultStage;

    @FXML
    private void initialize() {
        InputTypeOneController.setPaneLog(paneLog);
        InputTypeTwoController.setPaneLog(paneLog);
        ExperimentsController.setPaneLog(paneLog);
        SaveController.setPaneLog(paneLog);
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
            case "Эксперимент 1" -> exp1(actionEvent);
            case "Эксперимент 2" -> exp2(actionEvent) ;
        }
    }

    private void exp1(ActionEvent actionEvent) {
        DataCache.clear();
        Label label = new Label("Тип 1");
        addRecordToLog(label);
        DataCache.experimentType = 1;
        DataCache.experts.add(7);
        if (inputStage == null) {
            inputStage = new Stage();
            inputStage.setTitle("input");
            inputStage.centerOnScreen();
            inputStage.setResizable(false);
            inputStage.initModality(Modality.WINDOW_MODAL);
            inputStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/input/exp1-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            throw new IOException();
        } catch (IOException e) {
            LOGGER.info("IN MainController.exp1()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void exp2(ActionEvent actionEvent) {
        DataCache.clear();
        Label label = new Label("Тип 2");
        addRecordToLog(label);
        DataCache.diapasons.add(1);
        DataCache.diapasons.add(2);
        DataCache.diapasons.add(3);
        DataCache.experimentType = 2;
        if (inputStage == null) {
            inputStage = new Stage();
            inputStage.setTitle("input");
            inputStage.centerOnScreen();
            inputStage.setResizable(false);
            inputStage.initModality(Modality.WINDOW_MODAL);
            inputStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/input/exp2-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.exp2()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
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
        rbOne.setDisable(true);
        rbTwo.setDisable(true);
        ExperimentsController experimentsController = new ExperimentsController();
        experimentsController.doExperiments();
        rbTwo.setDisable(false);
        rbOne.setDisable(false);
        btnResults.setDisable(false);
        btnLaunch.setDisable(false);
    }
}
