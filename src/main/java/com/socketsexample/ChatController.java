package com.socketsexample;

import com.socketsexample.Modelo.Cliente;
import com.socketsexample.Modelo.Usuario;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class ChatController {

    @FXML
    private Button botonEnviar;

    @FXML
    private TextArea cajaDeMensajes;

    @FXML
    private TextArea cajaTexto;

    @FXML
    private Circle circulo_fotoPerfil;

    @FXML
    private Circle circulo_verificacionConexion;

    @FXML
    private Label label_nombreUsuario;

    @FXML
    private Label label_verificacionConexion;

    @FXML
    private Pane panelBase;

    Cliente cliente;

    @FXML
    void enviarMensaje(ActionEvent event) {
        msg(cajaTexto.getText());
        
    }

    @FXML
    void keyPressed(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            msg(cajaTexto.getText());
        }

    }

    public void limpiarCajaTexto() {
        // System.out.println("XD");
        cajaTexto.setText("");
    }

    void msg(String mensaje) {
        
        if (!cajaTexto.getText().isEmpty() && !cajaTexto.getText().isBlank()) {            
            cliente.enviarMensaje(mensaje);
            limpiarCajaTexto();
            cajaDeMensajes.setScrollTop(Double.MAX_VALUE);
        } else {
            System.out.println("Caja de texto vacia");
        }

    }

    public void agregar(String mensaje) {

        if (cajaDeMensajes.getText().isEmpty()) {
            cajaDeMensajes.setText(mensaje);
        } else {
            cajaDeMensajes.setText(cajaDeMensajes.getText() + mensaje);
        }
    }

    public void init(String user) {
        label_nombreUsuario.setText(user);
        Usuario usr = new Usuario(user, "localhost", " Se ha conectado");
        this.cliente = new Cliente(usr, this);

    }

}
