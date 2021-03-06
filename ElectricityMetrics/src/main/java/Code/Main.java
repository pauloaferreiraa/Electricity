package Code;

import GUI.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static GUIController controller;
    private static Database db;
    public static Stage stage;
    public static void main(String[] args) {
        launch(args);
        ReadData rd = new ReadData();
        rd.CalcMediaDia();
        //Map<String, Double> medias = rd.getMediaDia();
        /*for(String s : medias.keySet()){
            System.out.println(s+"   :"+ medias.get(s));
        }

        System.out.println("-------------------------------");
        Map<String,Double> desvio = rd.getDesvioDia();
        for(String s : desvio.keySet()){
            System.out.println(s+"   :"+ desvio.get(s));
        }
        System.out.println("-------------------------------");
        Map<String,Double> mediaM = rd.getMediaMes();
        for(String s : mediaM.keySet()){
            System.out.println(s+"   :"+ mediaM.get(s));
        }

        System.out.println("-------------------------------");
        Map<String,Double> desvioM = rd.getDesvioMes();
        for(String s : desvioM.keySet()){
            System.out.println(s+"   :"+ desvioM.get(s));
        }*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Eletricidade");
        primaryStage.setScene(scene);
        primaryStage.show();
        this.stage = primaryStage;

    }
}
