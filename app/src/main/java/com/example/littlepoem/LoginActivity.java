package com.example.littlepoem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView reg_button;
    private Button login_button;
    private EditText edit_login, edit_password;

    private DBHelper dbHelper;
    private UsersDB usersDB;

    private Toast main_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(null);

        login_button = findViewById(R.id.login_button);
        reg_button = findViewById(R.id.reg_button);
        edit_login = findViewById(R.id.edit_login);
        edit_password = findViewById(R.id.edit_password);

        main_toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        dbHelper = new DBHelper(this);
        usersDB = new UsersDB(dbHelper, getApplicationContext());

        //Кнопка регистрации
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //Кнопка войти
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edit_login.getText().toString().trim();
                String password = edit_password.getText().toString();

                if (usersDB.GetDataByLoginPassword(login, password)) {
                    SharedPreferences preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("current_user_id", usersDB.id);
                    editor.apply();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                else {
                    main_toast.setText(R.string.error_logging);
                    main_toast.cancel();
                    main_toast.show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}