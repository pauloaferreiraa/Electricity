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
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class PicosController {

    @FXML
    private ChoiceBox<String> yearCB;

    @FXML
    private ChoiceBox<String> monthCB;

    @FXML
    private ChoiceBox<String> dayCB;

    @FXML
    private LineChart<String, Number> picosChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series pico;
    private XYChart.Series gastos;
    private ReadData rd;

    public PicosController() {
        yearCB = new ChoiceBox<String>();
        monthCB = new ChoiceBox<String>();
        dayCB = new ChoiceBox<String>();
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        picosChart = new LineChart<String, Number>(xAxis, yAxis);
        pico = new XYChart.Series();
        gastos = new XYChart.Series();
        rd = new ReadData();
    }

    @FXML
    public void initialize() {
        fillYearCB();
        picosChart.getData().addAll(gastos,pico);
        yearCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(yearCB.getSelectionModel().isEmpty())) {
                    String year = String.valueOf(yearCB.getItems().get((Integer) newValue));
                    if (!year.equals("Ano")) {
                        fillMonthCB(year);
                    } else {
                        monthCB.getItems().clear();
                        dayCB.getItems().clear();
                    }
                }
            }
        });

        monthCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(yearCB.getSelectionModel().isEmpty() || monthCB.getSelectionModel().isEmpty())) {
                    String year = String.valueOf(yearCB.getSelectionModel().getSelectedItem());
                    String month = String.valueOf(monthCB.getItems().get((Integer) newValue));
                    if (!year.equals("Ano") && !month.equals("Mês")) {
                        fillDayCB(year, month);
                    } else {
                        if (month.equals("Mês")) {
                            dayCB.getItems().clear();
                        }
                    }
                }
            }
        });

        dayCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!dayCB.getSelectionModel().isEmpty()) {
                    String year = String.valueOf(yearCB.getSelectionModel().getSelectedItem());
                    String month = String.valueOf(monthCB.getSelectionModel().getSelectedItem());
                    String day = String.valueOf(dayCB.getItems().get((Integer) newValue));
                    if (!day.equals("Dias")) {
                        fillChart(year, month, day);
                    }
                }
            }
        });
    }

    private void fillChart(String year, String month, String day){
        gastos.getData().clear();
        pico.getData().clear();
        String date = year + "/" + month + "/" + day;
        double pico_kw = rd.getKwMaximum(year,month,day) - 5;
        Map<String,Double> res = rd.getPicos(date);
        for(Map.Entry<String,Double> entry:res.entrySet()){
            gastos.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
            pico.getData().add(new XYChart.Data(entry.getKey(),pico_kw));
        }
        gastos.setName("Gastos em KW");
        pico.setName("Pico");
        picosChart.setAnimated(false);

    }

    private void fillDayCB(String year, String month) {
        ArrayList<String> months = rd.getDayByMonthYear(year, month);
        ObservableList<String> list = FXCollections.observableArrayList(months);
        dayCB.setItems(list);
    }

    private void fillMonthCB(String year) {
        ArrayList<String> months = rd.getMonthByYear(year);
        ObservableList<String> list = FXCollections.observableArrayList(months);
        monthCB.setItems(list);
    }

    private void fillYearCB() {
        ArrayList<String> years = rd.years();
        ObservableList<String> list = FXCollections.observableArrayList(years);
        yearCB.setItems(list);

    }

    public void voltarBtClicked(MouseEvent mouseEvent) throws Exception{
        Stage stage = Main.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}