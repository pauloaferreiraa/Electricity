package GUI;

import Code.Main;
import Code.ReadData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Map;

public class SombraController {

    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series sombraSeries;
    @FXML
    private Button backBT;
    private ReadData rd;

    @FXML
    private LineChart<?, ?> sombraChart;
    
    public SombraController(){
        rd = new ReadData();
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        sombraSeries = new XYChart.Series();
        sombraSeries.setName("Tarifa de Sombra");
        sombraChart = new LineChart<>(xAxis,yAxis);

    }

    @FXML
    public void initialize(){
        initSombraChart();
    }

    private void initSombraChart() {
        Map<String,Integer> sombras = rd.getSombra();
        for(Map.Entry<String,Integer> entry:sombras.entrySet()){
            sombraSeries.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
        }
        sombraChart.getData().add(sombraSeries);
    }

    @FXML
    void backBtClicked(MouseEvent event) throws Exception{
        Stage stage = Main.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}