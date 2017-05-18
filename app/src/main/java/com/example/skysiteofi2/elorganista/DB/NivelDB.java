package com.example.skysiteofi2.elorganista.DB;

import com.example.skysiteofi2.elorganista.Nivel;
import com.orm.SugarRecord;

/**
 * Created by skysiteofi2 on 17/5/17.
 */

public class NivelDB extends SugarRecord<NivelDB>   {
    String titulo;
    String IdNivel;



    public NivelDB(){
    }

    public NivelDB(String id,String titulo){
        this.titulo = titulo;
        this.IdNivel = id;
    }
    public String getTitulo() {
        return titulo;
    }

    public String getIdNivel() {
        return IdNivel;
    }
}
