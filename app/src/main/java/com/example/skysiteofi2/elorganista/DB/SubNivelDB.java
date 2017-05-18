package com.example.skysiteofi2.elorganista.DB;

import com.orm.SugarRecord;

/**
 * Created by skysiteofi2 on 17/5/17.
 */

public class SubNivelDB extends SugarRecord<SubNivelDB> {
    String titulo;
    NivelDB nivel;
    String IdSubnivel;

    public SubNivelDB(){}

    public SubNivelDB(String titulo, NivelDB nivel, String IdSubnivel){
        this.titulo = titulo;
        this.nivel = nivel;
        this.IdSubnivel = IdSubnivel;
    }
    public String getTitulo() {
        return titulo;
    }

    public NivelDB getNivel() {
        return nivel;
    }
    public String getIdSubnivel() {
        return IdSubnivel;
    }
}
