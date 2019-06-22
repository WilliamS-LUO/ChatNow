package Client.Log_in;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage PrimaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        PrimaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Log_in.fxml"));
        primaryStage.setTitle("ChatNow");
        primaryStage.setScene(new Scene(root, 350, 450));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage getPrimaryStage(){
        return PrimaryStage;
    }
}
