package br.com.pocomartins.bollyfilmes.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Poço Martins on 3/28/2017.
 */

public class FilmesAuthenticatiosService extends Service{

    private FilmesAuthenticator filmesAuthenticator;

    @Override
    public void onCreate() {
        filmesAuthenticator = new FilmesAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return filmesAuthenticator.getIBinder();
    }
}
