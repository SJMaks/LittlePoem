package com.example.littlepoem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        publishButton = v.findViewById(R.id.publish_button);
        rejectButton = v.findViewById(R.id.reject_button);

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
                        poemsDB.PublishPoem(poem.getId(), ((MainActivity)getActivity()).getCurrentUser());
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
                        poemsDB.RejectPoem(poem.getId(), ((MainActivity)getActivity()).getCurrentUser());
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

        return v;
    }

    @SuppressLint("WrongConstant")
    private void setData() {
        Bundle bundle = getArguments();
        poem = (Poem) bundle.getSerializable("poem");

        poemTitle.setText(poem.getTitle());
        poemAuthor.setText(poem.getAuthor());
        poemGenre.setText(poem.getGenre());
        poemText.setText(poem.getText());
        poemText.setTextAlignment(poem.getTextAlignment());
    }
}
