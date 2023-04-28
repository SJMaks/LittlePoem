package com.example.littlepoem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Объявление переменных

    //Бандл
    Bundle bundle;
    String current_id;

    //Фрагменты
    private WriterMainFragment writer_main_fragment;
    private ReaderMainFragment reader_main_fragment;
    private ModeratorMainFragment moderator_main_fragment;
    private AboutAppFragment about_app_fragment;
    private SettingsFragment settings_fragment;
    private EditPoemFragment edit_poem_fragment;

    //Меню
    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    //Элементы окна
    private ImageView menu_button;
    private String name_text, role_text;
    private Bitmap profile_picture;

    //База данных
    private DBHelper dbHelper;
    private UsersDB usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(null);

        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        current_id = preferences.getString("current_user_id", "");

        //Проверка id текущего пользователя
        if (current_id.equals("")) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        else {
            //Инициализация переменных
            writer_main_fragment = new WriterMainFragment();
            reader_main_fragment = new ReaderMainFragment();
            moderator_main_fragment = new ModeratorMainFragment();
            about_app_fragment = new AboutAppFragment();
            settings_fragment = new SettingsFragment();
            edit_poem_fragment = new EditPoemFragment();

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            menu_button = findViewById(R.id.menu_button);

            dbHelper = new DBHelper(this);
            usersDB = new UsersDB(dbHelper, getApplicationContext());

            bundle = new Bundle();

            bundle.putString("user_id", current_id);
            updateMenu(current_id);

            //Инициализация основного фрагмента
            if (usersDB.role.equals(this.getResources().getString(R.string.writer))) {
                writer_main_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, writer_main_fragment).commit();
            }
            else  if (usersDB.role.equals(this.getResources().getString(R.string.reader))) {
                reader_main_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, reader_main_fragment).commit();
            }
            else {
                moderator_main_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, moderator_main_fragment).commit();
            }

            //Кнопка меню
            menu_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });

            //Клик на элемент меню
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case (0): {
                            mDrawerLayout.close();
                            break;
                        }
                        case (1): {
                            writer_main_fragment.setArguments(bundle);
                            reader_main_fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, usersDB.role.equals(getResources().getString(R.string.writer))?writer_main_fragment:usersDB.role.equals(getResources().getString(R.string.reader))?reader_main_fragment:moderator_main_fragment).commit();
                            mDrawerLayout.close();
                            break;
                        }
                        case (2): {
                            settings_fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, settings_fragment).commit();
                            mDrawerLayout.close();
                            break;
                        }
                        case (3): {
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, about_app_fragment).commit();
                            mDrawerLayout.close();
                            break;
                        }
                        case (4): {
                            usersDB.id = null;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("current_user_id", "");
                            editor.apply();
                            Intent intent = new Intent(view.getContext(), LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void updateMenu(String id) {
        //Получение данных пользователя
        if (usersDB.GetDataByID(id)) {
            name_text = usersDB.name;
            role_text = usersDB.role;
            profile_picture = usersDB.picture;
        }
        else {
            name_text = this.getResources().getString(R.string.login);
            role_text = this.getResources().getString(R.string.role);
            profile_picture = usersDB.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_profile_empty));
        }

        //Элементы меню
        mNavItems.removeAll(mNavItems);
        mNavItems.add(new NavItem(name_text, role_text, profile_picture));
        mNavItems.add(new NavItem(this.getResources().getString(R.string.home_button_title), "", usersDB.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_home_button))));
        mNavItems.add(new NavItem(this.getResources().getString(R.string.settings_button_title), "", usersDB.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_settings_button))));
        mNavItems.add(new NavItem(this.getResources().getString(R.string.info_button_title), "", usersDB.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_info_button))));
        mNavItems.add(new NavItem(this.getResources().getString(R.string.logout_button_title), "", usersDB.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_logout_button))));

        //Инициализация меню
        mDrawerPane = (RelativeLayout) findViewById(R.id.menuView);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
    }

    public void openFragment(int i) {
        switch (i){
            case (0):
                writer_main_fragment.setArguments(bundle);
                reader_main_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, usersDB.role.equals(getResources().getString(R.string.writer))?writer_main_fragment:usersDB.role.equals(getResources().getString(R.string.reader))?reader_main_fragment:moderator_main_fragment).commit();
                break;
            case (1):
                settings_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, settings_fragment).commit();
                break;
            case (2):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, about_app_fragment).commit();
                break;
            case (3):
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, edit_poem_fragment).commit();
                break;
        }
    }

    public void resetCurrentUser() {
        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_user_id", "");
        editor.apply();
    }
}