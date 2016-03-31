package com.okunev.portfolio.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.okunev.portfolio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gwa on 3/23/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> urls = new ArrayList<>();
    Boolean local = false;

    public ImageAdapter(Context c, ArrayList<String> urls) {
        this.urls = urls;
        mContext = c;
    }



    public int getCount() {
        return urls.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
           imageView.setLayoutParams(new GridView.LayoutParams(185, 278));
     //       imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
           // imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(urls.get(position)).placeholder(R.drawable.fg).error(R.drawable.err)
                .into(imageView);
        return imageView;
    }
}