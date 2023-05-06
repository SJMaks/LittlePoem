package com.example.littlepoem;

import android.content.Context;
import android.text.Spanned;

import java.io.Serializable;

public class Poem implements Serializable {
    private String id;
    private String title;
    private Spanned text;
    private int text_alignment;
    private String author;
    private String genre;
    private float rating;
    private String publicationDate;
    private int publicationState;
    private int moderator;

    private DBHelper dbHelper;
    private UsersDB usersDB;

    public Poem(Context context, String id, String title, Spanned text, int text_alignment, int author, String genre, float rating, String publicationDate, int publicationState, int moderator) {
        dbHelper = new DBHelper(context);
        usersDB = new UsersDB(dbHelper, context);

        this.id = id;
        this.title = title;
        this.text = text;
        this.text_alignment = text_alignment;
        usersDB.GetDataByID(Integer.toString(author));
        this.author = usersDB.name;
        this.genre = genre;
        this.rating = rating;
        this.publicationDate = publicationDate;
        this.publicationState = publicationState;
        this.moderator = moderator;
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

    public int getTextAlignment() {
        return text_alignment;
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

    public int getModerator() {
        return moderator;
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
                ", moderator=" + moderator +
                '}';
    }
}
