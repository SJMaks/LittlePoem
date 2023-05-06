package com.example.littlepoem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class WriterProfileFragment extends Fragment {

    private TextView name, role, poems_title;
    private ImageView profile_picture, action_button;
    private ListView myPoemsListView, favouritePoemsListView;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_writer_profile, container, false);

        name = v.findViewById(R.id.name);
        role = v.findViewById(R.id.role);
        poems_title = v.findViewById(R.id.poems_textView);
        profile_picture = v.findViewById(R.id.profile_picture);
        action_button = v.findViewById(R.id.action_button);
        myPoemsListView = v.findViewById(R.id.my_poems);
        favouritePoemsListView = v.findViewById(R.id.favourite_poems);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        setData();

        List<Poem> myPoems = poemsDB.selectUsersPoems(getArguments().getString("user_id"));
        PoemListAdapter adapterMyPoems = new PoemListAdapter(getContext(), myPoems);
        myPoemsListView.setAdapter(adapterMyPoems);

        //Нажатие на стихотворение
        myPoemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        usersDB.GetDataByID(getArguments().getString("user_id"));

        name.setText(usersDB.name);
        role.setText(usersDB.role);
        profile_picture.setImageBitmap(usersDB.picture);

        usersDB.GetDataByID(((MainActivity)getActivity()).getCurrentUser());

        if (usersDB.role.equals(this.getResources().getString(R.string.moderator))) {
            poems_title.setText(this.getResources().getString(R.string.poems));
            action_button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_ban_button));
        }
        else {
            poems_title.setText(this.getResources().getString(R.string.my_poems));
            action_button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_settings_button_light));
        }
    }
}
