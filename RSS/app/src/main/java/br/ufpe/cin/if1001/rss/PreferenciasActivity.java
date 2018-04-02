package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class PreferenciasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
    }

    public static class RssPreferenceFragment extends PreferenceFragment {
        Preference pref;
        SharedPreferences.OnSharedPreferenceChangeListener mListener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Carrega preferências de um recurso xml em res/xml
            addPreferencesFromResource(R.xml.preferencias);

            // Obtém valor atual de rss_feed
            pref = getPreferenceManager().findPreference(MainActivity.RSSFEED_KEY);

            // Listener para atualizar o summary após modificar a URL do feed
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    pref.setSummary(sharedPreferences.getString(key,MainActivity.RSSFEED_KEY));
                }
            };

            // Pega objeto SharedPreferences gerenciado pelo PreferenceManager deste fragmento
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            // Registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(mListener);

            // Força chamada ao método de callback para exibir link atual
            mListener.onSharedPreferenceChanged(prefs, MainActivity.RSSFEED_KEY);
        }
    }
}