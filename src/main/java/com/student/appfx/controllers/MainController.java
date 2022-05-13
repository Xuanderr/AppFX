package com.student.appfx.controllers;

import com.student.appfx.cache.MainCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class.getName());

    @FXML
    private VBox paneLog;

    private Stage stage;

    public void actionPart_1(ActionEvent actionEvent) {
        Label label = new Label("Выбрана первая часть: оценка экспертов");
        addRecordToLog(label);
        setPartStage(actionEvent, "Part 1");
        stage.setWidth(350);
        stage.setMaxWidth(350);
        stage.setMinWidth(350);
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainController.class.getResource("/com/student/appfx/expertsExperiments/expertsExperiments.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.actionPart_1()", e);
        }
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void actionPart_2(ActionEvent actionEvent) {
        Label label = new Label("Выбрана вторая часть: оценка информации");
        addRecordToLog(label);
        setPartStage(actionEvent, "Part 2");
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainController.class.getResource("/com/student/appfx/informationExperiments/infoExperiments.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.actionPart_2()", e);
        }
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void actionPart_3(ActionEvent actionEvent) {
        Label label = new Label("Выбрана третья часть: обратная связь");
        addRecordToLog(label);
        setPartStage(actionEvent, "Part 3");
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainController.class.getResource("/com/student/appfx/feedbackExperiments/feedbackExperiments.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.actionPart_3()", e);
        }
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void actionAIVS(ActionEvent actionEvent) {
        Label label = new Label("Запуск системы в полной сборке");
        addRecordToLog(label);
        setPartStage(actionEvent, "Part 3");
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainController.class.getResource("/com/student/appfx/expertsExperiments/expertsExperiments.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN MainController.actionPart_3()", e);
        }
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }

    private void setPartStage(ActionEvent actionEvent, String title) {
        stage = new Stage();
        stage.setTitle(title);
        stage.setX(((Node) actionEvent.getSource()).getScene().getWindow().getX() - 15);
        stage.setY(((Node) actionEvent.getSource()).getScene().getWindow().getY() + 15);
        stage.setMinHeight(250);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.setOnCloseRequest(windowEvent -> {
            MainCache.graphics.clear();
        });
    }
}
