package com.example.littlepoem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ModeratorReviewsFragment extends Fragment {

    private EditText edit_search_text;
    private TextView type;
    private ImageView filter_button, search_button;
    private ListView unpublishedPoemsListView;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moderator_main, container, false);

        edit_search_text = v.findViewById(R.id.edit_search_text);
        type = v.findViewById(R.id.type_textView);
        filter_button = v.findViewById(R.id.filter_button);
        search_button = v.findViewById(R.id.search_button);
        unpublishedPoemsListView = v.findViewById(R.id.list_view);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());

        type.setText(getContext().getResources().getString(R.string.reviews));

        List<Poem> unpublishedPoems = poemsDB.selectUnpublishedPoems();
        PoemListAdapter adapter = new PoemListAdapter(getContext(), unpublishedPoems);
        unpublishedPoemsListView.setAdapter(adapter);

        //Нажатие на стихотворение
        unpublishedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ModeratePoemFragment());
            }
        });

        //Кнопка фильтра поиска
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersDB.clearDB();
                ((MainActivity)getActivity()).resetCurrentUser();
            }
        });

        //Кнопка поиска
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return v;
    }
}
