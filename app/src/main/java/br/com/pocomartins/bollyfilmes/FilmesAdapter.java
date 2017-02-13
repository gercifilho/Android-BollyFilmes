package br.com.pocomartins.bollyfilmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Poço Martins on 1/21/2017.
 */

public class FilmesAdapter extends ArrayAdapter<ItemFilme> {

    private static final int VIEM_TYPE_DESTAQUE = 0;
    private static final int VIEM_TYPE_ITEM = 1;
    private boolean useFilmeDestaque = false;



    public FilmesAdapter(Context context, ArrayList<ItemFilme> filmes) {
        super(context, 0, filmes);
    }

    public static class itemFilmeHolder {

        TextView titulo;
        TextView descricao;
        TextView dataLancamento;
        RatingBar avaliacao;
        ImageView poster;

        public itemFilmeHolder(View view) {

            titulo = (TextView) view.findViewById(R.id.item_titulo);
            descricao = (TextView) view.findViewById(R.id.item_desc);
            dataLancamento = (TextView) view.findViewById(R.id.item_data);
            avaliacao = (RatingBar) view.findViewById(R.id.item_avaliacao);
            poster = (ImageView) view.findViewById(R.id.item_poster);

            }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);
        ItemFilme filme = getItem(position);
        View itemView = convertView;


        switch (viewType) {
            case VIEM_TYPE_DESTAQUE: {

                itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_filme_destaque, parent, false);

                TextView titulo = (TextView) itemView.findViewById(R.id.item_titulo);
                titulo.setText(filme.getTitulo());

                RatingBar avaliacao = (RatingBar) itemView.findViewById(R.id.item_avaliacao);
                avaliacao.setRating(filme.getAvaliacao());
                break;
            }
            case VIEM_TYPE_ITEM: {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_filme, parent, false);

                itemFilmeHolder holder;
                if (itemView.getTag() == null) {
                    holder = new itemFilmeHolder(itemView);
                    itemView.setTag(holder);
                } else {
                    holder = (itemFilmeHolder) itemView.getTag();
                }

                holder.titulo.setText(filme.getTitulo());
                holder.descricao.setText(filme.getDescricao());
                holder.dataLancamento.setText(filme.getDataLancamento());
                holder.avaliacao.setRating(filme.getAvaliacao());

                break;
            }

        }
        return itemView;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && useFilmeDestaque? VIEM_TYPE_DESTAQUE : VIEM_TYPE_ITEM);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void setUseFilmeDestaque(boolean useFilmeDestaque) {
        this.useFilmeDestaque = useFilmeDestaque;
    }
}
