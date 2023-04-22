package com.example.littlepoem;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditPoemFragment extends Fragment {

    private EditText editPoemText;
    private ImageView buttonBold, buttonItalic, buttonAlignLeft, buttonAlignCenter, buttonAlignRight;

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
                text.removeSpan(styleSpan);
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
                text.removeSpan(styleSpan);
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

        editPoemText = v.findViewById(R.id.poem_edit_text);
        buttonBold = v.findViewById(R.id.bold_button);
        buttonItalic = v.findViewById(R.id.italic_button);
        buttonAlignLeft = v.findViewById(R.id.align_left_button);
        buttonAlignCenter = v.findViewById(R.id.align_center_button);
        buttonAlignRight = v.findViewById(R.id.align_right_button);



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

        editPoemText.setAccessibilityDelegate(new View.AccessibilityDelegate() {
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

        return v;
    }
}
