package com.example.littlepoem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ModeratorProfileFragment extends Fragment {

    private TextView name;
    private ImageView profile_picture, settings_button;
    private ListView publishedPoemsListView, rejectedPoemsListView, deletedPoemsListView;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moderator_profile, container, false);

        name = v.findViewById(R.id.name);
        profile_picture = v.findViewById(R.id.profile_picture);
        settings_button = v.findViewById(R.id.settings_button);
        publishedPoemsListView = v.findViewById(R.id.published_poems);
        rejectedPoemsListView = v.findViewById(R.id.rejected_poems);
        deletedPoemsListView = v.findViewById(R.id.deleted_poems);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        setData();

        List<Poem> publishedPoems = poemsDB.selectModeratorPublishedPoems(((MainActivity)getActivity()).getCurrentUser());
        PoemListAdapter adapterPublishedPoems = new PoemListAdapter(getContext(), publishedPoems);
        publishedPoemsListView.setAdapter(adapterPublishedPoems);

        List<Poem> rejectedPoems = poemsDB.selectModeratorRejectedPoems(((MainActivity)getActivity()).getCurrentUser());
        PoemListAdapter adapterRejectedPoems = new PoemListAdapter(getContext(), rejectedPoems);
        rejectedPoemsListView.setAdapter(adapterRejectedPoems);

        List<Poem> deletedPoems = poemsDB.selectModeratorDeletedPoems(((MainActivity)getActivity()).getCurrentUser());
        PoemListAdapter adapterDeletedPoems = new PoemListAdapter(getContext(), deletedPoems);
        deletedPoemsListView.setAdapter(adapterDeletedPoems);

        //Нажатие на стихотворение
        publishedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ModeratePoemFragment());
            }
        });

        rejectedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ModeratePoemFragment());
            }
        });

        deletedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ModeratePoemFragment());
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openFragment(new SettingsFragment());
            }
        });

        return v;
    }

    private void setData() {
        usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());

        name.setText(usersDB.name);
        profile_picture.setImageBitmap(usersDB.picture);
    }
}
