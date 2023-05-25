package com.example.littlepoem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private LinearLayout reviewsButton;
    private ImageView likeButton, copyButton, complaintButton, deleteButton;

    Poem poem;

    private DBHelper dbHelper;
    private PoemsDB poemsDB;
    private UsersDB usersDB;
    private ComplaintsDB complaintsDB;

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
        deleteButton = v.findViewById(R.id.delete_button);

        dbHelper = new DBHelper(getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        complaintsDB = new ComplaintsDB(dbHelper, getContext());

        main_toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

        setData();

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getContext().getResources().getString(R.string.confirm_delete_poem));
                builder.setPositiveButton(getContext().getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        poemsDB.deletePoem(poem.getId(), null, false);
                        main_toast.setText(getContext().getResources().getString(R.string.successfully_deleted_poem));
                        main_toast.cancel();
                        main_toast.show();
                        ((MainActivity)getActivity()).openFragment(((MainActivity)getActivity()).getCurrentMainFragment());
                    }
                });
                builder.setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
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

        reviewsButton.setVisibility(View.GONE);
        likeButton.setVisibility(View.GONE);
        copyButton.setVisibility(View.GONE);
        complaintButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        if (poem.getPublicationState() == 2) {
            deleteButton.setVisibility(View.VISIBLE);
        }
        else if (poem.getPublicationState() == 1) {
            usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());

            if (!usersDB.id.equals(poem.getAuthorID())) {
                complaintButton.setVisibility(View.VISIBLE);
                likeButton.setVisibility(View.VISIBLE);
            }
            else {
                deleteButton.setVisibility(View.VISIBLE);
            }
            reviewsButton.setVisibility(View.VISIBLE);
            copyButton.setVisibility(View.VISIBLE);

            if (usersDB.liked_poems.contains(poem.getId())) {
                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_button));
            }
            else {
                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_button));
            }

            complaintButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getContext().getResources().getString(R.string.complaint));

                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint(getContext().getResources().getString(R.string.complaint_text));
                    builder.setView(input);

                    builder.setPositiveButton(getContext().getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String complaintText = input.getText().toString();

                            if (!complaintText.trim().isEmpty()) {
                                complaintsDB.createNewPoemComplaint(poem.getId(), usersDB.id, complaintText);

                                main_toast.setText(getContext().getResources().getString(R.string.successfully_complaint));
                            }
                            else {
                                main_toast.setText(getContext().getResources().getString(R.string.error_empty_complaint_text));
                            }
                            main_toast.cancel();
                            main_toast.show();
                        }
                    });

                    builder.setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
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
