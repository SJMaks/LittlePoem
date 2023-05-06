package com.example.littlepoem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class WriterMainFragment extends Fragment {

    private EditText edit_search_text;
    private ImageView filter_button, search_button;
    private LinearLayout add_button;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_writer_main, container, false);

        edit_search_text = v.findViewById(R.id.edit_search_text);
        filter_button = v.findViewById(R.id.filter_button);
        search_button = v.findViewById(R.id.search_button);
        add_button = v.findViewById(R.id.add_button);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        usersDB.GetDataByID(getArguments().getString("user_id"));

        //Кнопка фильтра поиска
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersDB.ClearDB();
                poemsDB.ClearDB();
                ((MainActivity)getActivity()).resetCurrentUser();
            }
        });

        //Кнопка поиска
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Poem> unpublishedPoems = poemsDB.selectUnpublishedPoems();
                for (Poem poem : unpublishedPoems) {
                    Log.d("Poem", "id: " + poem.getId() + ", title: " + poem.getTitle() + ", text: " + poem.getText() +
                            ", author: " + poem.getAuthor() + ", genre: " + poem.getGenre() + ", rating: " + poem.getRating() +
                            ", publication_date: " + poem.getPublicationDate() + ", publication_state: " + poem.getPublicationState());
                }

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditPoemFragment();
                ((MainActivity)getActivity()).openFragment(fragment);
            }
        });

        return v;
    }
}
