package com.example.littlepoem;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditPoemFragment extends Fragment {

    private EditText editPoemText, editPoemTitleText;
    private ImageView buttonBold, buttonItalic, buttonAlignLeft, buttonAlignCenter, buttonAlignRight;
    private Button buttonCancel, buttonSave;
    private Spinner genreSpinner;
    private String genre;

    private DBHelper dbHelper;
    private PoemsDB poemsDB;

    private Toast main_toast;

    private void toggleBoldStyle() {
        // Get the selected text from EditText
        int startSelection = editPoemText.getSelectionStart();
        int endSelection = editPoemText.getSelectionEnd();
        Spannable text = editPoemText.getText();

        // Check if the selected text already has a BOLD style
        StyleSpan[] styleSpans = text.getSpans(startSelection, endSelection, StyleSpan.class);
        boolean hasBold = false;
        for (StyleSpan styleSpan : styleSpans) {
            if (styleSpan.getStyle() == Typeface.BOLD) {
                int spanStart = text.getSpanStart(styleSpan);
                int spanEnd = text.getSpanEnd(styleSpan);
                text.removeSpan(styleSpan);
                if (spanStart > startSelection && spanEnd < endSelection) {
                    for (StyleSpan boldSpan : text.getSpans(startSelection, endSelection, StyleSpan.class)) {
                        if (boldSpan.getStyle() == Typeface.BOLD) {
                            text.removeSpan(boldSpan);
                        }
                    }
                    hasBold = true;
                    break;
                }
                if (spanStart <= startSelection) {
                    text.setSpan(new StyleSpan(Typeface.BOLD), spanStart, startSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else {
                    text.setSpan(new StyleSpan(Typeface.BOLD), endSelection, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (spanEnd >= endSelection) {
                    text.setSpan(new StyleSpan(Typeface.BOLD), endSelection, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else {
                    text.setSpan(new StyleSpan(Typeface.BOLD), spanStart, startSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                hasBold = true;
            }
        }

        // If the selected text doesn't have BOLD style, set it to BOLD
        if (!hasBold) {
            text.setSpan(new StyleSpan(Typeface.BOLD), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            buttonBold.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bold_button_pushed));
        }
        else {
            buttonBold.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bold_button));
        }
    }

    private void toggleItalicStyle() {
        // Get the selected text from EditText
        int startSelection = editPoemText.getSelectionStart();
        int endSelection = editPoemText.getSelectionEnd();
        Spannable text = editPoemText.getText();

        // Check if the selected text already has an ITALIC style
        StyleSpan[] styleSpans = text.getSpans(startSelection, endSelection, StyleSpan.class);
        boolean hasItalic = false;
        for (StyleSpan styleSpan : styleSpans) {
            if (styleSpan.getStyle() == Typeface.ITALIC) {
                int spanStart = text.getSpanStart(styleSpan);
                int spanEnd = text.getSpanEnd(styleSpan);
                text.removeSpan(styleSpan);
                if (spanStart > startSelection && spanEnd < endSelection) {
                    for (StyleSpan boldSpan : text.getSpans(startSelection, endSelection, StyleSpan.class)) {
                        if (boldSpan.getStyle() == Typeface.ITALIC) {
                            text.removeSpan(boldSpan);
                        }
                    }
                    hasItalic = true;
                    break;
                }
                if (spanStart <= startSelection) {
                    text.setSpan(new StyleSpan(Typeface.ITALIC), spanStart, startSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else {
                    text.setSpan(new StyleSpan(Typeface.ITALIC), endSelection, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (spanEnd >= endSelection) {
                    text.setSpan(new StyleSpan(Typeface.ITALIC), endSelection, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                else {
                    text.setSpan(new StyleSpan(Typeface.ITALIC), spanStart, startSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                hasItalic = true;
            }
        }

        // If the selected text doesn't have ITALIC style, set it to ITALIC
        if (!hasItalic) {
            text.setSpan(new StyleSpan(Typeface.ITALIC), startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            buttonItalic.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_italic_button_pushed));
        }
        else {
            buttonItalic.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_italic_button));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_poem, container, false);

        editPoemTitleText = v.findViewById(R.id.poem_title_edit_text);
        editPoemText = v.findViewById(R.id.poem_edit_text);
        buttonBold = v.findViewById(R.id.bold_button);
        buttonItalic = v.findViewById(R.id.italic_button);
        buttonAlignLeft = v.findViewById(R.id.align_left_button);
        buttonAlignCenter = v.findViewById(R.id.align_center_button);
        buttonAlignRight = v.findViewById(R.id.align_right_button);
        buttonCancel = v.findViewById(R.id.cancel_button);
        buttonSave = v.findViewById(R.id.save_button);
        genreSpinner = v.findViewById(R.id.genre_spinner);

        dbHelper = new DBHelper(getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());

        main_toast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getResources().getString(R.string.confirm_send_poem_to_moderation));
        builder.setPositiveButton(getContext().getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String title = editPoemTitleText.getText().toString().trim();
                Spannable text = editPoemText.getText();
                int author = ((MainActivity)getActivity()).getCurrentUser();

                if(poemsDB.CreateNewPoem(title, text, author, genre)) {
                    main_toast.setText(getContext().getResources().getString(R.string.successfully_sent_on_moderation));
                    main_toast.cancel();
                    main_toast.show();
                    ((MainActivity)getActivity()).openFragment(((MainActivity)getActivity()).getCurrentMainFragment());
                }
            }
        });
        builder.setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();

        buttonBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBoldStyle();
            }
        });

        buttonItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleItalicStyle();
            }
        });

        buttonAlignLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPoemText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                Spannable spannableString = new SpannableStringBuilder(editPoemText.getText());
                editPoemText.setText(spannableString);
                buttonAlignLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_left_button_pushed));
                buttonAlignCenter.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_center_button));
                buttonAlignRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_right_button));
            }
        });

        buttonAlignCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPoemText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Spannable spannableString = new SpannableStringBuilder(editPoemText.getText());
                editPoemText.setText(spannableString);
                buttonAlignLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_left_button));
                buttonAlignCenter.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_center_button_pushed));
                buttonAlignRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_right_button));
            }
        });

        buttonAlignRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPoemText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                Spannable spannableString = new SpannableStringBuilder(editPoemText.getText());
                editPoemText.setText(spannableString);
                buttonAlignLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_left_button));
                buttonAlignCenter.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_center_button));
                buttonAlignRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_align_right_button_pushed));
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_toast.setText(getContext().getResources().getString(R.string.draft_saved));
                main_toast.cancel();
                main_toast.show();
                ((MainActivity)getActivity()).openFragment(((MainActivity)getActivity()).getCurrentMainFragment());
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editPoemTitleText.getText().toString().trim();
                Spannable text = editPoemText.getText();

                if (title.isEmpty() || text.toString().isEmpty()) {
                    main_toast.setText(R.string.error_empty_field);
                    main_toast.cancel();
                    main_toast.show();
                }
                else {
                    dialog.show();
                }
            }
        });

        editPoemText.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override public void sendAccessibilityEvent(View host, int eventType) {
                super.sendAccessibilityEvent(host, eventType);
                if (eventType == android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {

                    buttonBold.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bold_button));
                    buttonItalic.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_italic_button));
                    SpannableStringBuilder spannable = new SpannableStringBuilder(editPoemText.getText());
                    StyleSpan[] styleSpans = spannable.getSpans(editPoemText.getSelectionStart(), editPoemText.getSelectionEnd(), StyleSpan.class);
                    for (StyleSpan styleSpan : styleSpans) {
                        switch (styleSpan.getStyle()) {
                            case (Typeface.BOLD):
                                buttonBold.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bold_button_pushed));
                                break;
                            case (Typeface.ITALIC):
                                buttonItalic.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_italic_button_pushed));
                                break;
                            default:
                                buttonBold.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_bold_button));
                                buttonItalic.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_italic_button));
                                break;
                        }
                    }
                }
                else {

                }
            }
        });

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genre = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return v;
    }
}
