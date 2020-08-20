package ru.randgor.testtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ru.randgor.testtask.data.NewsContract;

import static ru.randgor.testtask.data.NewsContract.NewEntry.*;

public class DbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "news.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_SOURCE + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_URL + " TEXT PRIMARY KEY, "
                + COLUMN_URLTOIMAGE + " TEXT, "
                + COLUMN_PUBLISHEDAT + " TEXT, "
                + COLUMN_CONTENT + " TEXT) WITHOUT ROWID;";

        db.execSQL(SQL_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isInDatabase(String url) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_URL + "= \"" + url + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public int getNewsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void insertNew(SimpleRow row) {
        if (isInDatabase(row.getUrl()))
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_SOURCE, row.getSource());
        values.put(COLUMN_AUTHOR, row.getAuthor());
        values.put(COLUMN_TITLE, row.getTitle());
        values.put(COLUMN_DESCRIPTION, row.getDescription());
        values.put(COLUMN_URL, row.getUrl());
        values.put(COLUMN_URLTOIMAGE, row.getUrlToImage());
        values.put(COLUMN_PUBLISHEDAT, row.getPublishedAt());
        values.put(COLUMN_CONTENT, row.getContent());

        try {
            db.insert(TABLE_NAME, null, values);
        }catch (Exception ignored){}
    }

    public ArrayList<SimpleRow> getNews() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SimpleRow> news = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            SimpleRow row = new SimpleRow(cursor);
            news.add(row);
        }

        return news;
    }
}