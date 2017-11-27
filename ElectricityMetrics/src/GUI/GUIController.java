package GUI;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class GUIController {

    @FXML
    private Button daysButton;

    @FXML
    private Button monthButton;

    @FXML
    private LineChart<?, ?> dayChart;

    @FXML
    private LineChart<?, ?> monthChart;

    private Boolean dayChartVisible, monthChartVisible;

    public GUIController(){
        daysButton = new Button();
        monthButton = new Button();
        dayChart = new LineChart<Number,String>(new NumberAxis(),new CategoryAxis());
        monthChart = new LineChart<Number,String>(new NumberAxis(),new CategoryAxis());
        dayChartVisible = false;
        monthChartVisible = false;
    }

    @FXML
    public void daysButtonCLicked(MouseEvent event) {
        if(monthChartVisible){
            monthChartVisible = !monthChartVisible;
            monthChart.setVisible(monthChartVisible);
        }
        dayChartVisible = !dayChartVisible;
        dayChart.setVisible(dayChartVisible);
    }

    @FXML
    public void monthButtonClicked(MouseEvent event) {
        if(dayChartVisible){
            dayChartVisible = !dayChartVisible;
            dayChart.setVisible(dayChartVisible);
        }
        monthChartVisible = !monthChartVisible;
        monthChart.setVisible(monthChartVisible);
    }
}

