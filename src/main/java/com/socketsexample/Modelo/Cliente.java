package com.socketsexample.Modelo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.socketsexample.ChatController;


//TODO: EL CLIENTE DEBE MANEJARSE SOLO, NO DEBE TENER REFERENCIA AL CONTROLADOR, SOLO DEBE ENVIAR LOS MENSAJES Y RECIBIRLOS,
//TODO  EL CONTROLADOR DEBE SER EL ENCARGADO DE MOSTRAR LOS MENSAJES EN LA INTERFAZ GRAFICA
public class Cliente {
    private ChatController controlador;
    private Usuario usr;
    private Socket socketCliente;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    public Cliente(Usuario usr, ChatController ct) {
        try {
            this.usr = usr;
            this.socketCliente = new Socket(usr.getIp(), 12345);

            this.salida = new ObjectOutputStream(socketCliente.getOutputStream());
            this.controlador = ct;
            this.salida.writeObject((Usuario) usr);
            this.salida.reset();
            this.leerMensaes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(String mensaje) {
        String mensajeFinal = null;
        try {
            usr.setMensaje(mensaje);
            mensajeFinal = this.usr.getUserName() + ": " + this.usr.getMensaje();
            controlador.agregar(mensajeFinal);
            this.salida.writeObject((Usuario) usr);
            this.salida.reset();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leerMensaes() {
        Thread hilo = new Thread() {
            @Override
            public void run() {
                try {
                    boolean obs = true;
                    entrada = new ObjectInputStream(socketCliente.getInputStream()); 
                    while (obs) {
                        try {
                            
                            Usuario user = (Usuario) entrada.readObject();

                            controlador.agregar(user.getUserName() + ": " + user.getMensaje());
                            

                        } catch (Exception e) {
                            e.printStackTrace();
                            obs = false;
                        }

                    }
                } catch (Exception e) {

                }
            }
        };
        hilo.setDaemon(true);
        hilo.start();

    }

}
