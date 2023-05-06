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

public class ReaderProfileFragment extends Fragment {

    private TextView name, role;
    private ImageView profile_picture, settings_button;
    private ListView favouritePoemsListView;

    private DBHelper dbHelper;
    private UsersDB usersDB;
    private PoemsDB poemsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader_profile, container, false);

        name = v.findViewById(R.id.name);
        role = v.findViewById(R.id.role);
        profile_picture = v.findViewById(R.id.profile_picture);
        settings_button = v.findViewById(R.id.settings_button);
        favouritePoemsListView = v.findViewById(R.id.favourite_poems);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        setData();

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openFragment(new SettingsFragment());
            }
        });

        return v;
    }

    private void setData() {
        usersDB.GetDataByID(((MainActivity)getActivity()).getCurrentUser());

        name.setText(usersDB.name);
        role.setText(usersDB.role);
        profile_picture.setImageBitmap(usersDB.picture);
    }
}
