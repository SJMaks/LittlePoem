package com.example.littlepoem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ModeratePoemFragment extends Fragment {
    private TextView poemTitle, poemAuthor, poemGenre, poemText;
    private Button publishButton, rejectButton;
    private LinearLayout buttons, reviews_button;
    private ImageView delete_button;

    Poem poem;

    private DBHelper dbHelper;
    private PoemsDB poemsDB;

    Toast main_toast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moderate_poem, container, false);

        poemTitle = v.findViewById(R.id.poem_name);
        poemAuthor = v.findViewById(R.id.poem_author);
        poemGenre = v.findViewById(R.id.poem_genre);
        poemText = v.findViewById(R.id.poem_text);
        buttons = v.findViewById(R.id.buttons);
        reviews_button = v.findViewById(R.id.reviews_button);
        publishButton = v.findViewById(R.id.publish_button);
        rejectButton = v.findViewById(R.id.reject_button);
        delete_button = v.findViewById(R.id.delete_button);

        dbHelper = new DBHelper(getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        main_toast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);

        setData();

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getContext().getResources().getString(R.string.confirm_publish_poem));
                builder.setPositiveButton(getContext().getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        poemsDB.publishPoem(poem.getId(), ((MainActivity)getActivity()).getCurrentUser());
                        main_toast.setText(getContext().getResources().getString(R.string.successfully_published_poem));
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

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getContext().getResources().getString(R.string.confirm_reject_poem));
                builder.setPositiveButton(getContext().getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        poemsDB.rejectPoem(poem.getId(), ((MainActivity)getActivity()).getCurrentUser());
                        main_toast.setText(getContext().getResources().getString(R.string.successfully_rejected_poem));
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

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getContext().getResources().getString(R.string.confirm_delete_poem));
                builder.setPositiveButton(getContext().getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        poemsDB.deletePoem(poem.getId(),((MainActivity)getActivity()).getCurrentUser(), true);
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

        return v;
    }

    @SuppressLint("WrongConstant")
    private void setData() {
        Bundle bundle = getArguments();
        poem = (Poem) bundle.getSerializable("poem");

        buttons.setVisibility(View.GONE);
        reviews_button.setVisibility(View.GONE);
        delete_button.setVisibility(View.GONE);
        if (poem.getPublicationState() == 0) {
            buttons.setVisibility(View.VISIBLE);
        }
        if (poem.getPublicationState() == 1) {
            reviews_button.setVisibility(View.VISIBLE);
            delete_button.setVisibility(View.VISIBLE);
        }

        poemTitle.setText(poem.getTitle());
        poemAuthor.setText(poem.getAuthor());
        poemAuthor.setPaintFlags(poemAuthor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        poemGenre.setText(poem.getGenre());
        poemText.setText(poem.getText());
        poemText.setTextAlignment(poem.getTextAlignment());
    }
}
