package GUI;

import Code.ReadData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Map;

public class GUIController {

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
    private Button gastosDiariosApplyButton;

    @FXML
    private ChoiceBox<String> gastosDiariosYearCB;

    @FXML
    private ChoiceBox<String> gastosDiariosMonthCB;

    @FXML
    private ChoiceBox<String> gastosDiariosDayCB;

    private Boolean dayChartVisible, monthChartVisible, dayCHartFilled, daysPaneVisible, gastosDiariosMonthCBVisible, gastosDiariosDayCBVisible;
    private CategoryAxis xAxisDays;
    private NumberAxis yAxisDays;
    private XYChart.Series seriesMeanDays;
    private XYChart.Series seriesStdDevDays;
    private ReadData rd;

    public GUIController() {
        rd = new ReadData();
        gastosDiariosApplyButton = new Button();
        daysButton = new Button();
        monthButton = new Button();
        xAxisDays = new CategoryAxis();
        yAxisDays = new NumberAxis();
        seriesMeanDays = new XYChart.Series();
        seriesStdDevDays = new XYChart.Series();
        dayChart = new LineChart<String, Number>(xAxisDays, yAxisDays);
        gastosDiariosYearCB = new ChoiceBox<String>();
        gastosDiariosMonthCB = new ChoiceBox<String>();
        gastosDiariosDayCB = new ChoiceBox<String>();
        //monthChart = new LineChart<Number,String>(new NumberAxis(),new CategoryAxis());
        dayChartVisible = false;
        monthChartVisible = false;
        dayCHartFilled = false;
        daysPaneVisible = false;
    }

    @FXML

    public void initialize() {

        fillYearCB();

        //Adicionar listener aos anos para adicionar novos elementos no mes
        gastosDiariosYearCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String year = String.valueOf(gastosDiariosYearCB.getItems().get((Integer) newValue));
                if (!year.equals("Ano")) {
                    fillMonthCB(year);
                } else {
                    gastosDiariosMonthCB.getItems().clear();
                    gastosDiariosDayCB.getItems().clear();
                }


            }
        });
        gastosDiariosMonthCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(gastosDiariosYearCB.getSelectionModel().isEmpty() || gastosDiariosMonthCB.getSelectionModel().isEmpty())) {
                    String year = String.valueOf(gastosDiariosYearCB.getItems().get((Integer) newValue));
                    String month = String.valueOf(gastosDiariosMonthCB.getItems().get((Integer) newValue));
                    if (!year.equals("Ano") && !month.equals("Mês")) {
                        fillDayCB(year, month);
                    } else {
                        if (month.equals("Mês")) {
                            gastosDiariosDayCB.getItems().clear();
                        }
                    }
                }
            }
        });
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


}

