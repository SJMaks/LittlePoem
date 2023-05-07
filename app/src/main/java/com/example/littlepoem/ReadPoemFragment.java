package com.example.littlepoem;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ReadPoemFragment extends Fragment {
    private TextView poemTitle, poemAuthor, poemGenre, poemRating, poemDate, poemText;
    private LinearLayout reviewsButton, action_buttons;
    private ImageView likeButton, copyButton, complaintButton;

    Poem poem;

    private DBHelper dbHelper;
    private PoemsDB poemsDB;
    private UsersDB usersDB;

    Toast main_toast;

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
        action_buttons = v.findViewById(R.id.poem_buttons);

        dbHelper = new DBHelper(getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());
        usersDB = new UsersDB(dbHelper, getContext());

        main_toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

        setData();

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());
                if (!usersDB.liked_poems.contains(poem.getId())) {
                    usersDB.addLikedPoem(poem.getId());
                    likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_button));
                    main_toast.setText(getContext().getResources().getString(R.string.successfully_liked));
                    main_toast.cancel();
                    main_toast.show();
                }
                else {
                    usersDB.removeLikedPoem(poem.getId());
                    likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_button));
                    main_toast.setText(getContext().getResources().getString(R.string.successfully_unliked));
                    main_toast.cancel();
                    main_toast.show();
                }
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getContext().CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("label", poemText.getText().toString());

                clipboard.setPrimaryClip(clip);

                main_toast.setText(getContext().getResources().getString(R.string.successfully_copied));
                main_toast.cancel();
                main_toast.show();
            }
        });

        complaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        poemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openProfileFragment(poem.getAuthorID());
            }
        });

        return v;
    }

    @SuppressLint("WrongConstant")
    private void setData() {
        Bundle bundle = getArguments();
        poem = (Poem) bundle.getSerializable("poem");

        usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());
        if (usersDB.liked_poems.contains(poem.getId())) {
            likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_button));
        }
        else {
            likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_button));
        }

        reviewsButton.setVisibility(View.GONE);
        action_buttons.setVisibility(View.GONE);
        if (poem.getPublicationState() == 1) {
            action_buttons.setVisibility(View.VISIBLE);
            reviewsButton.setVisibility(View.VISIBLE);
        }

        poemTitle.setText(poem.getTitle());
        poemAuthor.setText(poem.getAuthor());
        poemAuthor.setPaintFlags(poemAuthor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        poemGenre.setText(poem.getGenre());
        poemRating.setText(String.valueOf(poem.getRating()));
        poemDate.setText(poem.getPublicationDate());
        poemText.setText(poem.getText());
        poemText.setTextAlignment(poem.getTextAlignment());
    }
}
