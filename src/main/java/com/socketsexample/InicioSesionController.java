package com.socketsexample;

import java.io.IOException;


import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class InicioSesionController {

    @FXML
    private Button boton_inicioSesion;

    @FXML
    private TextField campoTexto;

    

    void habilitarBoton() {

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(campoTexto.textProperty());
            }
        
            @Override
            protected boolean computeValue() {
                return (campoTexto.getText().isEmpty());
            }
        };
        boton_inicioSesion.disableProperty().bind(bb);
    }

    @FXML
    void inicioSesion(ActionEvent event) throws IOException {
        change(event, null);
        
        
        
    }


    void change(ActionEvent ev, KeyEvent kv) throws IOException{
        Stage stage;
        Parent root;
        FXMLLoader fxmlod = new FXMLLoader(getClass().getResource("chatCliente.fxml"));
        root = fxmlod.load();

        ChatController ct = new ChatController();
        ct = fxmlod.getController();
        ct.init(campoTexto.getText());
        
        Scene scene;
        stage = new Stage();
        if(ev == null){
            stage = (Stage)((Node)kv.getSource()).getScene().getWindow();
        } else if(kv == null){
            stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
        }
        
        scene = new Scene(root);
        stage.setTitle("Cliente");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void onEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            
            change(null, event);
        }
    }

    

    void init() {
        //boton_inicioSesion.setDisable(true);
        habilitarBoton();
        
    }

}