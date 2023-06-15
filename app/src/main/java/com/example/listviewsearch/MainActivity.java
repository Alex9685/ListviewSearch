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
import java.util.Arrays;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CustomListAdapter adapter;
    private ArrayList<String> arrayList;
    private ArrayList<String> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lstNombres);

        arrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_nombres)));
        filteredList = new ArrayList<>(arrayList);

        adapter = new CustomListAdapter(this, R.layout.list_item, filteredList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = filteredList.get(position);
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
                    for (String name : arrayList) {
                        if (name.toLowerCase().contains(filterPattern)) {
                            filteredList.add(name);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private class CustomListAdapter extends ArrayAdapter<String> implements Filterable {
        private int layoutResourceId;
        private ArrayList<String> data;
        private ArrayList<String> originalData;

        public CustomListAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
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
        public String getItem(int position) {
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

            String name = data.get(position);
            holder.txtNombre.setText(name);
            holder.txtMatricula.setText(getResources().getStringArray(R.array.array_matriculas)[position]);

            String imagenNombre = "img_alumno" + (position + 1);
            int imagenId = getResources().getIdentifier(imagenNombre, "drawable", getPackageName());

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

                    ArrayList<String> filteredList = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        filteredList.addAll(originalData);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (String name : originalData) {
                            if (name.toLowerCase().contains(filterPattern)) {
                                filteredList.add(name);
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
                    filteredList.addAll((ArrayList<String>) results.values);
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
}
