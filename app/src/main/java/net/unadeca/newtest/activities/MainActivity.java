package net.unadeca.newtest.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.unadeca.newtest.R;
import net.unadeca.newtest.database.models.Arbolito;
import net.unadeca.newtest.database.models.Arbolito_Table;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //conecta el diseño con la vista
    private ListView lista;
    private CoordinatorLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //aquí se conecta la vista
        lista = findViewById(R.id.lista);
        view = findViewById(R.id.content);
        setAdapter();

       // Arbolito pino = new Arbolito();
       // pino.altura = 4;
        //pino.fecha_plantado = "2019-01-01";
        //pino.fecha_ultima_revicion = "2019-02-10";
        //pino.plantado_por = " Juan Martinez ";
        //pino.save();

        //Arbolito cedro = new Arbolito();
        //cedro.altura = 10;
        //cedro.fecha_plantado = "2017-01-01";
        //cedro.fecha_ultima_revicion = "2019-02-01";
        //cedro.plantado_por = " Martin Perez ";
        //cedro.save();


        borrarArbolito();

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
               // long contadorArbolitos = SQLite.selectCountOf().from(Arbolito.class).count();
                // Snackbar.make(view, "En este momento hay"+ contadorArbolitos + "arbolitos registrados"
                // , Snackbar.LENGTH_LONG);

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

    private List<Arbolito> getListArbolitos(){
        return  SQLite.select().from(Arbolito.class).queryList();
    }

    //Establecemos el adaptador
    private void setAdapter(){
        //Adaptador de arreglo
      //  lista.setAdapter( new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getArbolitos()));

        lista.setAdapter(new CustomAdapter(getListArbolitos(),getApplicationContext(), view));
    }
    // mostrando un Diálogo utilzando la funconalidad de Android
    public void mostrarDialog(){
        //Para personlizar un dialogo
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        //Hacemos una vista
        View formulario = layoutInflater.inflate(R.layout.formulario, null);
        //Hacemos que el diálogo se conecte con el diseño


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Establecemos la vista como parte del diálogo
        builder.setView(formulario);
        final TextInputLayout altura = formulario.findViewById(R.id.txtAltura);
        final TextInputLayout siembra = formulario.findViewById(R.id.txtfechaSiembra);
        final TextInputLayout rebicion = formulario.findViewById(R.id.txtcheck);
        final TextInputLayout encargado = formulario.findViewById(R.id.txtEncargado);

        builder.setMessage("Rellene toda la información solicitada")
                .setTitle("Crear nuevo arbolito")
                .setCancelable(false)
        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    validate(altura, siembra, rebicion, encargado);
                    guardarABD(altura, siembra, rebicion, encargado);
                }catch (Exception e){
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
                }
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
    //Métodos para validar la información
    private void validate(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e) throws Exception{
        if (a.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir la altura del arbolito");

        if (s.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir la fecha de siembra del arbolito");

        if (c.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir la fecha de check del arbolito");

        if (e.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir el nombre del encargado del arbolito");

    }
    //Método guardar
    private void guardarABD(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e){
        Arbolito arbolito = new Arbolito();
        arbolito.altura = Integer.parseInt(a.getEditText().getText().toString());
        arbolito.fecha_plantado = s.getEditText().getText().toString();
        arbolito.fecha_ultima_revicion = c.getEditText().getText().toString();
        arbolito.plantado_por = e.getEditText().getText().toString();
        arbolito.save();

        Snackbar.make(view, "Se ha guardado el arbolito", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        setAdapter();

    }
    //Método para borrar
    private void borrarArbolito(){
        //Este se usa si queremos borrar toda la tabla
        //*Delete.table(Arbolito.class);
        //Este borra dependiendo de una consulta
        SQLite.delete().from(Arbolito.class).where(Arbolito_Table.altura.between(1).and(10
        )).execute();
        setAdapter();

        Snackbar.make(view, "Hemos borrado el listado de arbolitos", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }



}
