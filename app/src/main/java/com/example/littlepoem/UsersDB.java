package com.example.littlepoem;

import static android.content.Context.MODE_PRIVATE;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;

public class UsersDB {

    private SQLiteDatabase database;
    private Context context;

    public String id;
    public  String login;
    public String password;
    public String role;
    public String name;
    public Bitmap picture;

    public UsersDB (DBHelper dbHelper, Context context) {
        this.database = dbHelper.getWritableDatabase();
        this.context = context;
    }

    public boolean CreateNewUser (String buf_login, String buf_password, String buf_role, byte[] buf_picture) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_LOGIN, buf_login);
        contentValues.put(DBHelper.KEY_PASSWORD, buf_password);
        contentValues.put(DBHelper.KEY_NAME, buf_login);
        contentValues.put(DBHelper.KEY_ROLE, buf_role);
        contentValues.put(DBHelper.KEY_PROFILE_PICTURE, buf_picture);

        id = Long.toString(database.insert(DBHelper.TABLE_USERS, null, contentValues));

        if (id.equals("-1")) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean CheckLogin (String buf_login) {
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE login = '" + buf_login + "'", null);
        if (cursor.moveToFirst()) { return true; }
        else { return false; }
    }

    public boolean GetDataByLoginPassword (String buf_login, String buf_password) {
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE login = '" + buf_login + "' AND password = '" + buf_password + "'", null);
        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(DBHelper.KEY_ID);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int role_index = cursor.getColumnIndex(DBHelper.KEY_ROLE);
            int picture_index = cursor.getColumnIndex(DBHelper.KEY_PROFILE_PICTURE);

            id = cursor.getString(id_index);
            login = buf_login;
            password = buf_password;
            role = cursor.getString(role_index);
            name = cursor.getString(name_index);
            byte[] buf_picture = cursor.getBlob(picture_index);
            picture = BitmapFactory.decodeByteArray(buf_picture, 0, buf_picture.length);

            return true;
        }
        else {
            return false;
        }
    }

    public boolean GetDataByID (String buf_id) {
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE id = " + buf_id, null);
        if (cursor.moveToFirst()) {
            int login_index = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
            int password_index = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int name_index = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int role_index = cursor.getColumnIndex(DBHelper.KEY_ROLE);
            int picture_index = cursor.getColumnIndex(DBHelper.KEY_PROFILE_PICTURE);

            id = buf_id;
            login = cursor.getString(login_index);
            password = cursor.getString(password_index);
            name = cursor.getString(name_index);
            role = cursor.getString(role_index);
            byte[] buf_picture = cursor.getBlob(picture_index);
            picture = BitmapFactory.decodeByteArray(buf_picture, 0, buf_picture.length);

            return true;
        }
        else {
            return false;
        }
    }

    public void SetName (String buf_name) {
        database.execSQL("update users set name = '" + buf_name + "' where id = '" + id + "'");
    }

    public void SetPassword (String buf_password) {
        database.execSQL("update users set password = '" + buf_password + "' where id = '" + id + "'");
    }

    public void SetPicture (byte[] buf_picture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_PROFILE_PICTURE, buf_picture);
        database.update(DBHelper.TABLE_USERS, contentValues, "id = ?", new String[]{id});
    }

    public void ClearDB() {
        database.execSQL("drop table if exists " + DBHelper.TABLE_USERS);
        database.execSQL("create table " + DBHelper.TABLE_USERS + "(" + DBHelper.KEY_ID
                + " integer primary key," + DBHelper.KEY_LOGIN + " text," + DBHelper.KEY_PASSWORD + " text," + DBHelper.KEY_NAME + " text," + DBHelper.KEY_ROLE + " text," + DBHelper.KEY_PROFILE_PICTURE + " blob)");
    }
}