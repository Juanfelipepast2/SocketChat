package com.socketsexample;



import com.socketsexample.Modelo.Server;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;



public class ServerController{

    @FXML
    private Button botonEnviar;

    @FXML
    private TextArea cajaDeMensajes;

    @FXML
    private TextArea cajaTexto;

    @FXML
    private Pane panelBase;

    Server sv;


    @FXML
    void keyHandlers(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            msg(cajaTexto.getText());
        }
    }

    @FXML
    void enviarMensaje(ActionEvent event) {
        msg(cajaTexto.getText());
    }

    


    public void agregar(String userMensaje){
        String mensaje =  userMensaje;

        if(cajaDeMensajes.getText().isEmpty() || cajaDeMensajes.getText().isEmpty()){
            cajaDeMensajes.setText(mensaje);
        } else {
            cajaDeMensajes.setText(cajaDeMensajes.getText().strip() + "\n" + mensaje);
        }
        cajaDeMensajes.setScrollTop(Double.MAX_VALUE);
    }

    void msg(String mensaje) {
        
        if (!cajaTexto.getText().isEmpty() && !cajaTexto.getText().isBlank()) {
            
            sv.enviarMensaje(mensaje);
            limpiarCajaTexto();
            cajaDeMensajes.setScrollTop(Double.MAX_VALUE);
        } else {
            System.out.println("Caja de texto vacia");
        }

    }

    public void limpiarCajaTexto() {
        // System.out.println("XD");
        cajaTexto.setText("");
    }


    public void init(Server server) {
        try {
            sv = server;    
            actualizarMensajes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }




    public void actualizarMensajes() {
        try {
            Thread t = new Thread("actualizacion de mensajes") {
                @Override
                public void run() {
                    while (true) {
                        
                        String ultimoMensaje = sv.getUltimoMensaje();
                        if (ultimoMensaje != null) {
                            agregar(ultimoMensaje);
                        }
                    }
                }
            };
            t.setDaemon(true);
            t.start();                                                            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
