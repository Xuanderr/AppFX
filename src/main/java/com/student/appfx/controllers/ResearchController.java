package com.student.appfx.controllers;

import com.student.appfx.cache.DataForInformationExperiments;
import com.student.appfx.cache.MainCache;
import com.student.appfx.controllers.experimentsControllers.InformationExperimentsController;
import com.student.appfx.controllers.manageControllers.ExpertsMainController;
import com.student.appfx.entities.informationExperiments.ChartWrapper;
import javafx.beans.binding.BooleanBinding;import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ResearchController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResearchController.class.getName());

    @FXML
    private ToolBar toolsCut;
    @FXML
    private HBox boxInfo;
    @FXML
    private CheckBox tooltipBox;
    @FXML
    private AnchorPane chartContainer;
    @FXML
    private MenuItem itemZoom;

    private Stage saveStage;
    private ChartWrapper chartWrapper;
    private final Rectangle zoomRect = createZoomRect();

    @FXML
    private void initialize() {
        chartWrapper = new ChartWrapper(
                MainCache.chartForResearch,
                DataForInformationExperiments.copyRatings(DataForInformationExperiments.expertsRatings));

        setChartContainer();

        if (getChartType(chartWrapper.getChart()) == 1) {
            toolsCut.setDisable(true);
        }

        setBoxInfo();

        final BooleanBinding disableControls =
                zoomRect.widthProperty().lessThan(5)
                        .or(zoomRect.heightProperty().lessThan(5));
        itemZoom.disableProperty().bind(disableControls);
    }

    /* Add/Remove tools */
    public void actionAddH(ActionEvent actionEvent) {
        if (chartWrapper.addHeight()) {
            getRecalculation();
        }
    }

    public void actionAddL(ActionEvent actionEvent) {
        if (chartWrapper.addLow()) {
            getRecalculation();
        }
    }

    public void actionRemoveH(ActionEvent actionEvent) {
        if (chartWrapper.removeHeight()) {
            getRecalculation();
        }
    }

    public void actionRemoveL(ActionEvent actionEvent) {
        if (chartWrapper.removeLow()) {
            getRecalculation();
        }
    }
    /*------------------------------*/

    /* Cut/Return tools */
    public void actionCutMax(ActionEvent actionEvent) {
        if (getChartType(chartWrapper.getChart()) == 1) {
            return;
        }
        if (chartWrapper.cutMax()) {
            getRecalculationMark();
        }
    }

    public void actionCutMin(ActionEvent actionEvent) {
        if (getChartType(chartWrapper.getChart()) == 1) {
            return;
        }
        if (chartWrapper.cutMin()) {
            getRecalculationMark();
        }
    }

    public void actionReturnMax(ActionEvent actionEvent) {
        if (getChartType(chartWrapper.getChart()) == 1) {
            return;
        }
        if (chartWrapper.returnMax()) {
            getRecalculationMark();
        }
    }

    public void actionReturnMin(ActionEvent actionEvent) {
        if (getChartType(chartWrapper.getChart()) == 1) {
            return;
        }
        if (chartWrapper.returnMin()) {
            getRecalculationMark();
        }
    }
    /*------------------------------*/

    public void actionZoom(ActionEvent actionEvent) {
        doZoom(zoomRect, chartWrapper.getChart());
    }

    public void actionReset(ActionEvent actionEvent) {
        final NumberAxis xAxis = (NumberAxis) chartWrapper.getChart().getXAxis();
        xAxis.setAutoRanging(true);
        final NumberAxis yAxis = (NumberAxis) chartWrapper.getChart().getYAxis();
        yAxis.setAutoRanging(true);

        zoomRect.setWidth(0);
        zoomRect.setHeight(0);
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
        FXMLLoader fxmlLoader = new FXMLLoader(ExpertsMainController.class.getResource("/com/student/appfx/save.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            LOGGER.info("IN ResultController.actionFileSave()", e);
        }
        SaveController controller = fxmlLoader.getController();
        controller.setChart(chartWrapper.getChart());
        saveStage.setScene(scene);
        saveStage.showAndWait();
    }

    public void actionTooltip(ActionEvent actionEvent) {
        if (tooltipBox.isSelected()) {
            setTooltip(chartWrapper.getChart());
        } else {
            removeTooltip(chartWrapper.getChart());
        }
    }

    /* Tooltip */
    private void setTooltip(LineChart<Number, Number> chart) {
        for (XYChart.Series<?,?> series: chart.getData()) {
            for (XYChart.Data<?,?> data: series.getData()) {
                Tooltip.install(data.getNode(), new Tooltip("(" + data.getXValue() + ", " + data.getYValue() + ")"));
            }
        }
    }

    private void removeTooltip(LineChart<Number, Number> chart) {
        for (XYChart.Series<?,?> series: chart.getData()) {
            for (XYChart.Data<?,?> data: series.getData()) {
                data.getNode().getProperties().clear();
            }
        }
    }
    /*------------------------------*/

    /* get info from chart */
    private int getChartType(LineChart<Number, Number> chart) {
        return Integer.parseInt(chart.getTitle().split(" ")[1].split(",")[0]);
    }

    private double getEpsilon(LineChart<Number, Number> chart) {
        String string = chart.getTitle().split(" ")[5].replace(",", ".");
        if (string.lastIndexOf(".") == string.length()-1) {
            string = string.substring(0, string.length()-1);
        }
        return Double.parseDouble(string);
    }

    private double getLevel(LineChart<Number, Number> chart) {
        return Double.parseDouble(chart.getTitle().split(" ")[7].replace(",", "."));
    }

    private int getAlgType(LineChart<Number, Number> chart) {
        return switch (chart.getTitle().split(" ")[3].split(",")[0]) {
            case "Simple" -> 1;
            case "Modified" -> 2;
            default -> 0;
        };
    }
    /*------------------------------*/

    /* Zoom */
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
    /*------------------------------*/

    /* Utils */
    private void setNewChart() {
        setBoxInfo();
        setChartContainer();
    }

    private void getRecalculation() {
        InformationExperimentsController informationExperimentsController = new InformationExperimentsController();
        int type = getChartType(chartWrapper.getChart());
        double level = 0;
        if (type == 2) {
            level = getLevel(chartWrapper.getChart());
        }
        chartWrapper.setChart(
                informationExperimentsController.recalculation(
                        chartWrapper.getRatingsExperts(), level, getEpsilon(chartWrapper.getChart()),
                        getAlgType(chartWrapper.getChart()), type));
        setNewChart();
    }

    private void getRecalculationMark() {
        InformationExperimentsController informationExperimentsController = new InformationExperimentsController();
        informationExperimentsController.recalculationMark(chartWrapper.getChart(), getAlgType(chartWrapper.getChart()));
        setNewChart();
    }

    private void setBoxInfo() {
        boxInfo.getChildren().clear();
        Label label = new Label("n: " + chartWrapper.getNeutral());
        label.setPadding(new Insets(5, 5, 5, 5));
        Label label1 = new Label("h: " + chartWrapper.getHeight());
        label1.setPadding(new Insets(5, 5, 5, 5));
        Label label2 = new Label("l: " + chartWrapper.getLow());
        label2.setPadding(new Insets(5, 5, 5, 5));
        boxInfo.getChildren().addAll(label, label1, label2);
    }

    private void setChartContainer() {
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(chartWrapper.getChart());
        AnchorPane.setBottomAnchor(chartWrapper.getChart(),0.0);
        AnchorPane.setLeftAnchor(chartWrapper.getChart(),0.0);
        AnchorPane.setRightAnchor(chartWrapper.getChart(),0.0);
        AnchorPane.setTopAnchor(chartWrapper.getChart(),0.0);
        chartContainer.getChildren().add(zoomRect);
        setUpZooming(zoomRect, chartWrapper.getChart());
    }
}
