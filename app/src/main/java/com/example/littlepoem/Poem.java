package com.example.littlepoem;

import android.content.Context;
import android.text.Spanned;

import java.io.Serializable;

public class Poem implements Serializable {
    private String id;
    private String title;
    private Spanned text;
    private String author;
    private String genre;
    private float rating;
    private String publicationDate;
    private int publicationState;

    private DBHelper dbHelper;
    private UsersDB usersDB;

    public Poem(Context context, String id, String title, Spanned text, int author, String genre, float rating, String publicationDate, int publicationState) {
        dbHelper = new DBHelper(context);
        usersDB = new UsersDB(dbHelper, context);

        this.id = id;
        this.title = title;
        this.text = text;
        usersDB.GetDataByID(Integer.toString(author));
        this.author = usersDB.name;
        this.genre = genre;
        this.rating = rating;
        this.publicationDate = publicationDate;
        this.publicationState = publicationState;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Spanned getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public float getRating() {
        return rating;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public int getPublicationState() {
        return publicationState;
    }

    // Override toString() method to log all properties
    @Override
    public String toString() {
        return "Poem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", text=" + text +
                ", author=" + author +
                ", genre='" + genre + '\'' +
                ", rating=" + rating +
                ", publicationDate='" + publicationDate + '\'' +
                ", publicationState=" + publicationState +
                '}';
    }
}
