package com.example.littlepoem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Объявление переменных

    //Бандл
    Bundle bundle;

    //Фрагменты
    private Fragment main_fragment,
            about_app_fragment,
            settings_fragment,
            moderator_poems_fragment,
            moderator_reviews_fragment,
            moderator_complaints_fragment,
            profile_fragment;

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

        //Проверка id текущего пользователя
        if (getCurrentUser().equals("")) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        else {
            //Инициализация переменных
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            menu_button = findViewById(R.id.menu_button);

            dbHelper = new DBHelper(this);
            usersDB = new UsersDB(dbHelper, getApplicationContext());

            bundle = new Bundle();

            main_fragment = getCurrentMainFragment();
            about_app_fragment = new AboutAppFragment();
            settings_fragment = new SettingsFragment();
            moderator_poems_fragment = new ModeratorPoemsFragment();
            moderator_reviews_fragment = new ModeratorReviewsFragment();
            moderator_complaints_fragment = new ModeratorComplaintsFragment();
            profile_fragment = getCurrentProfileFragment();

            bundle.putString("user_id", getCurrentUser());
            updateMenu(getCurrentUser());

            //Инициализация основного фрагмента
            main_fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, main_fragment).commit();

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
                    NavItem clickedItem = (NavItem) parent.getItemAtPosition(position);
                    Fragment clickedFragment = clickedItem.mFragment;
                    if (clickedFragment == null) {
                        usersDB.id = null;
                        resetCurrentUser();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    else {
                        bundle.putString("user_id", getCurrentUser());
                        clickedFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, clickedFragment).commit();
                        mDrawerLayout.close();
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateMenu(String id) {
        Converter converter = new Converter();

        //Получение данных пользователя
        if (usersDB.getDataByID(id)) {
            name_text = usersDB.name;
            role_text = usersDB.role;
            profile_picture = usersDB.picture;
        }
        else {
            name_text = this.getResources().getString(R.string.login);
            role_text = this.getResources().getString(R.string.role);
            profile_picture = converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_profile_empty));
        }

        //Элементы меню
        mNavItems.removeAll(mNavItems);
        mNavItems.add(new NavItem(name_text, role_text, profile_picture, profile_fragment));
        if (usersDB.role.equals(this.getResources().getString(R.string.moderator))) {
            mNavItems.add(new NavItem(this.getResources().getString(R.string.poems), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_poems_button)), moderator_poems_fragment));
            mNavItems.add(new NavItem(this.getResources().getString(R.string.reviews), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_reviews_menu_button)), moderator_reviews_fragment));
            mNavItems.add(new NavItem(this.getResources().getString(R.string.complaints), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_complaint_menu_button)), moderator_complaints_fragment));
        }
        else {
            mNavItems.add(new NavItem(this.getResources().getString(R.string.home_button_title), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_home_button)), main_fragment));
        }
        mNavItems.add(new NavItem(this.getResources().getString(R.string.settings_button_title), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_settings_button)), settings_fragment));
        mNavItems.add(new NavItem(this.getResources().getString(R.string.info_button_title), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_info_button)), about_app_fragment));
        mNavItems.add(new NavItem(this.getResources().getString(R.string.logout_button_title), "", converter.drawableToBitmap(getApplicationContext().getResources().getDrawable(R.drawable.ic_logout_button)), null));

        //Инициализация меню
        mDrawerPane = (RelativeLayout) findViewById(R.id.menuView);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
    }

    public void openFragment(Fragment fragment) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();
    }

    public void openReadPoemFragment(Poem poem, Fragment read_fragment) {
        bundle.putSerializable("poem", poem);
        read_fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, read_fragment).commit();
    }

    public void openProfileFragment(String buf_id) {
        bundle.putString("user_id", buf_id);
        Fragment user_profile_fragment;
        usersDB.getDataByID(buf_id);
        if (usersDB.role.equals(this.getResources().getString(R.string.writer))) {
            user_profile_fragment = new WriterProfileFragment();
        }
        else {
            user_profile_fragment = new ReaderProfileFragment();
        }
        user_profile_fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, user_profile_fragment).commit();
    }

    public void resetCurrentUser() {
        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_user_id", "");
        editor.apply();
    }

    public String getCurrentUser() {
        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String current_id = preferences.getString("current_user_id", "");

        return current_id;
    }

    public Fragment getCurrentMainFragment() {
        if(usersDB.getDataByID(getCurrentUser())) {
            if (usersDB.role.equals(this.getResources().getString(R.string.writer))) {
                return new WriterMainFragment();
            }

            if (usersDB.role.equals(this.getResources().getString(R.string.reader))) {
                return new ReaderMainFragment();
            }

            return new ModeratorPoemsFragment();
        }
        else {
            return new ErrorMainFragment();
        }
    }

    public Fragment getCurrentProfileFragment() {
        if(usersDB.getDataByID(getCurrentUser())) {
            if (usersDB.role.equals(this.getResources().getString(R.string.writer))) {
                return new WriterProfileFragment();
            }

            if (usersDB.role.equals(this.getResources().getString(R.string.reader))) {
                return new ReaderProfileFragment();
            }

            return new ModeratorProfileFragment();
        }
        else {
            return new ErrorMainFragment();
        }
    }

    @Override
    public void onBackPressed() {
        openFragment(getCurrentMainFragment());
    }
}