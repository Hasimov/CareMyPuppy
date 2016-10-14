package com.carepuppy.pirtu.caremypuppy.Models;

/**
 * Created by pirtu on 24/09/2016.
 */
public class ConversacionPojo {

    private String nombre_cuidador;
    private String url_carerAvatar;
    private String fecha_lastmessage;

    public ConversacionPojo() {
    }

    public ConversacionPojo(String nombre_cuidador, String url_carerAvatar, String fecha_lastmessage) {
        this.nombre_cuidador = nombre_cuidador;
        this.url_carerAvatar = url_carerAvatar;
        this.fecha_lastmessage = fecha_lastmessage;
    }

    public String getNombre_cuidador() {
        return nombre_cuidador;
    }

    public void setNombre_cuidador(String nombre_cuidador) {
        this.nombre_cuidador = nombre_cuidador;
    }

    public String getUrl_carerAvatar() {
        return url_carerAvatar;
    }

    public void setUrl_carerAvatar(String url_carerAvatar) {
        this.url_carerAvatar = url_carerAvatar;
    }

    public String getFecha_lastmessage() {
        return fecha_lastmessage;
    }

    public void setFecha_lastmessage(String fecha_lastmessage) {
        this.fecha_lastmessage = fecha_lastmessage;
    }
}
