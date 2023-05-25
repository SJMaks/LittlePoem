package com.example.littlepoem;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReadComplaintFragment extends Fragment {
    private TextView poemTitle, complaintAuthor, complaintText;
    private Button delete_button;

    private Complaint complaint;

    private DBHelper dbHelper;
    private PoemsDB poemsDB;
    private ComplaintsDB complaintsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_read_complaint, container, false);

        poemTitle = v.findViewById(R.id.poem_title);
        complaintAuthor = v.findViewById(R.id.complaint_author);
        complaintText = v.findViewById(R.id.complaint_text);
        delete_button = v.findViewById(R.id.delete_button);

        dbHelper = new DBHelper(getContext());
        poemsDB = new PoemsDB(dbHelper, getContext());
        complaintsDB = new ComplaintsDB(dbHelper, getContext());


        Bundle bundle = getArguments();
        complaint = (Complaint) bundle.getSerializable("complaint");

        if (complaint.getPoemID().equals("0")) {
            poemTitle.setText(complaint.getReviewID());
        }
        else {
            poemTitle.setText(complaint.getPoemName());
        }
        poemTitle.setPaintFlags(poemTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        complaintAuthor.setText(complaint.getUserName());
        complaintAuthor.setPaintFlags(complaintAuthor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        complaintText.setText(complaint.getComplaintText());

        poemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openReadPoemFragment(poemsDB.getPoemByID(complaint.getPoemID()), new ModeratePoemFragment());
            }
        });

        complaintAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openProfileFragment(complaint.getUserID());
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintsDB.deleteComplaint(complaint.getId());
                ((MainActivity)getActivity()).openFragment(new ModeratorComplaintsFragment());
            }
        });

        return v;
    }
}
