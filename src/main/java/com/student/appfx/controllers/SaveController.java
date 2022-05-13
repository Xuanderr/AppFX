package com.student.appfx.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SaveController.class.getName());

    private static VBox paneLog;

    private LineChart<?,?> chart;
    private final Pattern pattern = Pattern.compile("\\w+");

    @FXML
    private TextField fileName;

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void actionSave(ActionEvent actionEvent) {
        String name = fileName.getText();
        if (Objects.equals(name, "") | !match(name)) {
            fileName.setText("буквы, цифры и _");
            return;
        }
        saveAsPng(chart, name);
        Label label = new Label(String.format("Файл %s.png сохранен", name));
        addRecordToLog(label);
        actionClose(actionEvent);
    }

    public void setChart(LineChart<?,?> chart) {
        this.chart = chart;
    }

    private boolean match(String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private void saveAsPng(Node node, String fileName) {
        WritableImage image = node.snapshot(new SnapshotParameters(), null);
        File file = new File(fileName+".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            LOGGER.info("IN SaveController.saveAsPng()", e);
        }
    }

    public static void setPaneLog(VBox paneLog) {
        SaveController.paneLog = paneLog;
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }
}
