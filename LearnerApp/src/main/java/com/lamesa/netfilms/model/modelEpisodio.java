package com.lamesa.netfilms.model;

import java.io.Serializable;

public class modelEpisodio implements Serializable {


    String id;
    String idEpisodio;
    String nombre;


    public modelEpisodio(){

    }

    public modelEpisodio(String idFilm, String idEpisodio, String nombre, String idioma) {

        this.id = idFilm;
        this.idEpisodio = idEpisodio;
        this.nombre = nombre;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIdEpisodio() {
        return idEpisodio;
    }

    public void setIdEpisodio(String idEpisodio) {
        this.idEpisodio = idEpisodio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }





}
