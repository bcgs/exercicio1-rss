package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.util.adapter.XmlFeedAdapter;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.util.ParserRSS;

public class MainActivity extends Activity {
    // Deixou de ser constante pois agora pode ser modificado nas configurações
    private String rss_feed;

    // Chave utilizada no SharedPreferences
    public static final String RSSFEED_KEY = "rssfeed";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome
    private ListView conteudoRSS;

    // Adapter customizado
    private XmlFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conteudoRSS = findViewById(R.id.items);

        // Ao clicarmos em um ítem navegaremos pelo link disponibilizado pelo mesmo
        conteudoRSS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemRSS item = (ItemRSS) adapter.getItem(position);
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(item.getLink())
                );
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Sempre atualizando a ListView através do link disponibilizado
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        rss_feed = prefs.getString(RSSFEED_KEY, getString(R.string.rss_feed_default));
        new CarregaRSStask().execute(rss_feed);
    }

    // Menu de opções na barra de tarefas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Ao selecionar a opção do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, PreferenciasActivity.class));
        return super.onOptionsItemSelected(item);
    }

    /**
     * Display feed.
     * @param feed Item list to be displayed.
     */
    private void displayFeed(List<ItemRSS> feed) {
        adapter = new XmlFeedAdapter(this, feed);
        conteudoRSS.setAdapter(adapter);
    }

    private class CarregaRSStask extends AsyncTask<String, Void, List<ItemRSS>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemRSS> doInBackground(String... params) {
            List<ItemRSS> itemList = new ArrayList<>();
            try {
                itemList = ParserRSS.parse(getRssFeed(params[0]));
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return itemList;
        }

        // onPostExecute passa a receber uma List<ItemRSS>
        @Override
        protected void onPostExecute(List<ItemRSS> feed) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            // Chamando função responsável por listar o feed
            displayFeed(feed);
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
