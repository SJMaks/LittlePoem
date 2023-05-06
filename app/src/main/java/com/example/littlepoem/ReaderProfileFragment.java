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

    private TextView name, role;
    private ImageView profile_picture, action_button;
    private ListView likedPoemsListView;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    private String liked_poems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader_profile, container, false);

        name = v.findViewById(R.id.name);
        role = v.findViewById(R.id.role);
        profile_picture = v.findViewById(R.id.profile_picture);
        action_button = v.findViewById(R.id.action_button);
        likedPoemsListView = v.findViewById(R.id.liked_poems);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        setData();

        List<Poem> likedPoems = poemsDB.selectUsersLikedPoems(liked_poems);
        PoemListAdapter adapterLikedPoems = new PoemListAdapter(getContext(), likedPoems);
        likedPoemsListView.setAdapter(adapterLikedPoems);

        //Нажатие на стихотворение
        likedPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Poem clickedPoem = (Poem) parent.getItemAtPosition(position);
                if (usersDB.role.equals(getResources().getString(R.string.moderator))) {
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
                if (usersDB.role.equals(getResources().getString(R.string.moderator))) {

                }
                else {
                    ((MainActivity)getActivity()).openFragment(new SettingsFragment());
                }
            }
        });

        return v;
    }

    private void setData() {
        usersDB.getDataByID(getArguments().getString("user_id"));

        name.setText(usersDB.name);
        role.setText(usersDB.role);
        profile_picture.setImageBitmap(usersDB.picture);
        liked_poems = TextUtils.join(",", usersDB.liked_poems);

        usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser());

        if (usersDB.role.equals(this.getResources().getString(R.string.moderator))) {
            action_button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_ban_button));
        }
        else {
            action_button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_settings_button_light));
        }
    }
}
