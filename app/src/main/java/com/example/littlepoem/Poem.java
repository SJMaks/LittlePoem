package com.example.littlepoem;

import android.text.Spanned;

public class Poem {
    private String id;
    private String title;
    private Spanned text;
    private int author;
    private String genre;
    private float rating;
    private String publicationDate;
    private int publicationState;

    public Poem(String id, String title, Spanned text, int author, String genre, float rating, String publicationDate, int publicationState) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.author = author;
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

    public int getAuthor() {
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
