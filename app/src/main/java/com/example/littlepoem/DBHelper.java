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
    public static final String KEY_LIKED_POEMS = "liked_poems";

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

    //Таблица жалоб
    public static final String TABLE_COMPLAINTS = "complaints";

    public static final String KEY_POEM_ID = "poem_id";
    public static final String KEY_REVIEW_ID = "review_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_COMPLAINT_TEXT = "complaint_text";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID
                + " integer primary key," + KEY_LOGIN + " text," + KEY_PASSWORD + " text," +
                KEY_NAME + " text," + KEY_ROLE + " text," + KEY_PROFILE_PICTURE + " blob," + KEY_LIKED_POEMS + " text)");
        db.execSQL("create table " + TABLE_POEMS + "(" + KEY_ID
                + " integer primary key," + KEY_TITLE + " text," + KEY_TEXT + " text," +
                KEY_TEXT_ALIGNMENT + " integer," + KEY_AUTHOR + " integer," + KEY_GENRE + " text," + KEY_RATING + " float," +
                KEY_PUBLICATION_DATE + " date," + KEY_PUBLICATION_STATE + " integer," + KEY_MODERATOR + " integer)");
        db.execSQL("create table " + TABLE_COMPLAINTS + "(" + KEY_ID
                + " integer primary key," + KEY_POEM_ID + " integer," + KEY_REVIEW_ID + " integer,"
                + KEY_USER_ID + " integer," + KEY_COMPLAINT_TEXT + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USERS);
        db.execSQL("drop table if exists " + TABLE_POEMS);
        db.execSQL("drop table if exists " + TABLE_COMPLAINTS);

        onCreate(db);

    }
}