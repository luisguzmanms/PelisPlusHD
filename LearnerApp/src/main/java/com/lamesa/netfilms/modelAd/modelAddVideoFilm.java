package com.lamesa.netfilms.modelAd;

import java.io.Serializable;

public class modelAddVideoFilm implements Serializable {


    public  String id;
    public  String idioma;
    public  String idEpisodio;
    public  String nombreEpisodio;
    public  String linkVideo;



    public modelAddVideoFilm(){

    }

    public modelAddVideoFilm(String id, String idioma, String idEpisodio, String nombreEpisodio, String linkVideo) {
        this.id = id;
        this.idioma = idioma;
        this.idEpisodio = idEpisodio;
        this.nombreEpisodio = nombreEpisodio;
        this.linkVideo = linkVideo;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdEpisodio() {
        return idEpisodio;
    }

    public void setIdEpisodio(String idEpisodio) {
        this.idEpisodio = idEpisodio;
    }


    public String getNombreEpisodio() {
        return nombreEpisodio;
    }

    public void setNombreEpisodio(String nombreEpisodio) {
        this.nombreEpisodio = nombreEpisodio;
    }


    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }





}
