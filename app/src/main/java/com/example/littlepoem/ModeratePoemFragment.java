package com.example.littlepoem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ModeratePoemFragment extends Fragment {
    private TextView poemTitle, poemAuthor, poemGenre, poemText;
    private Button publishButton, rejectButton;

    Poem poem;

    private DBHelper dbHelper;

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

        setData();

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    private void setData() {
        Bundle bundle = getArguments();
        poem = (Poem) bundle.getSerializable("poem");

        poemTitle.setText(poem.getTitle());
        poemAuthor.setText(poem.getAuthor());
        poemGenre.setText(poem.getGenre());
        poemText.setText(poem.getText());
    }
}
