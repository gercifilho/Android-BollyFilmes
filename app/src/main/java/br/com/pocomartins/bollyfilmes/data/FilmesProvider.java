package br.com.pocomartins.bollyfilmes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Poço Martins on 3/7/2017.
 */

public class FilmesProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private FilmesDBHelper dbHelper;

    private static final int FILME = 100;

    private static final int FILME_ID = 101;

    static private UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FilmesContract.CONTENT_AUTHORITY, FilmesContract.PATH_FILMES, FILME);
        uriMatcher.addURI(FilmesContract.CONTENT_AUTHORITY, FilmesContract.PATH_FILMES + "/#", FILME_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FilmesDBHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
