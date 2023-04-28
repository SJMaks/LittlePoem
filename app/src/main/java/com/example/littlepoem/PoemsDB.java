package com.example.littlepoem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PoemsDB {
    private SQLiteDatabase database;
    private Context context;

    public String id;
    public  String title;
    public Spanned text;
    public int author;
    public String genre;
    public float rating;
    public String publication_date;
    public int publication_state;

    public PoemsDB (DBHelper dbHelper, Context context) {
        this.database = dbHelper.getWritableDatabase();
        this.context = context;
    }

    public boolean CreateNewPoem (String buf_title, Spanned buf_text, int buf_author, String buf_genre) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = String.format("%04d-%02d-%02d", year, month, day);

        String htmlText = Html.toHtml(buf_text);

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_TITLE, buf_title);
        contentValues.put(DBHelper.KEY_TEXT, htmlText);
        contentValues.put(DBHelper.KEY_AUTHOR, buf_author);
        contentValues.put(DBHelper.KEY_GENRE, buf_genre);
        contentValues.put(DBHelper.KEY_RATING, 0);
        contentValues.put(DBHelper.KEY_PUBLICATION_DATE, currentDate);
        contentValues.put(DBHelper.KEY_PUBLICATION_STATE, 0);

        id = Long.toString(database.insert(DBHelper.TABLE_POEMS, null, contentValues));

        if (id.equals("-1")) {
            return false;
        }
        else {
            return true;
        }
    }

    public void PublishPoem () {
        database.execSQL("update poems set publication_state = '1' where id = '" + id + "'");
    }

    public boolean GetDataByID (String buf_id) {
        Cursor cursor = database.rawQuery("SELECT * FROM poems WHERE id = " + buf_id, null);
        if (cursor.moveToFirst()) {
            int title_index = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int text_index = cursor.getColumnIndex(DBHelper.KEY_TEXT);
            int author_index = cursor.getColumnIndex(DBHelper.KEY_AUTHOR);
            int genre_index = cursor.getColumnIndex(DBHelper.KEY_GENRE);
            int rating_index = cursor.getColumnIndex(DBHelper.KEY_RATING);
            int publication_date_index = cursor.getColumnIndex(DBHelper.KEY_PUBLICATION_DATE);
            int publication_state_index = cursor.getColumnIndex(DBHelper.KEY_PUBLICATION_STATE);

            id = buf_id;
            title = cursor.getString(title_index);
            text = Html.fromHtml(cursor.getString(text_index));
            author = cursor.getInt(author_index);
            genre = cursor.getString(genre_index);
            rating = cursor.getFloat(rating_index);
            publication_date = cursor.getString(publication_date_index);
            publication_state = cursor.getInt(publication_state_index);

            return true;
        }
        else {
            return false;
        }
    }

    public List<Poem> selectUnpublishedPoems() {
        String[] columns = { "id", "title", "text", "author", "genre", "rating", "publication_date", "publication_state" };
        String selection = "publication_state = ?";
        String[] selectionArgs = { "0" };
        Cursor cursor = database.query("poems", columns, selection, selectionArgs, null, null, null);

        List<Poem> poems = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int title_index = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int text_index = cursor.getColumnIndex(DBHelper.KEY_TEXT);
            int author_index = cursor.getColumnIndex(DBHelper.KEY_AUTHOR);
            int genre_index = cursor.getColumnIndex(DBHelper.KEY_GENRE);
            int rating_index = cursor.getColumnIndex(DBHelper.KEY_RATING);
            int publication_date_index = cursor.getColumnIndex(DBHelper.KEY_PUBLICATION_DATE);
            int publication_state_index = cursor.getColumnIndex(DBHelper.KEY_PUBLICATION_STATE);

            id = cursor.getString(id_index);
            title = cursor.getString(title_index);
            text = Html.fromHtml(cursor.getString(text_index));
            author = cursor.getInt(author_index);
            genre = cursor.getString(genre_index);
            rating = cursor.getFloat(rating_index);
            publication_date = cursor.getString(publication_date_index);
            publication_state = cursor.getInt(publication_state_index);

            Poem poem = new Poem(context.getApplicationContext(), id, title, text, author, genre, rating, publication_date, publication_state);
            poems.add(poem);
        }

        cursor.close();
        return poems;
    }

    public void ClearDB() {
        database.execSQL("drop table if exists " + DBHelper.TABLE_POEMS);
        database.execSQL("create table " + DBHelper.TABLE_POEMS + "(" + DBHelper.KEY_ID
                + " integer primary key," + DBHelper.KEY_TITLE + " text," + DBHelper.KEY_TEXT + " text," +
                DBHelper.KEY_AUTHOR + " integer," + DBHelper.KEY_GENRE + " text," + DBHelper.KEY_RATING + " float," +
                DBHelper.KEY_PUBLICATION_DATE + " date," + DBHelper.KEY_PUBLICATION_STATE + " integer)");
    }
}
