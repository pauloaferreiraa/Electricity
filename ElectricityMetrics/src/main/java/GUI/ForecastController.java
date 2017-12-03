package GUI;

import Code.Forecast;
import Code.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ForecastController {

    @FXML
    private BarChart<String, Number> previsaoChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series mes1serie;
    private XYChart.Series mes2serie;

    private Forecast fcast;

    public ForecastController(){
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        mes1serie = new XYChart.Series();
        mes2serie = new XYChart.Series();
        previsaoChart = new BarChart<String, Number>(xAxis, yAxis);
        previsaoChart.setTitle("Previsão dos Gastos");
        xAxis.setLabel("Mês");
        yAxis.setLabel("Kw");
        fcast = new Forecast();

    }


    public void initialize(){
        String mes1 = "2014/5";
        String mes2 = "2014/6";

        mes1serie.getData().add(new XYChart.Data(mes1,fcast.previsao(mes1)));
        mes2serie.getData().add(new XYChart.Data(mes2,fcast.previsao(mes2)));
        mes1serie.setName(mes1);
        mes2serie.setName(mes2);
        previsaoChart.getData().addAll(mes1serie,mes2serie);
    }


    @FXML
    void backBtClicked(MouseEvent event) throws Exception{
        Stage stage = Main.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}