package com.example.littlepoem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class ComplaintListAdapter extends BaseAdapter {

    Context mContext;
    List<Complaint> mComplaintItems;

    public ComplaintListAdapter(Context context, List<Complaint> complaintItems) {
        mContext = context;
        mComplaintItems = complaintItems;
    }

    @Override
    public int getCount() {
        return mComplaintItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mComplaintItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mComplaintItems.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.complaint_item, parent, false);
        }

        TextView poemTitleView = (TextView) convertView.findViewById(R.id.complaint_title);
        TextView authorTitleView = (TextView) convertView.findViewById(R.id.complaint_author);

        Complaint complaint = mComplaintItems.get(position);
        if (complaint.getPoemID().equals("0")) {
            poemTitleView.setText(complaint.getReviewID());
        }
        else {
            poemTitleView.setText(complaint.getPoemName());
        }
        authorTitleView.setText(complaint.getUserName());

        return convertView;
    }
}
