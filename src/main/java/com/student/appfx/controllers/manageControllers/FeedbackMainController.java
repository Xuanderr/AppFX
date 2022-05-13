package com.student.appfx.controllers.manageControllers;

import com.student.appfx.cache.DataForFeedbackExperiments;
import com.student.appfx.cache.DataForInformationExperiments;
import com.student.appfx.cache.MainCache;
import com.student.appfx.controllers.SaveController;
import com.student.appfx.controllers.experimentsControllers.FeedbackExperimentController;
import com.student.appfx.controllers.experimentsControllers.InformationExperimentsController;
import com.student.appfx.entities.expertExperiments.Experiment;
import com.student.appfx.entities.informationExperiments.Splitter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackMainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeedbackMainController.class.getName());

    private final Pattern patternInt = Pattern.compile("\\d+");
    private final Pattern patternDouble = Pattern.compile("0\\.\\d+");

    @FXML
    private TextField fieldExp;
    @FXML
    private VBox paneLog;
    @FXML
    private Button btnResults;
    @FXML
    private Button btnLaunch;
    @FXML
    private TextField fieldLevel;

    private Stage resultStage;

    @FXML
    private void initialize() {
        SaveController.setPaneLog(paneLog);
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
        MainCache.partToShow = 2;
        FXMLLoader fxmlLoader = new FXMLLoader(ExpertsMainController.class.getResource("/com/student/appfx/results.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN FeedbackMainController.actionResult()", e);
        }
        resultStage.setScene(scene);
        resultStage.show();
    }

    public void actionLaunch(ActionEvent actionEvent) {
        if (!getInput()) {
            return;
        }
        if (!DataForFeedbackExperiments.dataInput) {
            Label label = new Label("Некорректные данные");
            addRecordToLog(label);
            return;
        }
        btnLaunch.setDisable(true);
        btnResults.setDisable(true);
        FeedbackExperimentController feedbackExperimentController = new FeedbackExperimentController();
        feedbackExperimentController.doExperiment();
        Label label = new Label("Данные получены");
        addRecordToLog(label);

        DataForInformationExperiments.clear();
        btnResults.setDisable(false);
        btnLaunch.setDisable(false);
    }

    private boolean checkExpertInput(String value, Pattern pattern) {
        if (value == null) {
            fieldExp.setText("error");
            return false;
        }
        if (match(value, pattern) & Integer.parseInt(value) != 0) {
            DataForFeedbackExperiments.experts = Integer.parseInt(value);
            Label label = new Label("Количество экспертов введено");
            addRecordToLog(label);
            return true;
        } else {
            fieldExp.setText("error");
            return false;
        }
    }

    private boolean checkLevelInput(String value, Pattern pattern) {
        if (value == null) {
            fieldLevel.setText("error");
            return false;
        }
        if (match(value, pattern)) {
            DataForFeedbackExperiments.level = Double.parseDouble(value);
            Label label = new Label("Исходный уровень доверия к сообщению введен");
            addRecordToLog(label);
            return true;
        } else {
            fieldLevel.setText("error");
            return false;
        }
    }

    private boolean getInput() {
        String experts = fieldExp.getText();
        if (Objects.equals(experts, "")) {
            experts = null;
        }
        String level = fieldLevel.getText();
        if (Objects.equals(level, "")) {
            level = null;
        }
        if (checkExpertInput(experts, patternInt) & checkLevelInput(level, patternDouble)) {
            DataForFeedbackExperiments.dataInput = true;
            return true;
        } else {
            DataForFeedbackExperiments.clear();
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
