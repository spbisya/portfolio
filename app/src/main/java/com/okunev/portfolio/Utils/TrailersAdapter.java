package com.okunev.portfolio.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.okunev.portfolio.R;

import java.util.ArrayList;

/**
 * Created by gwa on 3/24/16.
 */
public class TrailersAdapter  extends BaseAdapter {
    ArrayList<Trailer> items = new ArrayList<>();
    Context context;

    public TrailersAdapter(ArrayList<Trailer> items, Context context) {
        if (items != null) {
            this.items = items;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position){items.remove(position); notifyDataSetChanged();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.trailer_item, parent, false);
        }

        TextView trailer_name = (TextView) convertView.findViewById(R.id.trailer_name);
        trailer_name.setText(items.get(position).getName());
        return convertView;
    }
}
