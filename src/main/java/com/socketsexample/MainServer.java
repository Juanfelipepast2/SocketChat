package com.socketsexample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.socketsexample.Modelo.Server;

/**
 * JavaFX App
 */
public class MainServer extends Application {

    private static Scene scene;
    private Server sv;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainServer.class.getResource("chatServer.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root);
        ServerController sc = fxmlLoader.getController();
        
        stage.setTitle("Servidor");
        stage.setScene(scene);
        sv = new Server();
        sc.init(sv);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainServer.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }


}