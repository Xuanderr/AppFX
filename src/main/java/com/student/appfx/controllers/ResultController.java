package com.student.appfx.controllers;

import com.student.appfx.cache.DataCache;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ResultController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResultController.class.getName());

    @FXML
    private Button btnZoom;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Pagination pgnResults;

    private Stage saveStage;
    private final Rectangle zoomRect = createZoomRect();
    private LineChart<Number, Number> currentChart;

    @FXML
    private void initialize() {
        pgnResults.setPageCount(DataCache.graphics.size());
        pgnResults.setMaxPageIndicatorCount(10);
        pgnResults.setPageFactory(this::createPage);
        final BooleanBinding disableControls =
                zoomRect.widthProperty().lessThan(5)
                        .or(zoomRect.heightProperty().lessThan(5));
        btnZoom.disableProperty().bind(disableControls);
    }

    private Node createPage(Integer pageIndex) {
        currentChart = DataCache.graphics.get(pageIndex);
        setTooltip(currentChart);
        StackPane chartContainer = new StackPane();
        chartContainer.getChildren().add(currentChart);
        chartContainer.getChildren().add(zoomRect);
        setUpZooming(zoomRect, currentChart);


        AnchorPane.setBottomAnchor(chartContainer,55.0);
        AnchorPane.setLeftAnchor(chartContainer,0.0);
        AnchorPane.setRightAnchor(chartContainer,0.0);
        AnchorPane.setTopAnchor(chartContainer,0.0);
        mainAnchorPane.getChildren().add(chartContainer);
        return chartContainer;
    }

    private void setTooltip(LineChart<Number,Number> lineChart) {
        for (XYChart.Series<Number, Number> series: lineChart.getData()) {
            for (XYChart.Data<Number, Number> data: series.getData()) {
                Tooltip.install(data.getNode(), new Tooltip("(" + data.getXValue() + ", " + data.getYValue() + ")"));
            }
        }
    }

    public void actionFileSave(ActionEvent actionEvent) {

        if (saveStage == null) {
            saveStage = new Stage();
            saveStage.setTitle("save");
            saveStage.centerOnScreen();
            saveStage.setResizable(false);
            saveStage.initModality(Modality.WINDOW_MODAL);
            saveStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/com/student/appfx/save.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ResultController.actionFileSave()", e);
        }
        SaveController controller = fxmlLoader.getController();
        controller.setChart(currentChart);
        saveStage.setScene(scene);
        saveStage.showAndWait();
    }

    public void actionZoom(ActionEvent actionEvent) {
        doZoom(zoomRect, currentChart);
    }

    public void actionReset(ActionEvent actionEvent) {
        final NumberAxis xAxis = (NumberAxis) currentChart.getXAxis();
        xAxis.setAutoRanging(true);
        final NumberAxis yAxis = (NumberAxis) currentChart.getYAxis();
        yAxis.setAutoRanging(true);

        zoomRect.setWidth(0);
        zoomRect.setHeight(0);
    }

    private Rectangle createZoomRect() {
        Rectangle zoomRect = new Rectangle();
        zoomRect.setManaged(false);
        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
        return zoomRect;
    }

    private void doZoom(Rectangle zoomRect, LineChart<Number, Number> chart) {
        Point2D zoomTopLeft = new Point2D(zoomRect.getX(), zoomRect.getY());
        Point2D zoomBottomRight = new Point2D(zoomRect.getX() + zoomRect.getWidth(), zoomRect.getY() + zoomRect.getHeight());
        final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        yAxis.setAutoRanging(false);
        Point2D yAxisInScene = yAxis.localToScene(0, 0);
        final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        xAxis.setAutoRanging(false);
        Point2D xAxisInScene = xAxis.localToScene(0, 0);
        double xOffset = zoomTopLeft.getX() - (yAxisInScene.getX() + yAxis.getWidth());
        double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
        double xAxisScale = xAxis.getScale();
        double yAxisScale = yAxis.getScale();
        xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
        xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
        yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
        yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
        zoomRect.setWidth(0);
        zoomRect.setHeight(0);
    }

    private void setUpZooming(final Rectangle rect, final Node zoomingNode) {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        zoomingNode.setOnMousePressed(event -> {
            mouseAnchor.set(new Point2D(event.getX(), event.getY()));
            rect.setWidth(0);
            rect.setHeight(0);
        });
        zoomingNode.setOnMouseDragged(event -> {
            double x = event.getX();
            double y = event.getY();
            rect.setX(Math.min(x, mouseAnchor.get().getX()));
            rect.setY(Math.min(y, mouseAnchor.get().getY()));
            rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
            rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));
        });
    }

}
