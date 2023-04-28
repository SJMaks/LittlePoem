package com.example.littlepoem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

class PoemListAdapter extends BaseAdapter {

    Context mContext;
    List<Poem> mPoemItems;

    public PoemListAdapter(Context context, List<Poem> poemItems) {
        mContext = context;
        mPoemItems = poemItems;
    }

    @Override
    public int getCount() {
        return mPoemItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mPoemItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mPoemItems.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.poem_item, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.poem_title);
        TextView authorView = (TextView) convertView.findViewById(R.id.poem_author);
        TextView ratingView = (TextView) convertView.findViewById(R.id.poem_rating);

        Poem poem = mPoemItems.get(position);
        titleView.setText(poem.getTitle());
        authorView.setText(poem.getAuthor());
        ratingView.setText(String.format("%.1f", poem.getRating()) + "/5");

        return convertView;
    }
}
