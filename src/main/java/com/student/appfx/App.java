package com.student.appfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("main");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setWidth(350);
        stage.setMaxWidth(350);
        stage.setMinWidth(350);
        stage.setMaxHeight(400);
        stage.setMinHeight(250);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}