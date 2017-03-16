package br.com.pocomartins.bollyfilmes.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Poço Martins on 3/1/2017.
 */

public class FilmesContract {

    public static final String CONTENT_AUTHORITY = "br.com.pocomartins.bollyfilmes";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FILMES = "filmes";

    private FilmesContract() {}

    public static abstract class FilmesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILMES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_FILMES;

        public static  final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_FILMES;

        public static final String TABLE_NAME = "filmes";

        public static final String ID = "_id";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_CAPA_PATH = "capa";
        public static final String COLUMN_AVALIACAO = "avaliacao";
        public static final String COLUMN_DATA_LANC = "dataLancamento";
        public static final String COLUMN_POPULARIDADE = "popularidade";

        public static Uri buildUriForFilmes() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildUriForFilmes(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

}
