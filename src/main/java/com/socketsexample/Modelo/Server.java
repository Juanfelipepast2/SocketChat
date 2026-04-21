package com.socketsexample.Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.socketsexample.ServerController;

public class Server implements Runnable {

    private ServerSocket socketServidor;
    private ObjectInputStream entrada;
    private Socket reciver;
    private ObjectOutputStream salida;
    private ServerController controlador;

    public Server(ServerController controller) {

        try {
            this.controlador = controller;
            this.socketServidor = new ServerSocket(1024);

            this.controlador.agregar("Server: Bienvenido");

            System.out.println("Todo correcto");
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void enviarMensaje(String mensaje) {
        try {
            Usuario servidorUser = new Usuario("-----Server", null, mensaje);
            this.salida.writeObject(servidorUser);
            this.controlador.agregar(servidorUser.getUserName() + ": " + mensaje.strip());
            this.salida.reset();

        } catch (Exception e) {

        }
    }

    @Override
    public void run() {

        try {
            while (true) {
                this.reciver = socketServidor.accept();
                this.entrada = new ObjectInputStream(reciver.getInputStream());
                this.salida = new ObjectOutputStream(reciver.getOutputStream());
                Usuario user = (Usuario) entrada.readObject();
                this.controlador.agregar("Server: " + user.getUserName() + " se ha conectado.");

                leerMensajes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void leerMensajes() {
        try {

            Thread t = new Thread("lectura de mensajes") {
                @Override
                public void run() {
                    Usuario user = null;
                    boolean obs = true;
                    while (obs) {
                        try {

                            user = (Usuario) entrada.readObject();
                            controlador.agregar(user.getUserName() + ": " + user.getMensaje());
                            System.out.println(user.getUserName() + ": " + user.getMensaje());

                        } catch (SocketException e) {
                            controlador.agregar(user.getUserName() + " ha abandonado el chat.");
                            e.printStackTrace();
                            obs = false;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            obs = false;
                        } catch (IOException e){
                            e.printStackTrace();
                            obs = false;
                        }


                    }
                }

            };
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
