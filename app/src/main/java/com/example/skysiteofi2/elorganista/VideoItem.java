package com.example.skysiteofi2.elorganista;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by skysiteofi2 on 27/10/16.
 */

public class VideoItem {
    public String id;
    public String titulo;
    public String descripcion;
    public String url;
    public String imagen;
    public Bitmap vista;
    public Boolean guardado;
    public Boolean guardando=false;

    public Boolean getGuardando() {
        return guardando;
    }

    public void setGuardando(Boolean guardando) {
        this.guardando = guardando;
    }

    public VideoItem(String id, String titulo, String descripcion, String url, Bitmap vista) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.imagen = imagen;
        this.vista = vista;
    }



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

    public String getId() {  return id;   }

    public void setId(String id) {   this.id = id;   }

    public Boolean getGuardado() {
        return guardado;
    }

    public void setGuardado(Boolean guardado) {
        this.guardado = guardado;
    }
}
