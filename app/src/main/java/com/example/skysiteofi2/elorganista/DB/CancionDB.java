package com.example.skysiteofi2.elorganista.DB;

import com.orm.SugarRecord;

/**
 * Created by skysiteofi2 on 17/5/17.
 */

public class CancionDB extends SugarRecord<CancionDB> {
    String titulo;
    String IdCancion;
    SubNivelDB subnivel;

    public CancionDB(){
    }



    public CancionDB(String titulo, String id_cancion, SubNivelDB subnivel) {
        this.titulo = titulo;
        this.IdCancion = id_cancion;
        this.subnivel = subnivel;
    }
    public String getTitulo() {
        return titulo;
    }

    public String getIdCancion() {
        return IdCancion;
    }

    public SubNivelDB getSubnivel() {
        return subnivel;
    }
}
