package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import br.ufpe.cin.if1001.rss.R;

public class PreferenciasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
    }

    public static class RssPreferenceFragment extends PreferenceFragment {
        Preference pref1, pref2;
        SharedPreferences.OnSharedPreferenceChangeListener mListener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Carrega preferências de um recurso xml em res/xml
            addPreferencesFromResource(R.xml.preferencias);

            // Obtém valor atual de rss_feed
            pref1 = getPreferenceManager().findPreference(MainActivity.RSSFEED_KEY);
            pref2 = getPreferenceManager().findPreference(MainActivity.UPDATE_INTERVAL_KEY);

            // Listener para atualizar o summary após modificar a URL do feed
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals(MainActivity.RSSFEED_KEY))
                        pref1.setSummary(sharedPreferences.getString(key, MainActivity.RSSFEED_KEY));
                    else if (key.equals(MainActivity.UPDATE_INTERVAL_KEY))
                        pref2.setSummary(sharedPreferences.getString(key, MainActivity.UPDATE_INTERVAL_KEY));
                }
            };

            // Pega objeto SharedPreferences gerenciado pelo PreferenceManager deste fragmento
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            // Registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(mListener);

            // Força chamada ao método de callback para exibir link atual
            mListener.onSharedPreferenceChanged(prefs, MainActivity.RSSFEED_KEY);
            mListener.onSharedPreferenceChanged(prefs, MainActivity.UPDATE_INTERVAL_KEY);
        }
    }
}