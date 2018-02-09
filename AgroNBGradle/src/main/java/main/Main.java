package main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    public static Stage EnterStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("console.encoding","Cp866");
        Main.EnterStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/enter.fxml"));
        primaryStage.setTitle("Argo");
        primaryStage.setScene(new Scene(root, 400, 375));
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args); }


}
