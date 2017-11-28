package GUI;

import Code.ReadData;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

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

    private Boolean dayChartVisible, monthChartVisible, dayCHartFilled;
    private CategoryAxis xAxisDays;
    private NumberAxis yAxisDays;
    private XYChart.Series seriesMeanDays;
    private XYChart.Series seriesStdDevDays;
    private ReadData rd;

    public GUIController() {
        rd = new ReadData();
        daysButton = new Button();
        monthButton = new Button();
        xAxisDays = new CategoryAxis();
        yAxisDays = new NumberAxis();
        seriesMeanDays = new XYChart.Series();
        seriesStdDevDays = new XYChart.Series();
        dayChart = new LineChart<String, Number>(xAxisDays, yAxisDays);
        //monthChart = new LineChart<Number,String>(new NumberAxis(),new CategoryAxis());
        dayChartVisible = false;
        monthChartVisible = false;
        dayCHartFilled = false;
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
        dayChartVisible = !dayChartVisible;
        dayChart.setVisible(dayChartVisible);
    }

    @FXML
    public void monthButtonClicked(MouseEvent event) {
        if (dayChartVisible) {
            dayChartVisible = !dayChartVisible;
            dayChart.setVisible(dayChartVisible);
        }
        monthChartVisible = !monthChartVisible;
        monthChart.setVisible(monthChartVisible);
    }

    public void initDayChart() {
        dayChart.setAnimated(false);
        Map<String, Double> medias = rd.getMediaDia();
        Map<String,Double> desvio = rd.getDesvioDia();
        for (String s : medias.keySet()) {
            seriesMeanDays.getData().add(new XYChart.Data(s, medias.get(s)));
        }

        for(String st : desvio.keySet()){
            seriesStdDevDays.getData().add(new XYChart.Data(st, desvio.get(st)));
        }
        seriesMeanDays.setName("Média");
        seriesStdDevDays.setName("Desvio Padrão");
        dayChart.getData().addAll(seriesMeanDays,seriesStdDevDays);

    }


}

