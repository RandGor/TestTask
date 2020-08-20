package ru.randgor.testtask;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static ru.randgor.testtask.data.NewsContract.NewEntry.*;

public class SimpleRow {
    private int type;

    private String source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public SimpleRow() {
        this(MyAdapter.VIEW_TYPE_LOADING);
    }

    public SimpleRow(int type) {
        this.type = type;
    }

    public SimpleRow(Cursor cursor) {
        this.type = MyAdapter.VIEW_TYPE_NORMAL;

        this.source = cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE));
        this.author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
        this.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        this.description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
        this.url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
        this.urlToImage = cursor.getString(cursor.getColumnIndex(COLUMN_URLTOIMAGE));
        this.publishedAt = cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHEDAT));
        this.content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
    }

    public SimpleRow(JSONObject article) {
        this.type = MyAdapter.VIEW_TYPE_NORMAL;

        try {
            this.source = article.getString("source");
            this.author = article.getString("author");
            this.title = article.getString("title");
            this.description = article.getString("description");
            this.url = article.getString("url");
            this.urlToImage = article.getString("urlToImage");
            this.publishedAt = article.getString("publishedAt");
            this.content = article.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SimpleRow(String source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.type = MyAdapter.VIEW_TYPE_NORMAL;

        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}