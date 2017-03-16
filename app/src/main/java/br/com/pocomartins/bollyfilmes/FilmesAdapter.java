package br.com.pocomartins.bollyfilmes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.pocomartins.bollyfilmes.data.FilmesContract;

/**
 * Created by Poço Martins on 1/21/2017.
 */

public class FilmesAdapter extends CursorAdapter {

    private static final int VIEM_TYPE_DESTAQUE = 0;
    private static final int VIEM_TYPE_ITEM = 1;
    private boolean useFilmeDestaque = false;



    public FilmesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    public static class itemFilmeHolder {

        TextView titulo;
        TextView descricao;
        TextView dataLancamento;
        RatingBar avaliacao;
        ImageView poster;
        ImageView capa;

        public itemFilmeHolder(View view) {

            titulo = (TextView) view.findViewById(R.id.item_titulo);
            descricao = (TextView) view.findViewById(R.id.item_desc);
            dataLancamento = (TextView) view.findViewById(R.id.item_data);
            avaliacao = (RatingBar) view.findViewById(R.id.item_avaliacao);
            poster = (ImageView) view.findViewById(R.id.item_poster);
            capa = (ImageView) view.findViewById(R.id.item_capa);

            }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType){
            case VIEM_TYPE_DESTAQUE: {
                layoutId = R.layout.item_filme_destaque;
                break;
            }
            case VIEM_TYPE_ITEM: {
                layoutId = R.layout.item_filme;
                break;
            }
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        itemFilmeHolder holder = new itemFilmeHolder(view);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        itemFilmeHolder holder = (itemFilmeHolder) view.getTag();
        int viewType = getItemViewType(cursor.getPosition());

        int tituloIndex = cursor.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_TITULO);
        int descricaoIndex = cursor.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_DESCRICAO);
        int posterIndex = cursor.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_POSTER_PATH);
        int capaIndex = cursor.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_CAPA_PATH);
        int dataIndex= cursor.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_DATA_LANC);
        int avaliacaoIndex = cursor.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_AVALIACAO);

        switch (viewType) {
            case VIEM_TYPE_DESTAQUE: {
                holder.titulo.setText(cursor.getString(tituloIndex));
                holder.avaliacao.setRating(cursor.getFloat(avaliacaoIndex));
                new DownloadImageTask(holder.capa).execute(cursor.getString(capaIndex));
                break;
            }
            case VIEM_TYPE_ITEM: {
                holder.titulo.setText(cursor.getString(tituloIndex));
                holder.descricao.setText(cursor.getString(descricaoIndex));
                holder.dataLancamento.setText(cursor.getString(dataIndex));
                holder.avaliacao.setRating(cursor.getFloat(avaliacaoIndex));
                new DownloadImageTask(holder.poster).execute(cursor.getString(posterIndex));
                break;
            }
        }

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
