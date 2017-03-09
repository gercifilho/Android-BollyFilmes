package br.com.pocomartins.bollyfilmes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                cursor = readableDatabase.query(FilmesContract.FilmesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FILME_ID:
                selection = FilmesContract.FilmesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(FilmesContract.FilmesEntry.getIdFromUri(uri))};

                cursor = readableDatabase.query(FilmesContract.FilmesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Uri não identificado: " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                return FilmesContract.FilmesEntry.CONTENT_TYPE;
            case FILME_ID:
                return FilmesContract.FilmesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri não identificado: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        long id;
        switch (URI_MATCHER.match(uri)) {
            case FILME:
                id = writableDatabase.insert(FilmesContract.FilmesEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Uri não identificado: " + uri);
        }
        return FilmesContract.FilmesEntry.buildUriForFilmes(id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                return writableDatabase.delete(FilmesContract.FilmesEntry.TABLE_NAME, selection, selectionArgs);

            case FILME_ID:
                selection = FilmesContract.FilmesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(FilmesContract.FilmesEntry.getIdFromUri(uri))};

                return writableDatabase.delete(FilmesContract.FilmesEntry.TABLE_NAME, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Uri não identificado: " + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                return writableDatabase.update(FilmesContract.FilmesEntry.TABLE_NAME, values, selection, selectionArgs);

            case FILME_ID:
                selection = FilmesContract.FilmesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(FilmesContract.FilmesEntry.getIdFromUri(uri))};

                return writableDatabase.update(FilmesContract.FilmesEntry.TABLE_NAME, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Uri não identificado: " + uri);
        }
    }
}
