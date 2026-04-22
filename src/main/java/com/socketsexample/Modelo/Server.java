package com.socketsexample.Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;



public class Server{

    private ServerSocket socketServidor;
    private ArrayList<String> mensajes;
    private final ArrayList<ConexionCliente> clientes = new ArrayList<>();
    private BufferChat bufferChat;

    private static class ConexionCliente {

        private final Socket socket;
        private final ObjectInputStream entrada;
        private final ObjectOutputStream salida;

        ConexionCliente(Socket socket, ObjectInputStream entrada, ObjectOutputStream salida) {
            this.socket = socket;
            this.entrada = entrada;
            this.salida = salida;
        }
    }

    public Server() {
        this.bufferChat = new BufferChat();
    }

    public void Conectar() {

        try {            
            mensajes = new java.util.ArrayList<>();
            //this.controlador = controller;
            this.socketServidor = new ServerSocket(12345);                
            //this.controlador.agregar("Server: Bienvenido");            
            
            Thread conex = detectarConexion();
            conex.start();
            this.bufferChat.agregarMensaje("Server: Servidor iniciado en el puerto " + socketServidor.getLocalPort());
            System.out.println("Todo correcto");      
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void enviarMensaje(String mensaje) {
        try {
            Usuario servidorUser = new Usuario("-----Server", null, mensaje);
            synchronized (mensajes) {
                mensajes.add(servidorUser.getUserName() + ": " + mensaje);
                bufferChat.agregarMensaje(servidorUser.getUserName() + ": " + mensaje);
            }
            broadcast(servidorUser, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public Thread detectarConexion() {
        Thread t = null;
        try {
            t = new Thread("detector de conexiones") {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Socket reciver = socketServidor.accept();
                            ObjectInputStream entrada = new ObjectInputStream(reciver.getInputStream());
                            ObjectOutputStream salida = new ObjectOutputStream(reciver.getOutputStream());
                            ConexionCliente conexion = new ConexionCliente(reciver, entrada, salida);
                            synchronized (clientes) {
                                clientes.add(conexion);
                            }
                            iniciarLecturaCliente(conexion);
                            Thread.sleep(50);
                        } catch (IOException e) {
                            System.out.println("No hay nadie conectado");
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };            
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.setDaemon(true);
        return t;
    }

    private void iniciarLecturaCliente(ConexionCliente conexion) {
        Thread t = new Thread("lectura de mensajes cliente") {
            @Override
            public void run() {
                boolean activo = true;
                while (activo) {
                    try {
                        Usuario user = (Usuario) conexion.entrada.readObject();
                        if (user == null) {
                            continue;
                        }
                        synchronized (mensajes) {
                            mensajes.add(user.getUserName() + ": " + user.getMensaje());
                            bufferChat.agregarMensaje(user.getUserName() + ": " + user.getMensaje());
                        }
                        System.out.println(user.getUserName() + ": " + user.getMensaje());

                        broadcast(user, conexion);
                    } catch (ClassNotFoundException | IOException e) {
                        activo = false;
                        desconectar(conexion);
                    }
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }

    private void broadcast(Usuario user, ConexionCliente excluir) {
        ArrayList<ConexionCliente> snapshot;
        synchronized (clientes) {
            snapshot = new ArrayList<>(clientes);
        }

        for (ConexionCliente cliente : snapshot) {
            if (cliente == excluir) {
                continue;
            }
            try {
                cliente.salida.writeObject(user);
                cliente.salida.reset();
            } catch (IOException e) {
                desconectar(cliente);
            }
        }
    }

    private void desconectar(ConexionCliente conexion) {
        synchronized (clientes) {
            
            clientes.remove(conexion);
        }
        try {
            conexion.entrada.close();
        } catch (IOException ignored) {
        }
        try {
            conexion.salida.close();
        } catch (IOException ignored) {
        }
        try {
            conexion.socket.close();
        } catch (IOException ignored) {
        }
    }

    public String getUltimoMensaje() {
        return bufferChat.recibirMensaje();
    }
         

}

class BufferChat{
    private Queue<String> mensajes;

    public BufferChat() {
        mensajes = new LinkedList<>();        
    }

    public void agregarMensaje(String mensaje) {
        synchronized (mensajes) {
            mensajes.add(mensaje);
            try {
                mensajes.notify();
            } catch (Exception e) {
                e.printStackTrace();
                
            }
        }
    }

    public String recibirMensaje() {

        synchronized (mensajes) {
            while (mensajes.isEmpty()) {
                try {
                    mensajes.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }        
            return mensajes.poll();
        }
    }


}