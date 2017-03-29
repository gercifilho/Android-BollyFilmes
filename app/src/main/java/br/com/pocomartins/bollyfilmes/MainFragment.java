package br.com.pocomartins.bollyfilmes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import br.com.pocomartins.bollyfilmes.data.FilmesContract;
import br.com.pocomartins.bollyfilmes.sync.FilmesSyncAdapter;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private int posicaoItem = ListView.INVALID_POSITION;

    private static final String KEY_POSICAO = "SELECIONADO";

    private ListView listView;

    private FilmesAdapter adapter;

    private boolean useFilmeDestaque = false;

    private static final int FILMES_LOADER = 0;

    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        listView = (ListView) view.findViewById(R.id.list_filmes);

        adapter = new FilmesAdapter(getContext(), null);
        adapter.setUseFilmeDestaque(useFilmeDestaque);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = FilmesContract.FilmesEntry.buildUriForFilmes(id);
                view.setSelected(true);
                Callback callback = (Callback) getActivity();
                callback.onItemSelect(uri);
                posicaoItem = position;
            }
        });

        // Inflate the layout for this fragment
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_POSICAO)) {
            posicaoItem = savedInstanceState.getInt(KEY_POSICAO);
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.pd_carregando_titulo));
        progressDialog.setMessage(getString(R.string.pd_carregando_mensagem));
        progressDialog.setCancelable(false);

        getLoaderManager().initLoader(FILMES_LOADER, null, this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(posicaoItem != ListView.INVALID_POSITION) {
            outState.putInt(KEY_POSICAO, posicaoItem);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            listView.smoothScrollToPosition(savedInstanceState.getInt(KEY_POSICAO));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_atualizar:
                FilmesSyncAdapter.syncImediately(getContext());

                Toast.makeText(getContext(), "Atualizando os filmes.....", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_config:
                startActivity(new Intent(getContext(), SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUseFilmeDestaque(boolean useFilmeDestaque) {
        this.useFilmeDestaque = useFilmeDestaque;
        if (adapter != null) {
            adapter.setUseFilmeDestaque(useFilmeDestaque);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(FILMES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        progressDialog.show();

        String[] projection = {
                FilmesContract.FilmesEntry._ID,
                FilmesContract.FilmesEntry.COLUMN_TITULO,
                FilmesContract.FilmesEntry.COLUMN_DESCRICAO,
                FilmesContract.FilmesEntry.COLUMN_DATA_LANC,
                FilmesContract.FilmesEntry.COLUMN_POSTER_PATH,
                FilmesContract.FilmesEntry.COLUMN_CAPA_PATH,
                FilmesContract.FilmesEntry.COLUMN_AVALIACAO,
                FilmesContract.FilmesEntry.COLUMN_POPULARIDADE
        };

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ordem = preferences.getString(getString(R.string.prefs_ordem_key),"popular");
        String popularValue = getResources().getStringArray(R.array.prefs_ordem_values)[0];

        String orderBy = null;
        if(ordem.equals(popularValue)) {
            orderBy = FilmesContract.FilmesEntry.COLUMN_POPULARIDADE + " DESC";
        } else {
            orderBy = FilmesContract.FilmesEntry.COLUMN_AVALIACAO + " DESC";
        }

        return new CursorLoader(getContext(), FilmesContract.FilmesEntry.CONTENT_URI, projection, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        progressDialog.dismiss();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

      public interface Callback {
        void onItemSelect(Uri uri);
    }

}
