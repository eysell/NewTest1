package net.unadeca.newtest.database.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.unadeca.newtest.database.TestDatabase;

import java.util.Locale;

@Table(database = TestDatabase.class)
public class Arbolito extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public int altura;

    @Column
    public String fecha_plantado;

    @Column
    public String fecha_ultima_revicion;

    @Column
    public String plantado_por;


    public String toString(){
        return String.format(Locale.getDefault(), "altura: %d\nfecha_plantado: " +
                "%s\nEncargado: %s", this.altura, this.fecha_plantado, this.plantado_por);
    }
}
