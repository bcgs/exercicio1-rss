package br.ufpe.cin.if1001.rss.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.ui.MainActivity;
import br.ufpe.cin.if1001.rss.util.ParserRSS;

public class DownloadService extends IntentService {
    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SQLiteRSSHelper db = SQLiteRSSHelper.getInstance(getApplication());
        String feeds = intent.getStringExtra(MainActivity.RSSFEED_KEY);
        List<ItemRSS> items;

        try {
            String feed = getRssFeed(feeds);
            items = ParserRSS.parse(feed);
            for (ItemRSS i : items) {
                ItemRSS item = db.getItemRSS(i.getLink());
                if (item == null) {
                    db.insertItem(i);
                }
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        LocalBroadcastManager
                .getInstance(this)
                .sendBroadcast(new Intent(MainActivity.DOWNLOAD_COMPLETE));
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed;
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
