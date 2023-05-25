package com.example.littlepoem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ComplaintsDB {
    private SQLiteDatabase database;
    private Context context;

    public String id, poem_id, review_id, user_id, complaint_text;

    public ComplaintsDB(DBHelper dbHelper, Context context) {
        this.database = dbHelper.getWritableDatabase();
        this.context = context;
    }

    public boolean createNewPoemComplaint (String buf_poem_id, String buf_user_id, String buf_complaint_text) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_POEM_ID, buf_poem_id);
        contentValues.put(DBHelper.KEY_REVIEW_ID, 0);
        contentValues.put(DBHelper.KEY_USER_ID, buf_user_id);
        contentValues.put(DBHelper.KEY_COMPLAINT_TEXT, buf_complaint_text);

        id = Long.toString(database.insert(DBHelper.TABLE_COMPLAINTS, null, contentValues));

        if (id.equals("-1")) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean createNewReviewComplaint (String buf_review_id, String buf_user_id, String buf_complaint_text) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_POEM_ID, 0);
        contentValues.put(DBHelper.KEY_REVIEW_ID, buf_review_id);
        contentValues.put(DBHelper.KEY_USER_ID, buf_user_id);
        contentValues.put(DBHelper.KEY_COMPLAINT_TEXT, buf_complaint_text);

        id = Long.toString(database.insert(DBHelper.TABLE_COMPLAINTS, null, contentValues));

        if (id.equals("-1")) {
            return false;
        }
        else {
            return true;
        }
    }

    public void deleteComplaint(String buf_id) {
        database.execSQL("DELETE FROM complaints WHERE id = '" + buf_id + "'");
    }

    public List<Complaint> selectComplaints() {
        String[] columns = { "id", "poem_id", "review_id", "user_id", "complaint_text" };

        Cursor cursor = database.query("complaints", columns, null, null, null, null, null);

        List<Complaint> complaints = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int poem_id_index = cursor.getColumnIndex(DBHelper.KEY_POEM_ID);
            int review_id_index = cursor.getColumnIndex(DBHelper.KEY_REVIEW_ID);
            int user_id_index = cursor.getColumnIndex(DBHelper.KEY_USER_ID);
            int complaint_text_index = cursor.getColumnIndex(DBHelper.KEY_COMPLAINT_TEXT);

            id = cursor.getString(id_index);
            poem_id = cursor.getString(poem_id_index);
            review_id = cursor.getString(review_id_index);
            user_id = cursor.getString(user_id_index);
            complaint_text = cursor.getString(complaint_text_index);

            Complaint complaint = new Complaint(context.getApplicationContext(), id, poem_id, review_id, user_id, complaint_text);
            complaints.add(complaint);
        }

        cursor.close();
        return complaints;
    }

    public void clearDB() {
        database.execSQL("drop table if exists " + DBHelper.TABLE_COMPLAINTS);
        database.execSQL("create table " + DBHelper.TABLE_COMPLAINTS + "(" + DBHelper.KEY_ID
                + " integer primary key," + DBHelper.KEY_POEM_ID + " integer," + DBHelper.KEY_REVIEW_ID + " integer,"
                + DBHelper.KEY_USER_ID + " integer," + DBHelper.KEY_COMPLAINT_TEXT + " text)");
    }
}
