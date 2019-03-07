package net.unadeca.newtest.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.unadeca.newtest.R;
import net.unadeca.newtest.database.models.Arbolito;
import net.unadeca.newtest.database.models.Arbolito_Table;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //conecta el diseño con la vista
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //aquí se conecta la vista
        lista = findViewById(R.id.lista);

        setAdapter();
        Arbolito pino = new Arbolito();
        pino.altura = 4;
        pino.fecha_plantado = "2019-01-01";
        pino.fecha_ultima_revicion = "2019-02-10";
        pino.plantado_por = " Juan Martinez ";
        pino.save();

        Arbolito cedro = new Arbolito();
        cedro.altura = 10;
        cedro.fecha_plantado = "2017-01-01";
        cedro.fecha_ultima_revicion = "2019-02-01";
        cedro.plantado_por = " Martin Perez ";
        cedro.save();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Ejem 1
                //* Arbolito test = SQLite.select().from(Arbolito.class).querySingle();
                // Arbolito cedro = SQLite.select().from(Arbolito.class).where(Arbolito_Table.altura.eq(10)).querySingle();
                //Snackbar.make(view, cedro.altura +"" + cedro.plantado_por , Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
                //cedro.plantado_por = "Pablito";
                //cedro.save();

                //Ejem 2
                //long contadorArbolitos = SQLite.selectCountOf().from(Arbolito.class).count();
                // Snackbar.make(view, "En este momento hay"+ contadorArbolitos + "arbolitos registrados"  , Snackbar.LENGTH_LONG)

                // .setAction("Action", null).show();


                //Implementación del dialogo
                mostrarDialog();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //arreglo string
    private String[] getArbolitos(){
        List<Arbolito> listado = SQLite.select().from(Arbolito.class).queryList();
        String[] array = new String[listado.size()];
        for (int c = 0; c< listado.size(); c++){
         array[c]= listado.get(c).toString();

        }
        return array;



    }
    //Establecemos el adaptador
    private void setAdapter(){
        lista.setAdapter( new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getArbolitos()));
    }

    public void mostrarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Este es un mensaje de prueba")
                .setTitle("Alerta!!!")
                .setCancelable(false)
        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }
}
