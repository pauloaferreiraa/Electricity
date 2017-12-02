package GUI;

import Code.Main;
import Code.ReadData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class GUIController {

    @FXML
    private AnchorPane gdChartPane;

    @FXML
    private Button daysButton;

    @FXML
    private Button monthButton;

    @FXML
    private LineChart<?, ?> dayChart;

    @FXML
    private LineChart<?, ?> monthChart;

    @FXML
    private Pane daysPane;

    @FXML
    private ChoiceBox<String> gastosDiariosYearCB;

    @FXML
    private ChoiceBox<String> gastosDiariosMonthCB;

    @FXML
    private ChoiceBox<String> gastosDiariosDayCB;

    private Boolean dayChartVisible, monthChartVisible, dayCHartFilled, daysPaneVisible, monthChartFilled;
    private CategoryAxis xAxisDays;
    private NumberAxis yAxisDays;
    private CategoryAxis xAxisMonth;
    private NumberAxis yAxisMonth;
    private XYChart.Series seriesMeanDays;
    private XYChart.Series seriesStdDevDays;
    private XYChart.Series seriesMeanMonth;
    private XYChart.Series seriesStdDevMonth;
    private ReadData rd;
    private final double SCALE_DELTA = 1.1;
    private AnimatedZoom zoomOperator;

    public GUIController() {
        rd = new ReadData();
        daysButton = new Button();
        monthButton = new Button();
        xAxisDays = new CategoryAxis();
        xAxisMonth = new CategoryAxis();
        yAxisDays = new NumberAxis();
        yAxisMonth = new NumberAxis();
        seriesMeanDays = new XYChart.Series();
        seriesStdDevDays = new XYChart.Series();
        seriesMeanMonth = new XYChart.Series();
        seriesStdDevMonth = new XYChart.Series();
        dayChart = new LineChart<>(xAxisDays, yAxisDays);
        monthChart = new LineChart<>(xAxisMonth, yAxisMonth);
        gastosDiariosYearCB = new ChoiceBox<>();
        gastosDiariosMonthCB = new ChoiceBox<>();
        gastosDiariosDayCB = new ChoiceBox<>();
        dayChartVisible = false;
        monthChartVisible = false;
        dayCHartFilled = false;
        daysPaneVisible = false;
        monthChartFilled = false;
        zoomOperator = new AnimatedZoom();
    }

    @FXML

    public void initialize() {

        fillYearCB();

        //Adicionar listener aos anos para adicionar novos elementos no mes
        gastosDiariosYearCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(gastosDiariosYearCB.getSelectionModel().isEmpty())) {
                    String year = String.valueOf(gastosDiariosYearCB.getItems().get((Integer) newValue));
                    if (!year.equals("Ano")) {
                        fillMonthCB(year);
                        applyFilters(year);
                    } else {
                        gastosDiariosMonthCB.getItems().clear();
                        gastosDiariosDayCB.getItems().clear();
                        applyFilters();
                    }
                }


            }
        });
        gastosDiariosMonthCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(gastosDiariosYearCB.getSelectionModel().isEmpty() || gastosDiariosMonthCB.getSelectionModel().isEmpty())) {
                    String year = String.valueOf(gastosDiariosYearCB.getSelectionModel().getSelectedItem());
                    String month = String.valueOf(gastosDiariosMonthCB.getItems().get((Integer) newValue));
                    if (!year.equals("Ano") && !month.equals("Mês")) {
                        fillDayCB(year, month);
                        applyFilters(year, month);
                        System.out.println(year + " " + month);
                    } else {
                        if (month.equals("Mês")) {
                            gastosDiariosDayCB.getItems().clear();
                            applyFilters(year);
                        }
                    }
                }
            }
        });

        gastosDiariosDayCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if (!gastosDiariosDayCB.getSelectionModel().isEmpty()) {
                    String year = String.valueOf(gastosDiariosYearCB.getSelectionModel().getSelectedItem());
                    String month = String.valueOf(gastosDiariosMonthCB.getSelectionModel().getSelectedItem());
                    String day = String.valueOf(gastosDiariosDayCB.getItems().get((Integer) newValue));
                    if (!day.equals("Dias")) {
                        applyFilters(year, month, day);
                    } else {
                        applyFilters(year, month);
                    }
                }


            }
        });
    }

    public void applyFilters() {
        Map<String, Double> medias = rd.getMediaDia();
        Map<String, Double> desvio = rd.getDesvioDia();
        for (String s : medias.keySet()) {
            seriesMeanDays.getData().add(new XYChart.Data(s, medias.get(s)));
        }

        for (String st : desvio.keySet()) {
            seriesStdDevDays.getData().add(new XYChart.Data(st, desvio.get(st)));
        }
    }

    public void applyFilters(String year) {
        Map<String, Double> medias = rd.getMediaDia(year);
        Map<String, Double> desvio = rd.getDesvioDia(year);
        seriesStdDevDays.getData().clear();
        seriesMeanDays.getData().clear();
        for (String s : medias.keySet()) {
            seriesMeanDays.getData().add(new XYChart.Data(s, medias.get(s)));
        }

        for (String st : desvio.keySet()) {
            seriesStdDevDays.getData().add(new XYChart.Data(st, desvio.get(st)));
        }
    }

    public void applyFilters(String year, String month) {
        Map<String, Double> medias = rd.getMediaDia(year, month);
        Map<String, Double> desvio = rd.getDesvioDia(year, month);
        seriesStdDevDays.getData().clear();
        seriesMeanDays.getData().clear();
        for (String s : medias.keySet()) {
            seriesMeanDays.getData().add(new XYChart.Data(s, medias.get(s)));
        }

        for (String st : desvio.keySet()) {
            seriesStdDevDays.getData().add(new XYChart.Data(st, desvio.get(st)));
        }
    }

    public void applyFilters(String year, String month, String day) {
        Map<String, Double> medias = rd.getMediaDia(year, month, day);
        Map<String, Double> desvio = rd.getDesvioDia(year, month, day);
        seriesStdDevDays.getData().clear();
        seriesMeanDays.getData().clear();
        for (String s : medias.keySet()) {
            seriesMeanDays.getData().add(new XYChart.Data(s, medias.get(s)));
        }

        for (String st : desvio.keySet()) {
            seriesStdDevDays.getData().add(new XYChart.Data(st, desvio.get(st)));
        }
    }

    @FXML
    public void daysButtonCLicked(MouseEvent event) {

        if (!dayCHartFilled) {
            initDayChart();
            dayCHartFilled = true;
        }
        if (monthChartVisible) {
            monthChartVisible = !monthChartVisible;
            monthChart.setVisible(monthChartVisible);
        }
        daysPaneVisible = !daysPaneVisible;
        daysPane.setVisible(daysPaneVisible);
        dayChartVisible = !dayChartVisible;
        dayChart.setVisible(dayChartVisible);
    }

    @FXML
    public void monthButtonClicked(MouseEvent event) {
        if (!monthChartFilled) {
            initMonthCHart();
            monthChartFilled = true;
        }
        if (dayChartVisible) {
            dayChartVisible = !dayChartVisible;
            dayChart.setVisible(dayChartVisible);
        }
        if (daysPaneVisible) {
            daysPaneVisible = !daysPaneVisible;
            daysPane.setVisible(daysPaneVisible);
        }
        monthChartVisible = !monthChartVisible;
        monthChart.setVisible(monthChartVisible);
    }

    private void initMonthCHart() {
        Map<String, Double> medias = rd.getMediaMes();
        Map<String, Double> desvio = rd.getDesvioMes();
        monthChart.setAnimated(false);
        for (String s : medias.keySet()) {
            seriesMeanMonth.getData().add(new XYChart.Data(s, medias.get(s)));
        }

        for (String st : desvio.keySet()) {
            seriesStdDevMonth.getData().add(new XYChart.Data(st, desvio.get(st)));
        }
        seriesMeanMonth.setName("Média");
        seriesStdDevMonth.setName("Desvio Padrão");
        monthChart.getData().addAll(seriesMeanMonth, seriesStdDevMonth);
    }

    public void initDayChart() {
        if (gastosDiariosYearCB.getSelectionModel().isEmpty() && gastosDiariosDayCB.getSelectionModel().isEmpty() &&
                gastosDiariosMonthCB.getSelectionModel().isEmpty()) {
            Map<String, Double> medias = rd.getMediaDia();
            Map<String, Double> desvio = rd.getDesvioDia();
            for (String s : medias.keySet()) {
                seriesMeanDays.getData().add(new XYChart.Data(s, medias.get(s)));
            }

            for (String st : desvio.keySet()) {
                seriesStdDevDays.getData().add(new XYChart.Data(st, desvio.get(st)));
            }
            seriesMeanDays.setName("Média");
            seriesStdDevDays.setName("Desvio Padrão");
            dayChart.getData().addAll(seriesMeanDays, seriesStdDevDays);
        }

    }

    public void fillMonthCB(String year) {
        ArrayList<String> months = rd.getMonthByYear(year);
        ObservableList<String> list = FXCollections.observableArrayList(months);
        gastosDiariosMonthCB.setItems(list);
    }

    public void fillYearCB() {
        ArrayList<String> years = rd.years();
        ObservableList<String> list = FXCollections.observableArrayList(years);
        gastosDiariosYearCB.setItems(list);
    }

    public void fillDayCB(String year, String month) {
        ArrayList<String> months = rd.getDayByMonthYear(year, month);
        ObservableList<String> list = FXCollections.observableArrayList(months);
        gastosDiariosDayCB.setItems(list);
    }

    @FXML
    void onScroll(ScrollEvent event) {

        double zoomFactor = 1.5;
        if (event.getDeltaY() <= 0) {
            // zoom out
            zoomFactor = 1 / zoomFactor;
        }
        zoomOperator.zoom(gdChartPane, zoomFactor, event.getSceneX(), event.getSceneY());

    }

    @FXML
    void sombraBtClicked(MouseEvent event) throws Exception{
        Stage stage = Main.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/Sombra.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}

