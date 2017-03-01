package br.com.pocomartins.bollyfilmes.data;

import android.provider.BaseColumns;

/**
 * Created by Poço Martins on 3/1/2017.
 */

public class FilmesContract {

    private FilmesContract() {}

    public static abstract class FilmesEntry implements BaseColumns {

        public static final String TABLE_NAME = "filmes";
        public static final String ID = "_id";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_CAPA_PATH = "capa";
        public static final String COLUMN_AVALIACAO = "avaliacao";

    }

}
