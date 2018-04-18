package br.ufpe.cin.if1001.rss.db;

import android.content.ContentValues;
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

    public static final String ITEM_ROWID = RssProviderContract._ID;
    public static final String ITEM_TITLE = RssProviderContract.TITLE;
    public static final String ITEM_DATE = RssProviderContract.DATE;
    public static final String ITEM_DESC = RssProviderContract.DESCRIPTION;
    public static final String ITEM_LINK = RssProviderContract.LINK;
    public static final String ITEM_UNREAD = RssProviderContract.UNREAD;

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
    public void insertItem(ItemRSS item) {
        insertItem(item.getTitle(), item.getPubDate(), item.getDescription(), item.getLink());
    }

    private void insertItem(String title, String pubDate, String description, String link) {
        ContentValues values = new ContentValues();
        values.put(ITEM_TITLE, title);
        values.put(ITEM_DATE, pubDate);
        values.put(ITEM_DESC, description);
        values.put(ITEM_LINK, link);
        values.put(ITEM_UNREAD, true);

        db.getWritableDatabase().insert(
                DB_TABLE,
                null,
                values
        );
    }

    public ItemRSS getItemRSS(String link) throws SQLException {
        String selection = ITEM_LINK + " = ?";
        String[] selectionArgs = { link };
        String[] columns = { ITEM_TITLE, ITEM_DESC };

        Cursor cursor = db.getReadableDatabase().query(
                DB_TABLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return new ItemRSS(
                    cursor.getString(cursor.getColumnIndex(ITEM_TITLE)),
                    link,
                    "2018-04-09",
                    cursor.getString(cursor.getColumnIndex(ITEM_DESC))
            );
        } else return null;
    }

    public Cursor getItems() throws SQLException {
        String selection = ITEM_UNREAD + " = ?";
        String[] selectionArgs = { "1" };
        return db.getReadableDatabase().query(
                DB_TABLE,
                COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    public boolean markAsUnread(String link) {
        ContentValues values = new ContentValues();
        values.put(ITEM_UNREAD, true);

        String where = ITEM_LINK + " = ?";
        String[] whereArgs = { link };
        return db.getWritableDatabase().update(
                DB_TABLE,
                values,
                where,
                whereArgs
        ) != 0;
    }

    public boolean markAsRead(String link) {
        ContentValues values = new ContentValues();
        values.put(ITEM_UNREAD, false);

        String where = ITEM_LINK + " = ?";
        String[] whereArgs = { link };
        return db.getWritableDatabase().update(
                DB_TABLE,
                values,
                where,
                whereArgs
        ) != 0;
    }
}
