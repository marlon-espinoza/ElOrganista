package com.example.skysiteofi2.elorganista;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by skysiteofi2 on 27/10/16.
 */

public class VideoItem {
    public String titulo;
    public String descripcion;
    public String url;
    public String imagen;


    public Bitmap vista;

    public VideoItem(String titulo, String descripcion, String url, Bitmap vista) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.vista = vista;
    }

    public VideoItem(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getUrl() { return url;    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Bitmap getVista() {
        return vista;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setVista(Bitmap vista) {
        this.vista = vista;
    }
}
