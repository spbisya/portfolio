package com.okunev.portfolio;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gwa on 3/23/16.
 */
public class GridFragment extends Fragment {
    OkHttpClient client;
    OnFragmentSelectedListener mCallback;
    ArrayList<String> urls = new ArrayList<>();
ArrayList<String> ids = new ArrayList<>();
    // Container Activity must implement this interface
    public interface OnFragmentSelectedListener {
        public void onMovieSelected(int position, String id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFragmentSelectedListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentSelectedListener");
        }
    }


    public void run(String url) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("DRE","request is built");
        // Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("DRE", "FAIL");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("DRE", "Response is got");
                String s = response.body().string();
                Log.d("DRE", s);

                JSONObject films = null;
                try {
                    Log.d("DRE", "123films123");
                    films = new JSONObject(s);
                    Log.d("DRE", "films");
                    JSONArray array = films.getJSONArray("results");
                    for (int i = 0; i < 20; i++) {
                        JSONObject film = array.getJSONObject(i);
                        urls.add("http://image.tmdb.org/t/p/w185/" + film.getString("poster_path"));
                        ids.add(film.getString("id"));
                        Log.d("DRE", "http://image.tmdb.org/t/p/w185/" + film.getString("poster_path"));
                    }
                    gridview.post(new Runnable() {
                        public void run() {
                            gridview.setAdapter(new ImageAdapter(getActivity(), urls));
                            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                            Display display = wm.getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int width = size.x;
                            gridview.setNumColumns(width / 185);
                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    mCallback.onMovieSelected(position, ids.get(position));
                                    Toast.makeText(getActivity(), "" + position,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("DRE", e.getMessage());
                }
            }
        });
    }



    GridView gridview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment,
                container, false);
        String myValue = this.getArguments().getString("url");
        urls = new ArrayList<>();
        gridview = (GridView) view.findViewById(R.id.grid);
        String api_key = "&api_key=183ca5d3a0f8e8239913bd2cda7c732e";
        String url = myValue + api_key;
        try {
            Log.d("DRE","Start method");
            run(url);
            Log.d("DRE", "End method");

        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }
}
