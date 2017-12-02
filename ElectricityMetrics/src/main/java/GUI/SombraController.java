package GUI;

import Code.Main;
import Code.ReadData;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
        sombraChart = new LineChart<String,Number>(xAxis,yAxis);

    }

    @FXML
    public void initialize(){
        initSombraChart();
    }

    private void initSombraChart() {
        Map<Integer,Integer> sombras = rd.getSombra();
        for(Map.Entry<Integer,Integer> entry:sombras.entrySet()){
            final XYChart.Data<Integer, Integer> data = new XYChart.Data(entry.getKey().toString(), entry.getValue());
            data.setNode(
                    new HoveredThresholdNode(
                            (entry.getKey() == 0) ? 0 : entry.getValue(),
                            entry.getValue()
                    )
            );
            sombraSeries.getData().add(data);
        }
        sombraChart.getData().add(sombraSeries);
    }

    @FXML
    void backBtClicked(MouseEvent event) throws Exception{
        Stage stage = Main.stage;
        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(Integer priorValue, Integer value) {
            setPrefSize(15, 15);

            final Label label = createDataThresholdLabel(priorValue, value);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }

        private Label createDataThresholdLabel(Integer priorValue, Integer value) {
            final Label label = new Label(value + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

            if (priorValue == 0) {
                label.setTextFill(Color.DARKGRAY);
            } else if (value > priorValue) {
                label.setTextFill(Color.FORESTGREEN);
            } else {
                label.setTextFill(Color.FIREBRICK);
            }

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }

}