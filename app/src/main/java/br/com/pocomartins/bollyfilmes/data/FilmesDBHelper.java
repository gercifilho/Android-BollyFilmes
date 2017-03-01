package br.com.pocomartins.bollyfilmes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Poço Martins on 3/1/2017.
 */

public class FilmesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "bollyfilmes.db";

    public FilmesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlTableFilmes = "CREATE TABLE " + FilmesContract.FilmesEntry.TABLE_NAME + " (" +
                FilmesContract.FilmesEntry.ID + " INTEGER PRIMARY KEY, " +
                FilmesContract.FilmesEntry.COLUMN_TITULO + " TEXT NOT NULL, " +
                FilmesContract.FilmesEntry.COLUMN_DESCRICAO  + " TEXT NOT NULL, " +
                FilmesContract.FilmesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FilmesContract.FilmesEntry.COLUMN_CAPA_PATH + " TEXT NOT NULL, " +
                FilmesContract.FilmesEntry.COLUMN_AVALIACAO + " REAL " +
                ");";
        db.execSQL(sqlTableFilmes);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " +FilmesContract.FilmesEntry.TABLE_NAME);
        onCreate(db);
    }
}
