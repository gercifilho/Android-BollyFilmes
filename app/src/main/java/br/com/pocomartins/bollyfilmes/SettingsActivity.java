package br.com.pocomartins.bollyfilmes;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by Poço Martins on 2/20/2017.
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String value = newValue.toString();

        if(preference instanceof ListPreference){

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex =  listPreference.findIndexOfValue(value);
            if(prefIndex >= 0){
                preference.setSummary(listPreference.getEntries()[prefIndex]);
             } else {
                preference.setSummary(value);

            }

        }

        return true;
    }
}
