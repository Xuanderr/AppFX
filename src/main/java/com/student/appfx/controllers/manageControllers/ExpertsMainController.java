package com.student.appfx.controllers.manageControllers;

import com.student.appfx.cache.DataForExpertExperiments;
import com.student.appfx.cache.MainCache;
import com.student.appfx.controllers.ResultController;
import com.student.appfx.controllers.experimentsControllers.ExpertExperimentsController;
import com.student.appfx.controllers.SaveController;
import com.student.appfx.controllers.inputForExpertsExperiments.InputTypeFourController;
import com.student.appfx.controllers.inputForExpertsExperiments.InputTypeOneController;

import com.student.appfx.controllers.inputForExpertsExperiments.InputTypeThreeController;
import com.student.appfx.controllers.inputForExpertsExperiments.InputTypeTwoController;
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

public class ExpertsMainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExpertsMainController.class.getName());

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
                Label label = new Label("ОШИБКА: выбор эксперимента не удался: IN ExpertsMainController");
                addRecordToLog(label);
            }
        }
    }

    private void typeOneInputForm(ActionEvent actionEvent) {
        DataForExpertExperiments.clear();
        Label label = new Label("Тип эксперимента 1");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(
                ExpertsMainController.class.getResource("/com/student/appfx/expertsExperiments/input/exp1-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ExpertsMainController.typeOneInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void typeTwoInputForm(ActionEvent actionEvent) {
        DataForExpertExperiments.clear();
        Label label = new Label("Тип эксперимента 2");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(ExpertsMainController.class.getResource("/com/student/appfx/expertsExperiments/input/exp2-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ExpertsMainController.typeTwoInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void typeThreeInputForm(ActionEvent actionEvent) {
        DataForExpertExperiments.clear();
        Label label = new Label("Тип эксперимента 3");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(ExpertsMainController.class.getResource("/com/student/appfx/expertsExperiments/input/exp3-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ExpertsMainController.typeThreeInputForm()", e);
        }
        inputStage.setScene(scene);
        inputStage.showAndWait();
    }

    private void typeFourInputForm(ActionEvent actionEvent) {
        DataForExpertExperiments.clear();
        Label label = new Label("Тип эксперимента 4");
        addRecordToLog(label);
        setInputStage(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(ExpertsMainController.class.getResource("/com/student/appfx/expertsExperiments/input/exp4-input.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ExpertsMainController.typeFourInputForm()", e);
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
        if (MainCache.graphics.isEmpty()) {
            Label label = new Label("Эксперимент не проводился");
            addRecordToLog(label);
            return;
        }
        if (resultStage == null) {
            resultStage = new Stage();
            resultStage.setTitle("result");
            resultStage.centerOnScreen();
        }
        MainCache.partToShow = 1;
        FXMLLoader fxmlLoader = new FXMLLoader(ExpertsMainController.class.getResource("/com/student/appfx/results.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ExpertsMainController.actionResult()", e);
        }
        resultStage.setScene(scene);
        resultStage.show();
    }

    public void actionLaunch(ActionEvent actionEvent) {
        if (!DataForExpertExperiments.dataInput) {
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
        ExpertExperimentsController expertExperimentsController = new ExpertExperimentsController();
        if(expertExperimentsController.doExperiment()) {
            Label label = new Label("Данные получены");
            addRecordToLog(label);
        } else {
            Label label = new Label("Тип эксперимента не распознан");
            addRecordToLog(label);
        }
        DataForExpertExperiments.clear();
        for (Toggle toggle : toggles) {
            RadioButton radioButton = (RadioButton) toggle;
            radioButton.setSelected(false);
            radioButton.setDisable(false);
        }
        btnResults.setDisable(false);
        btnLaunch.setDisable(false);
    }
}
