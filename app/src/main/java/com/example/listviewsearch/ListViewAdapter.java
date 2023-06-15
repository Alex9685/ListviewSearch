package com.example.listviewsearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<AlumnoData> {
    private Activity context;
    private int layoutResourceId;
    private ArrayList<AlumnoData> list;
    private LayoutInflater inflater;

    public ListViewAdapter(Activity context, int layoutResourceId, ArrayList<AlumnoData> list) {
        super(context, layoutResourceId, list);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.imgFoto = row.findViewById(R.id.imgFoto);
            holder.txtNombre = row.findViewById(R.id.txtNombre);
            holder.txtMatricula = row.findViewById(R.id.txtMatricula);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        AlumnoData alumno = list.get(position);
        holder.txtNombre.setText(alumno.getNombreCompleto());
        holder.txtMatricula.setText(alumno.getMatricula());

        String imagenNombre = "img_" + alumno.getMatricula();
        int imagenId = context.getResources().getIdentifier(imagenNombre, "drawable", context.getPackageName());

        if (imagenId != 0) {
            holder.imgFoto.setImageResource(imagenId);
        } else {
            // Si no se encuentra la imagen, puedes establecer una imagen predeterminada o dejarla en blanco
        }

        return row;
    }

    static class ViewHolder {
        ImageView imgFoto;
        TextView txtNombre;
        TextView txtMatricula;
    }
}
