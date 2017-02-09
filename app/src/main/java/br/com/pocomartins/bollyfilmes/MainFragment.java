package br.com.pocomartins.bollyfilmes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_filmes);
        final ArrayList<ItemFilme> arrayList = new ArrayList<>();
        arrayList.add(new ItemFilme("Homem Aranha", "Filme de heroi picado por uma aranha", "10/04/2017", 4));
        arrayList.add(new ItemFilme("Capitão America", "Filme de heroi picado por uma capitao america", "11/04/2017", 5));
        arrayList.add(new ItemFilme("Super Amigos", "Filme de heroi picado por uma super amigo", "12/04/2017", 3.5f));
        arrayList.add(new ItemFilme("Avangers", "Filme de heroi picado por uma Avangers", "13/04/2017", 5));
        arrayList.add(new ItemFilme("Homem de Ferro", "Filme de heroi picado por um Homem de Ferro", "15/04/2017", 5));
        arrayList.add(new ItemFilme("Hulk", "Filme de heroi picado por um Hulk", "20/04/2017", 2));
        arrayList.add(new ItemFilme("Homem Formiga", "Filme de heroi picado por um Homem Formiga", "13/04/2017", 4.5f));

        FilmesAdapter adapter = new FilmesAdapter(getContext(), arrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemFilme itemFilme = arrayList.get(position);
                Intent intent = new Intent(getContext(), FilmeDetalheActivity.class);
                intent.putExtra(MainActivity.KEY_FILME, itemFilme);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_atualizar:
                Toast.makeText(getContext(), "Atualizando os filmes.....", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
