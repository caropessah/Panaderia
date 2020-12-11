package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Panaderia");
        primaryStage.setScene(new Scene(root, 392, 231));
        Image logo = new Image(getClass().getResourceAsStream("bread.png"));
        primaryStage.getIcons().add(logo);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
