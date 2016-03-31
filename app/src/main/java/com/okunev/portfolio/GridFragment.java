package com.okunev.portfolio;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.okunev.portfolio.R;
import com.okunev.portfolio.Utils.DBHelper;
import com.okunev.portfolio.Utils.ImageAdapter;
import com.okunev.portfolio.Utils.ImageLocalAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gwa on 3/23/16.
 */
public class GridFragment extends Fragment {
    OkHttpClient client;
    OnFragmentSelectedListener mCallback;
    ArrayList<String> urls, ids = new ArrayList<>();
    DBHelper mydb;
    GridView gridview;

    public interface OnFragmentSelectedListener {
        void onMovieSelected(int position, String id, Boolean isLocal);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentSelectedListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment,
                container, false);
        gridview = (GridView) view.findViewById(R.id.grid);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        String myValue = "";
        try {
            myValue = getArguments().getString("url");
        } catch (Exception l) {

            myValue = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
        }
        if(myValue.equals("local")){
            mydb = new DBHelper(getActivity());
            if(mydb.numberOfRows()>0) {
                ArrayList<Bitmap> posters = mydb.getAllFilmPosters();
                final ArrayList<String> film_ids = mydb.getAllNumbers();
                final ImageLocalAdapter imageLocalAdapter = new ImageLocalAdapter(getActivity(), posters);
                gridview.post(new Runnable() {
                    public void run() {
                        gridview.setAdapter(imageLocalAdapter);
                        gridview.setNumColumns(gridview.getWidth() / 185);
                        gridview.setVerticalSpacing(5);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                mCallback.onMovieSelected(position, film_ids.get(position),true);
                                Toast.makeText(getActivity(), "" + position,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            else{
                Toast.makeText(getActivity(), "There are no favourite films yet.\nGo and add some!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            urls = new ArrayList<>();
            String api_key = "&api_key=183ca5d3a0f8e8239913bd2cda7c732e";
            String url = myValue + api_key;
            try {
                Log.d("DRE", "Start method");
                run(url);
                Log.d("DRE", "End method");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (getFragmentManager().findFragmentById(R.id.gr_fragment) != null) {
            gridview.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        }
    }

    public void run(String url) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("DRE", "request is built");
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
                            gridview.setNumColumns(gridview.getWidth() / 185);
                            Log.d("DRE", "" + gridview.getColumnWidth() + " " + gridview.getWidth());
                            gridview.setVerticalSpacing(5);
                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    mCallback.onMovieSelected(position, ids.get(position),false);
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


}
