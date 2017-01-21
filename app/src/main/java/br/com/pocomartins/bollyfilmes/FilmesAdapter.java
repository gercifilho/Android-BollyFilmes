package br.com.pocomartins.bollyfilmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Poço Martins on 1/21/2017.
 */

public class FilmesAdapter extends ArrayAdapter<ItemFilme> {

    public FilmesAdapter(Context context, ArrayList<ItemFilme> filmes) {
        super(context, 0, filmes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        if (itemView==null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_filme, parent, false);
        }

        ItemFilme filme = getItem(position);

        TextView titulo = (TextView) itemView.findViewById(R.id.item_titulo);
        titulo.setText(filme.getTitulo());

        TextView descricao = (TextView) itemView.findViewById(R.id.item_desc);
        descricao.setText(filme.getDescricao());

        TextView dataLancamento = (TextView) itemView.findViewById(R.id.item_data);
        dataLancamento.setText(filme.getDataLancamento());

        RatingBar avaliacao = (RatingBar) itemView.findViewById(R.id.item_avaliacao);
        avaliacao.setRating(filme.getAvaliacao());

        return itemView;

    }
}
