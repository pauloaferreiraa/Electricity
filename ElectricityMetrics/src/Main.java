import GUI.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    private static GUIController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI/MainWindow.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Eletricidade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
