package br.com.pocomartins.bollyfilmes.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.com.pocomartins.bollyfilmes.BuildConfig;
import br.com.pocomartins.bollyfilmes.ItemFilme;
import br.com.pocomartins.bollyfilmes.JsonUtil;
import br.com.pocomartins.bollyfilmes.R;
import br.com.pocomartins.bollyfilmes.data.FilmesContract;

/**
 * Created by Poço Martins on 3/22/2017.
 */

public class FilmesIntentService extends IntentService {

    public FilmesIntentService() {
        super("FilmesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ordem = preferences.getString(getString(R.string.prefs_ordem_key),"popular");
        String idioma = preferences.getString(getString(R.string.prefs_idioma_key),"pt-BR");

        try {

            String urlBase = "https://api.themoviedb.org/3/movie/" + ordem + "?";
            String apiKey = "api_key";
            String language = "language";

            Uri uriapi = Uri.parse(urlBase).buildUpon()
                    .appendQueryParameter(apiKey, BuildConfig.TMDB_API_KEY)
                    .appendQueryParameter(language,idioma)
                    .build();

            URL url = new URL(uriapi.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String linha;
            StringBuffer buffer = new StringBuffer();

            while ((linha = reader.readLine()) != null) {
                buffer.append(linha);
                buffer.append("\n");
            }

            List<ItemFilme> itemFilmes = JsonUtil.fromJsonToList(buffer.toString());

            if (itemFilmes == null) {
                return;
            }


            for (ItemFilme itemFilme: itemFilmes) {
                ContentValues values = new ContentValues();
                values.put(FilmesContract.FilmesEntry._ID, itemFilme.getId());
                values.put(FilmesContract.FilmesEntry.COLUMN_TITULO, itemFilme.getTitulo());
                values.put(FilmesContract.FilmesEntry.COLUMN_DESCRICAO, itemFilme.getDescricao());
                values.put(FilmesContract.FilmesEntry.COLUMN_POSTER_PATH, itemFilme.getPosterPath());
                values.put(FilmesContract.FilmesEntry.COLUMN_CAPA_PATH, itemFilme.getCapaPath());
                values.put(FilmesContract.FilmesEntry.COLUMN_AVALIACAO, itemFilme.getAvaliacao());
                values.put(FilmesContract.FilmesEntry.COLUMN_DATA_LANC, itemFilme.getDataLancamento());
                values.put(FilmesContract.FilmesEntry.COLUMN_POPULARIDADE, itemFilme.getPopularidade());

                String where = FilmesContract.FilmesEntry._ID + "=?";
                String[] whereValues = new String[] {String.valueOf(itemFilme.getId())};

                int update = getContentResolver().update(FilmesContract.FilmesEntry.buildUriForFilmes(itemFilme.getId()), values, null, null);

                if(update == 0) {
                    getContentResolver().insert(FilmesContract.FilmesEntry.CONTENT_URI, values);
                }
            }


        } catch(IOException e) {
            e.printStackTrace();

        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }

            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
