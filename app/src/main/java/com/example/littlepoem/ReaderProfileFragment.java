package com.example.littlepoem;

import android.os.Bundle;
import android.text.TextUtils;
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

public class ReaderProfileFragment extends Fragment {

    private TextView name, role, liked_poems_title;
    private ImageView profile_picture, action_button;
    private ListView likedPoemsListView;

    private DBHelper dbHelper;
    private UsersDB currentUserDB, profileUserDB;
    private PoemsDB poemsDB;

    private String liked_poems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader_profile, container, false);

        name = v.findViewById(R.id.name);
        role = v.findViewById(R.id.role);
        liked_poems_title = v.findViewById(R.id.liked_poems_textView);
        profile_picture = v.findViewById(R.id.profile_picture);
        action_button = v.findViewById(R.id.action_button);
        likedPoemsListView = v.findViewById(R.id.liked_poems);

        dbHelper = new DBHelper(getContext());
        currentUserDB = new UsersDB(dbHelper, getContext());
        profileUserDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        setData();

        return v;
    }

    private void setData() {
        profileUserDB.getDataByID(getArguments().getString("user_id"));
        currentUserDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());

        name.setText(profileUserDB.name);
        role.setText(profileUserDB.role);
        profile_picture.setImageBitmap(profileUserDB.picture);

        action_button.setVisibility(View.INVISIBLE);
        liked_poems_title.setVisibility(View.GONE);
        likedPoemsListView.setVisibility(View.GONE);

        if (currentUserDB.id.equals(profileUserDB.id)) {
            action_button.setVisibility(View.VISIBLE);
            action_button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_settings_button_light));
            liked_poems_title.setVisibility(View.VISIBLE);
            likedPoemsListView.setVisibility(View.VISIBLE);
            liked_poems = TextUtils.join(",", profileUserDB.liked_poems);

            List<Poem> likedPoems = poemsDB.selectUsersLikedPoems(liked_poems);
            PoemListAdapter adapterLikedPoems = new PoemListAdapter(getContext(), likedPoems);
            likedPoemsListView.setAdapter(adapterLikedPoems);

            likedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                    if (currentUserDB.role.equals(getResources().getString(R.string.moderator))) {
                        ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ModeratePoemFragment());
                    }
                    else {
                        ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ReadPoemFragment());
                    }
                }
            });

            action_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).openFragment(new SettingsFragment());
                }
            });
        }
        else if (currentUserDB.role.equals(this.getResources().getString(R.string.moderator))) {
            action_button.setVisibility(View.VISIBLE);
            action_button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_ban_button));
            liked_poems_title.setVisibility(View.VISIBLE);
            likedPoemsListView.setVisibility(View.VISIBLE);
            liked_poems = TextUtils.join(",", profileUserDB.liked_poems);

            List<Poem> likedPoems = poemsDB.selectUsersLikedPoems(liked_poems);
            PoemListAdapter adapterLikedPoems = new PoemListAdapter(getContext(), likedPoems);
            likedPoemsListView.setAdapter(adapterLikedPoems);

            likedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                    if (currentUserDB.role.equals(getResources().getString(R.string.moderator))) {
                        ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ModeratePoemFragment());
                    }
                    else {
                        ((MainActivity)getActivity()).openReadPoemFragment(clickedPoem, new ReadPoemFragment());
                    }
                }
            });

            action_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
        }
    }
}
