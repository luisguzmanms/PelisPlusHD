package com.lamesa.netfilms.model;

import java.io.Serializable;

public class modelCategoria implements Serializable {

    String imagen;
    String nombre;


    public modelCategoria(){

    }

    public modelCategoria( String imagen, String nombre)  {

        this.imagen = imagen;
        this.nombre = nombre;

    }




    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
