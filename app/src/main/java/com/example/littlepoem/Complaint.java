package com.example.littlepoem;

import android.content.Context;
import android.text.Spanned;

import java.io.Serializable;

public class Complaint implements Serializable {
    private String id, poem_id, review_id, user_id, complaint_text;
    private String user_name, poem_name;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    public Complaint(Context context, String id, String poem_id, String review_id, String user_id, String complaint_text) {
        dbHelper = new DBHelper(context);
        usersDB = new UsersDB(dbHelper, context);
        poemsDB = new PoemsDB(dbHelper, context);

        this.id = id;
        this.poem_id = poem_id;
        this.review_id = review_id;
        this.user_id = user_id;
        this.complaint_text = complaint_text;
        usersDB.getDataByID(user_id);
        this.user_name = usersDB.name;
        Poem poem = poemsDB.getPoemByID(poem_id);
        this.poem_name = poem.getTitle();
    }

    public String getId() {
        return id;
    }

    public String getPoemID() {
        return poem_id;
    }

    public String getPoemName() {
        return poem_name;
    }

    public String getReviewID() {
        return review_id;
    }

    public String getUserID() {
        return user_id;
    }

    public String getUserName() {
        return user_name;
    }

    public String getComplaintText() {
        return complaint_text;
    }
}
