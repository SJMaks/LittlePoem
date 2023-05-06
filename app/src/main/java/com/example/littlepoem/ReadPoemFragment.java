package com.example.littlepoem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ReadPoemFragment extends Fragment {
    private TextView poemTitle, poemAuthor, poemGenre, poemRating, poemDate, poemText;
    private LinearLayout reviewsButton;
    private ImageView likeButton, copyButton, complaintButton;

    Poem poem;

    private DBHelper dbHelper;
    private PoemsDB poemsDB;
    private UsersDB usersDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_read_poem, container, false);

        poemTitle = v.findViewById(R.id.poem_title);
        poemAuthor = v.findViewById(R.id.poem_author);
        poemGenre = v.findViewById(R.id.poem_genre);
        poemRating = v.findViewById(R.id.poem_rating);
        poemDate = v.findViewById(R.id.poem_date);
        poemText = v.findViewById(R.id.poem_text);
        reviewsButton = v.findViewById(R.id.reviews_button);
        likeButton = v.findViewById(R.id.like_button);
        copyButton = v.findViewById(R.id.copy_button);
        complaintButton = v.findViewById(R.id.complaint_button);

        dbHelper = new DBHelper(getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());
        usersDB = new UsersDB(dbHelper, getContext());

        setData();

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        complaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @SuppressLint("WrongConstant")
    private void setData() {
        Bundle bundle = getArguments();
        poem = (Poem) bundle.getSerializable("poem");

        poemTitle.setText(poem.getTitle());
        poemAuthor.setText(poem.getAuthor());
        poemGenre.setText(poem.getGenre());
        poemRating.setText(String.valueOf(poem.getRating()));
        poemDate.setText(poem.getPublicationDate());
        poemText.setText(poem.getText());
        poemText.setTextAlignment(poem.getTextAlignment());
    }
}
