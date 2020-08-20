package ru.randgor.testtask.data;

import android.provider.BaseColumns;

public class NewsContract {

    private NewsContract() {

    }

    public static final class NewEntry implements BaseColumns {
        public final static String TABLE_NAME = "news";

        public final static String COLUMN_SOURCE = "source";
        public final static String COLUMN_AUTHOR = "author";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_URL = "url";
        public final static String COLUMN_URLTOIMAGE = "urlToImage";
        public final static String COLUMN_PUBLISHEDAT = "publishedAt";
        public final static String COLUMN_CONTENT = "content";
    }
}
