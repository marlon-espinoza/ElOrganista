package com.example.skysiteofi2.elorganista.DB;

import com.orm.SugarRecord;

/**
 * Created by skysiteofi2 on 17/5/17.
 */

public class VideoDB extends SugarRecord {
    String IdVideo;
    String titulo;
    String descripcion;
    String url;
    CancionDB cancion;

    public VideoDB() {
    }
    public VideoDB(String idVideo, String titulo, String descripcion, String url, CancionDB cancion) {
        IdVideo = idVideo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.cancion = cancion;
    }



    public CancionDB getCancion() {
        return cancion;
    }

    public String getIdVideo() {
        return IdVideo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url;
    }
}
