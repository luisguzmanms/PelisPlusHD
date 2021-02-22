package com.lamesa.netfilms.model;

import java.io.Serializable;

public class modelFilm implements Serializable {


    String id;
    String imagen;
    String nombre;
    String tipo;
    String calidad;
    String fechaActualizado;


    public modelFilm(){

    }

    public modelFilm(String id, String nombre, String tipo, String calidad, String imagen, String fechaActualizado) {

        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.tipo = tipo;
        this.calidad = calidad;
        this.fechaActualizado = fechaActualizado;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public String getFechaActualizado() {
        return fechaActualizado;
    }

    public void setFechaActualizado(String id) {
        this.fechaActualizado = fechaActualizado;
    }

}
