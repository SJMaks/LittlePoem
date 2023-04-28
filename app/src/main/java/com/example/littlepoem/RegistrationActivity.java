package com.example.littlepoem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    public static RegistrationActivity reg_activity;

    private TextView login_button;
    private Button register_button;
    private EditText edit_login, edit_password, edit_repeat_password;
    private RadioGroup role_choice;
    private DBHelper dbHelper;
    private UsersDB usersDB;
    private Toast main_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(null);

        reg_activity = this;

        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.register_button);
        edit_login = findViewById(R.id.edit_login);
        edit_password = findViewById(R.id.edit_password);
        edit_repeat_password = findViewById(R.id.edit_repeat_password);
        role_choice = findViewById(R.id.role_choice);

        main_toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        dbHelper = new DBHelper(this);
        usersDB = new UsersDB(dbHelper, getApplicationContext());

        //Кнопка войти
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //Кнопка зарегистрироваться
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edit_login.getText().toString().trim();
                String password = edit_password.getText().toString();
                String repeat_password = edit_repeat_password.getText().toString();
                String role;
                if ((int)role_choice.getCheckedRadioButtonId() == -1) {
                    role = "";
                }
                else if ((int)role_choice.getCheckedRadioButtonId() % 2 == 1) {
                    role = getResources().getString(R.string.writer);
                }
                else {
                    role = getResources().getString(R.string.reader);
                }

                if (checkData(login, password, repeat_password, role)) {
                    Converter converter = new Converter();
                    byte[] defalt_picture = converter.drawableToByte(getApplicationContext().getResources().getDrawable(R.drawable.ic_profile_empty));
                    if(usersDB.CreateNewUser(login, password, role, defalt_picture)) {
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("current_user_id", usersDB.id);
                        editor.apply();
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    //Проверка логина на корректность
    private boolean isCorrectLogin(String str) {
        String regex = "^[A-Za-z]\\w{4,29}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //Проверка строки пароля на пробелы
    private boolean isCorrectPassword(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isSpaceChar(c)) {
                return false;
            }
        }
        return true;
    }

    //Проверка данных для регистрации на корректность
    private boolean checkData(String login, String password, String repeat_password, String role) {
        if (login.isEmpty() || password.isEmpty() || repeat_password.isEmpty() || role == "") {
            main_toast.setText(R.string.error_empty_field);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (usersDB.CheckLogin(login)) {
            main_toast.setText(R.string.error_login_already_exists);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (!isCorrectLogin(login)) {
            main_toast.setText(R.string.error_wrong_login);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (!isCorrectPassword(password)) {
            main_toast.setText(R.string.error_wrong_password);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (!password.equals(repeat_password)) {
            main_toast.setText(R.string.error_different_passwords);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (password.length() < 5 || password.length() > 30) {
            main_toast.setText(R.string.error_short_password);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else return true;
    }
}