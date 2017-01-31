package br.com.pocomartins.bollyfilmes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class FilmeDetalheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme_detalhe);

        Intent intent = getIntent();
        ItemFilme itemFilme = (ItemFilme) intent.getSerializableExtra(MainActivity.KEY_FILME);

        TextView titulo = (TextView) findViewById(R.id.item_titulo);
        titulo.setText(itemFilme.getTitulo());

        TextView data = (TextView) findViewById(R.id.item_data);
        data.setText(itemFilme.getDataLancamento());

        TextView descricao = (TextView) findViewById(R.id.item_desc);
        descricao.setText(itemFilme.getTitulo());

        RatingBar avaliacao = (RatingBar) findViewById(R.id.item_avaliacao);
        avaliacao.setRating(itemFilme.getAvaliacao());

        Button btntrailler = (Button) findViewById(R.id.item_btn_trailler);

    }
}
