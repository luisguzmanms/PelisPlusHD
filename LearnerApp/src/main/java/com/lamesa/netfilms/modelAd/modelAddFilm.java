package com.lamesa.netfilms.modelAd;

import java.io.Serializable;

public class modelAddFilm implements Serializable {

    String estado;
    String imagen;
    String nombre;
    String tipo;
    String calidad;
    String sipnosis;
    String ano;
    String puntaje;
    String categoria;
    String red;
    String fechaActualizado;
    String id;

    public modelAddFilm(){

    }

    public modelAddFilm(String id, String nombre, String sipnosis, String tipo, String calidad, String categoria, String ano, String puntaje, String imagen, String red, String estado, String fechaActualizado) {

        this.id = id;
        this.nombre = nombre;
        this.sipnosis = sipnosis;
        this.imagen = imagen;
        this.tipo = tipo;
        this.calidad = calidad;
        this.ano = ano;
        this.categoria = categoria;
        this.puntaje = puntaje;
        this.red = red;
        this.estado = estado;
        this.fechaActualizado = fechaActualizado;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSipnosis() {
        return sipnosis;
    }

    public void setSipnosis(String sipnosis) {
        this.sipnosis = sipnosis;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
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

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getFechaActualizado() {
        return fechaActualizado;
    }

    public void setFechaActualizado(String fechaActualizado) {
        this.fechaActualizado = fechaActualizado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
