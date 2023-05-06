package com.example.littlepoem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mainDB";

    //Таблица пользователей
    public static final String TABLE_USERS = "users";

    public static final String KEY_ID = "id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_ROLE = "role";
    public static final String KEY_PROFILE_PICTURE = "profile_picture";

    //Таблица стихотворений
    public static final String TABLE_POEMS = "poems";

    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT = "text";
    public static final String KEY_TEXT_ALIGNMENT = "text_alignment";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_RATING = "rating";
    public static final String KEY_PUBLICATION_DATE = "publication_date";
    public static final String KEY_PUBLICATION_STATE = "publication_state";
    public static final String KEY_MODERATOR = "moderator";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID
                + " integer primary key," + KEY_LOGIN + " text," + KEY_PASSWORD + " text," +
                KEY_NAME + " text," + KEY_ROLE + " text," + KEY_PROFILE_PICTURE + " blob)");
        db.execSQL("create table " + TABLE_POEMS + "(" + KEY_ID
                + " integer primary key," + KEY_TITLE + " text," + KEY_TEXT + " text," +
                KEY_TEXT_ALIGNMENT + " integer," + KEY_AUTHOR + " integer," + KEY_GENRE + " text," + KEY_RATING + " float," +
                KEY_PUBLICATION_DATE + " date," + KEY_PUBLICATION_STATE + " integer," + KEY_MODERATOR + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USERS);
        db.execSQL("drop table if exists " + TABLE_POEMS);

        onCreate(db);

    }
}