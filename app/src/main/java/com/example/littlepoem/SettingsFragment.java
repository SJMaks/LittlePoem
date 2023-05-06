package com.example.littlepoem;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsFragment extends Fragment {

    private ImageView profile_picture;
    private EditText edit_name_text, edit_current_password, edit_new_password, edit_repeat_new_password;
    private TextView user_agreement, login_text, role_text;
    private Switch notifications;
    private Button save_button_1, save_button_2;

    private Toast main_toast;

    private DBHelper dbHelper;
    private UsersDB usersDB;

    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagePath;
    private Bitmap imageToStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        profile_picture = v.findViewById(R.id.profile_picture);
        edit_name_text = v.findViewById(R.id.edit_name_text);
        edit_current_password = v.findViewById(R.id.edit_current_password);
        edit_new_password = v.findViewById(R.id.edit_new_password);
        edit_repeat_new_password = v.findViewById(R.id.edit_repeat_new_password);
        login_text = v.findViewById(R.id.login_text);
        role_text = v.findViewById(R.id.role_text);
        user_agreement = v.findViewById(R.id.user_agreement);
        notifications = v.findViewById(R.id.notifications_switch);
        save_button_1 = v.findViewById(R.id.save_button_1);
        save_button_2 = v.findViewById(R.id.save_button_2);

        main_toast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);

        dbHelper = new DBHelper(getContext());
        usersDB = new UsersDB(dbHelper, getContext());

        setData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getResources().getString(R.string.confirm_change_settings));
        builder.setPositiveButton(getContext().getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Converter converter = new Converter();

                usersDB.setName(edit_name_text.getText().toString());
                byte[] buf_picture = converter.drawableToByte(profile_picture.getDrawable());
                usersDB.setPicture(buf_picture);
                ((MainActivity)getActivity()).updateMenu(usersDB.id);

                if(checkChangePassword(edit_current_password.getText().toString(),
                        edit_new_password.getText().toString(),
                        edit_repeat_new_password.getText().toString())) {
                    usersDB.setPassword(edit_repeat_new_password.getText().toString());
                }

                main_toast.setText(R.string.change_settings_success);
                main_toast.cancel();
                main_toast.show();
            }
        });
        builder.setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();

        save_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCorrectName(edit_name_text.getText().toString())) {
                    main_toast.setText(R.string.error_wrong_name);
                    main_toast.cancel();
                    main_toast.show();
                }
                else if (edit_current_password.getText().toString().equals("") &&
                        edit_new_password.getText().toString().equals("") &&
                        edit_repeat_new_password.getText().toString().equals("")) {
                    dialog.show();
                }
                else if (checkChangePassword(edit_current_password.getText().toString(),
                        edit_new_password.getText().toString(),
                        edit_repeat_new_password.getText().toString())) {
                    dialog.show();
                }
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        return v;
    }

    private void setData() {
        if (usersDB.getDataByID(((MainActivity)getActivity()).getCurrentUser())) {
            login_text.setText(usersDB.login);
            edit_name_text.setText(usersDB.name);
            role_text.setText(usersDB.role);
            edit_current_password.setText("");
            edit_new_password.setText("");
            edit_repeat_new_password.setText("");
            notifications.setChecked(true);
            profile_picture.setImageBitmap(usersDB.picture);
        }
        else {
            edit_name_text.setText(this.getResources().getString(R.string.login));
            login_text.setText(this.getResources().getString(R.string.login));
            role_text.setText(this.getResources().getString(R.string.role));
            edit_current_password.setText("");
            edit_new_password.setText("");
            edit_repeat_new_password.setText("");
            notifications.setChecked(true);
            profile_picture.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_profile_empty));
        }
    }

    private boolean isCorrectName(String str) {
        String regex = "^[A-Za-zА-Яа-я]\\w{3,19}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private boolean isCorrectPassword(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isSpaceChar(c)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkChangePassword(String current_password, String new_password, String repeat_new_password) {
        if (current_password.equals("") && new_password.equals("") && repeat_new_password.equals("")) {
            return false;
        }
        else if (!current_password.equals(usersDB.password)) {
            main_toast.setText(R.string.error_wrong_current_password);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if(!isCorrectPassword(new_password)) {
            main_toast.setText(R.string.error_wrong_password);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (new_password.length() < 5 || new_password.length() > 30) {
            main_toast.setText(R.string.error_short_password);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else if (!new_password.equals(repeat_new_password)) {
            main_toast.setText(R.string.error_different_passwords);
            main_toast.cancel();
            main_toast.show();
            return false;
        }
        else {
            return true;
        }
    }

    private void chooseImage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
        catch (Exception e) {
            main_toast.setText(e.getMessage());
            main_toast.cancel();
            main_toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imagePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imagePath);
                profile_picture.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e) {
            main_toast.setText(e.getMessage());
            main_toast.cancel();
            main_toast.show();
        }
    }
}
