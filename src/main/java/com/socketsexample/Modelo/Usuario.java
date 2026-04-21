package com.socketsexample.Modelo;

import java.io.Serializable;

public class Usuario implements Serializable{
    String userName;
    String ip;
    String Mensaje;


    public Usuario(String userName, String ip, String mensaje) {
        this.userName = userName;
        this.ip = ip;
        Mensaje = mensaje;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    
    


    

}
