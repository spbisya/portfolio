package com.okunev.portfolio.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.okunev.portfolio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gwa on 3/25/16.
 */
public class ImageLocalAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> urls = new ArrayList<>();

    public ImageLocalAdapter(Context c, ArrayList<Bitmap> urls) {
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
        imageView.setImageBitmap(urls.get(position));
        return imageView;
    }
}