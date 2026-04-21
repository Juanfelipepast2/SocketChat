package com.socketsexample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class MainCliente extends Application {

    private static Scene scene;
    private static InicioSesionController in;
    private Parent   root;   





    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainCliente.class.getResource("inicioSesion.fxml"));
        root = fxmlLoader.load();
        in = fxmlLoader.getController();
        scene = new Scene(root);
        
        
        stage.setTitle("Cliente");
        stage.setScene(scene);
        in.init();
        
        
        stage.show();
        
    }

    



    public static void main(String[] args) {
        launch();
    }

}