package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.preference.PreferenceManager;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.service.DownloadService;
import br.ufpe.cin.if1001.rss.ui.adapter.ItemRSSAdapter;

public class MainActivity extends Activity {
    public static final String RSSFEED_KEY = "rssfeed";
    public static final String DOWNLOAD_COMPLETE = "downloadcomplete";
    public static final String UPDATE_INTERVAL_KEY = "updateinterval";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    private SQLiteRSSHelper db;
    private ItemRSSAdapter adapter;
    public static SortedList<ItemRSS> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = SQLiteRSSHelper.getInstance(this);

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new SortedList<>(ItemRSS.class, callbackMethods);
        adapter = new ItemRSSAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        setContentView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String rss_feed = prefs.getString(RSSFEED_KEY, getString(R.string.rss_feed_default));

        // Inicializa um Service para download do RSS
        Intent iService = new Intent(getApplicationContext(), DownloadService.class);
        iService.putExtra(RSSFEED_KEY, rss_feed);
        startService(iService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter downloadFilter = new IntentFilter(DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onDownloadCompleteEvent, downloadFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(onDownloadCompleteEvent);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
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

    private BroadcastReceiver onDownloadCompleteEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "Banco atualizado", Toast.LENGTH_SHORT).show();
            new ExibirFeed().execute();
        }
    };

    class ExibirFeed extends AsyncTask<Void, Void, ArrayList<ItemRSS>> {

        @Override
        protected ArrayList<ItemRSS> doInBackground(Void... voids) {
            Cursor c = db.getItems();
            c.getCount();
            ArrayList<ItemRSS> newList = new ArrayList<>();

            // Popular newList com conteúdo do Cursor
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String title = c.getString(c.getColumnIndex(SQLiteRSSHelper.ITEM_TITLE));
                String link = c.getString(c.getColumnIndex(SQLiteRSSHelper.ITEM_LINK));
                String date = c.getString(c.getColumnIndex(SQLiteRSSHelper.ITEM_DATE));
                String description = c.getString(c.getColumnIndex(SQLiteRSSHelper.ITEM_DESC));

                newList.add(new ItemRSS(title, link, date, description));
            }
            c.close();
            return newList;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemRSS> newList) {
            adapter.swap(newList);  // Exibir lista
        }
    }

    /** Responsável pelo comportamento da SortedList */
    SortedList.Callback<ItemRSS> callbackMethods = new SortedList.Callback<ItemRSS>() {
        @Override
        public int compare(ItemRSS o1, ItemRSS o2) {
            return o1.getPubDate().compareTo(o2.getPubDate());
        }

        @Override
        public void onChanged(int position, int count) {
            adapter.notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ItemRSS oldItem, ItemRSS newItem) {
            return areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areItemsTheSame(ItemRSS item1, ItemRSS item2) {
            return compare(item1, item2) == 0;
        }

        @Override
        public void onInserted(int position, int count) {
            adapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
}