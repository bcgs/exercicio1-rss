package br.ufpe.cin.if1001.rss.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.ufpe.cin.if1001.rss.domain.ItemRSS;

public class SQLiteRSSHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "rss";
    public static final String DB_TABLE = "items";
    private static final int DB_VERSION = 1;

    Context c;

    private SQLiteRSSHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        c = context;
    }

    private static SQLiteRSSHelper db;
    public static SQLiteRSSHelper getInstance(Context c) {
        if (db==null) {
            db = new SQLiteRSSHelper(c.getApplicationContext());
        }
        return db;
    }

    public static final String ITEM_ROWID = "_ID";
    public static final String ITEM_TITLE = "TITLE";
    public static final String ITEM_DATE = "DATE";
    public static final String ITEM_DESC = "DESCRIPTION";
    public static final String ITEM_LINK = "LINK";
    public static final String ITEM_UNREAD = "UNREAD";

    public final static String[] COLUMNS = {
            ITEM_ROWID, ITEM_TITLE, ITEM_DATE,
            ITEM_DESC, ITEM_LINK, ITEM_UNREAD
    };

    private static final String CREATE_DB_COMMAND = "CREATE TABLE " + DB_TABLE + " (" +
            ITEM_ROWID +" integer primary key autoincrement, "+
            ITEM_TITLE + " text not null, " +
            ITEM_DATE + " text not null, " +
            ITEM_DESC + " text not null, " +
            ITEM_LINK + " text not null, " +
            ITEM_UNREAD + " boolean not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("nao se aplica");
    }

    //IMPLEMENTAR ABAIXO
    //Implemente a manipulação de dados nos métodos auxiliares para não ficar criando consultas manualmente
    public long insertItem(ItemRSS item) {
        return insertItem(item.getTitle(),item.getPubDate(),item.getDescription(),item.getLink());
    }
    public long insertItem(String title, String pubDate, String description, String link) {
        return (long) 0.0;
    }
    public ItemRSS getItemRSS(String link) throws SQLException {
        return new ItemRSS("FALTA IMPLEMENTAR","FALTA IMPLEMENTAR","2018-04-09","FALTA IMPLEMENTAR");
    }
    public Cursor getItems() throws SQLException {
        return null;
    }
    public boolean markAsUnread(String link) {
        return false;
    }

    public boolean markAsRead(String link) {
        return false;
    }
}
