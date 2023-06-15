package com.example.listviewsearch;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CustomListAdapter adapter;
    private ArrayList<Alumno> arrayList;
    private ArrayList<Alumno> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lstNombres);

        arrayList = new ArrayList<>();
        String[] nombres = getResources().getStringArray(R.array.array_nombres);
        String[] matriculas = getResources().getStringArray(R.array.array_matriculas);
        for (int i = 0; i < nombres.length; i++) {
            String nombre = nombres[i];
            String matricula = matriculas[i];
            String imagenNombre = "img_alumno" + (i + 1);
            int imagenId = getResources().getIdentifier(imagenNombre, "drawable", getPackageName());
            Alumno alumno = new Alumno(nombre, matricula, imagenId);
            arrayList.add(alumno);
        }

        filteredList = new ArrayList<>(arrayList);

        adapter = new CustomListAdapter(this, R.layout.list_item, filteredList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alumno selectedAlumno = filteredList.get(position);
                String selectedName = selectedAlumno.getNombre();
                Toast.makeText(MainActivity.this, "Seleccionó el alumno: " + selectedName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList.clear();
                if (newText.isEmpty()) {
                    filteredList.addAll(arrayList);
                } else {
                    String filterPattern = newText.toLowerCase().trim();
                    for (Alumno alumno : arrayList) {
                        if (alumno.getNombre().toLowerCase().contains(filterPattern) ||
                                alumno.getMatricula().toLowerCase().contains(filterPattern)) {
                            filteredList.add(alumno);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private class CustomListAdapter extends ArrayAdapter<Alumno> implements Filterable {
        private int layoutResourceId;
        private ArrayList<Alumno> data;
        private ArrayList<Alumno> originalData;

        public CustomListAdapter(Context context, int layoutResourceId, ArrayList<Alumno> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.data = data;
            this.originalData = new ArrayList<>(data);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Alumno getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ViewHolder();
                holder.imgFoto = row.findViewById(R.id.imgFoto);
                holder.txtNombre = row.findViewById(R.id.txtNombre);
                holder.txtMatricula = row.findViewById(R.id.txtMatricula);

                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            Alumno alumno = data.get(position);
            holder.txtNombre.setText(alumno.getNombre());
            holder.txtMatricula.setText(alumno.getMatricula());

            int imagenId = alumno.getImagenId();
            if (imagenId != 0) {
                holder.imgFoto.setImageResource(imagenId);
            } else {
                holder.imgFoto.setImageResource(R.drawable.img_alumno1);
            }

            return row;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();

                    ArrayList<Alumno> filteredList = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        filteredList.addAll(originalData);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (Alumno alumno : originalData) {
                            if (alumno.getNombre().toLowerCase().contains(filterPattern) ||
                                    alumno.getMatricula().toLowerCase().contains(filterPattern)) {
                                filteredList.add(alumno);
                            }
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredList.clear();
                    filteredList.addAll((ArrayList<Alumno>) results.values);
                    notifyDataSetChanged();
                }
            };
        }

        private class ViewHolder {
            ImageView imgFoto;
            TextView txtNombre;
            TextView txtMatricula;
        }
    }

    public class Alumno {
        private String nombre;
        private String matricula;
        private int imagenId;

        public Alumno(String nombre, String matricula, int imagenId) {
            this.nombre = nombre;
            this.matricula = matricula;
            this.imagenId = imagenId;
        }

        public String getNombre() {
            return nombre;
        }

        public String getMatricula() {
            return matricula;
        }

        public int getImagenId() {
            return imagenId;
        }
    }
}
