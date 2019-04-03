package net.unadeca.newtest.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.unadeca.newtest.R;
import net.unadeca.newtest.database.models.Arbolito;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Arbolito> {

    private List<Arbolito> dataSet;
    Context mContext;
    CoordinatorLayout view;

    // View lookup cache
    private static class ViewHolder {
        TextView txtArbolito;
        ImageView delete;
        ImageView update;

        }

    public CustomAdapter(List<Arbolito> data, Context context, CoordinatorLayout l) {
        super(context, R.layout.item, data);
        this.dataSet = data;
        this.mContext=context;
        this.view = l;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Arbolito dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder.txtArbolito =  convertView.findViewById(R.id.text);
            viewHolder.delete =  convertView.findViewById(R.id.delete);
            viewHolder.update =  convertView.findViewById(R.id.imagen);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtArbolito.setText(dataModel.toString());
        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog(dataModel);

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dataModel.delete();
                 dataSet.remove(dataModel);
                 notifyDataSetChanged();
                Toast.makeText(getContext(), "Se eliminó el arbolito", Toast.LENGTH_LONG).show();


            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    public void mostrarDialog(final Arbolito arbol){
        //Para personlizar un dialogo
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        //Hacemos una vista
        View formulario = layoutInflater.inflate(R.layout.formulario, null);
        //Hacemos que el diálogo se conecte con el diseño


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //Establecemos la vista como parte del diálogo
        builder.setView(formulario);
        final TextInputLayout altura = formulario.findViewById(R.id.txtAltura);
        altura.getEditText().setText(arbol.altura);
        final TextInputLayout siembra = formulario.findViewById(R.id.txtfechaSiembra);
        siembra.getEditText().setText(arbol.fecha_plantado);
        final TextInputLayout rebicion = formulario.findViewById(R.id.txtcheck);
        rebicion.getEditText().setText(arbol.fecha_ultima_revicion);
        final TextInputLayout encargado = formulario.findViewById(R.id.txtEncargado);
        encargado.getEditText().setText(arbol.plantado_por);

        builder.setMessage("Rellene toda la información solicitada")
                .setTitle("Crear nuevo arbolito")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            validate(altura, siembra, rebicion, encargado);
                            guardarABD(altura, siembra, rebicion, encargado,arbol);
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
    private void guardarABD(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e,
    Arbolito arbolito){

        arbolito.altura = Integer.parseInt(a.getEditText().getText().toString());
        arbolito.fecha_plantado = s.getEditText().getText().toString();
        arbolito.fecha_ultima_revicion = c.getEditText().getText().toString();
        arbolito.plantado_por = e.getEditText().getText().toString();
        arbolito.save();

        Snackbar.make(view, "Se ha modificado el arbolito", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        notifyDataSetChanged();

    }
}