package com.example.littlepoem;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NavItem {
    String mTitle;
    String mSubtitle;
    Bitmap mIcon;
    Fragment mFragment;

    public NavItem(String title, String subtitle, Bitmap icon, Fragment fragment) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
        mFragment = fragment;
    }
}

class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    public Fragment getFragment(int position) {
        return mNavItems.get(position).mFragment;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        ImageView iconView1 = (ImageView) view.findViewById(R.id.icon1);
        ImageView iconView2 = (ImageView) view.findViewById(R.id.icon2);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)titleView.getLayoutParams();

        titleView.setText( mNavItems.get(position).mTitle );
        if (position == 0) {
            cardView.setVisibility(View.VISIBLE);
            iconView1.setImageBitmap(mNavItems.get(position).mIcon);
        }
        else {
            cardView.setVisibility(View.INVISIBLE);
            iconView2.setImageBitmap(mNavItems.get(position).mIcon);
        }
        if (mNavItems.get(position).mSubtitle.equals("")) {
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.icon2);
            subtitleView.setHeight(0);
            titleView.setLayoutParams(params);
        }
        else {
            titleView.setLayoutParams(params);
            subtitleView.setText( mNavItems.get(position).mSubtitle );
        }

        return view;
    }
}
