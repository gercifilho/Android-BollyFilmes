package br.com.pocomartins.bollyfilmes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_filmes);
        ArrayList<ItemFilme> arrayList = new ArrayList<>();
        arrayList.add(new ItemFilme("Homem Aranha", "Filme de heroi picado por uma aranha", "10/04/2017", 4));
        arrayList.add(new ItemFilme("Capitão America", "Filme de heroi picado por uma aranha", "11/04/2017", 5));
        arrayList.add(new ItemFilme("Super Amigos", "Filme de heroi picado por uma aranha", "12/04/2017", 3.5f));
        arrayList.add(new ItemFilme("Avangers", "Filme de heroi picado por uma aranha", "13/04/2017", 5));
        arrayList.add(new ItemFilme("Homem de Ferro", "Filme de heroi picado por uma aranha", "15/04/2017", 5));
        arrayList.add(new ItemFilme("Hulk", "Filme de heroi picado por uma aranha", "20/04/2017", 2));
        arrayList.add(new ItemFilme("Homem Formiga", "Filme de heroi picado por uma aranha", "13/04/2017", 4.5f));

        FilmesAdapter adapter = new FilmesAdapter(this, arrayList);
        listView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_atualizar:
                Toast.makeText(this, "Atualizando os filmes.....", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
