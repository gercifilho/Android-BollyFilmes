package br.com.pocomartins.bollyfilmes.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.com.pocomartins.bollyfilmes.BuildConfig;
import br.com.pocomartins.bollyfilmes.FilmeDetalheActivity;
import br.com.pocomartins.bollyfilmes.ItemFilme;
import br.com.pocomartins.bollyfilmes.JsonUtil;
import br.com.pocomartins.bollyfilmes.R;
import br.com.pocomartins.bollyfilmes.data.FilmesContract;

/**
 * Created by Poço Martins on 3/29/2017.
 */

public class FilmesSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 720;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public final static int NOTIFICATION_FILMES_ID = 1001;

    public FilmesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ordem = preferences.getString(getContext().getString(R.string.prefs_ordem_key),"popular");
        String idioma = preferences.getString(getContext().getString(R.string.prefs_idioma_key),"pt-BR");

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

                int update = getContext().getContentResolver().update(FilmesContract.FilmesEntry.buildUriForFilmes(itemFilme.getId()), values, null, null);

                if(update == 0) {
                    getContext().getContentResolver().insert(FilmesContract.FilmesEntry.CONTENT_URI, values);
                    notify(itemFilme);
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

    public void notify(ItemFilme itemFilme) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String notifyPrefKey = getContext().getString(R.string.prefs_notif_filmes_key);
        String notifyDefault = getContext().getString(R.string.prefs_notif_filmes_default);
        boolean notifyprefs = sharedPreferences.getBoolean(notifyPrefKey, Boolean.parseBoolean(notifyDefault));

        if(!notifyprefs) {
            return;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext()).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle(itemFilme.getTitulo()).
                setContentText(itemFilme.getDescricao());

        Intent intent = new Intent(getContext(), FilmeDetalheActivity.class);
        Uri uri = FilmesContract.FilmesEntry.buildUriForFilmes(itemFilme.getId());
        intent.setData(uri);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_FILMES_ID, builder.build());

    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest syncRequest = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(syncRequest);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreate(Account account, Context context) {

        FilmesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(account, context.getString(R.string.content_authority), true);

        syncImediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void syncImediately(Context context) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(context.getString(R.string.app_name),  context.getString(R.string.sync_account_type));

        if(accountManager.getPassword(account) == null){
            if(!accountManager.addAccountExplicitly(account, "", null)) {
                return null;
            }

            onAccountCreate(account, context);
        }

        return account;
    }
}

