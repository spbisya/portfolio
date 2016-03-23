package com.okunev.portfolio;


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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gwa on 3/23/16.
 */
public class MovieFragment extends Fragment {
    final static String ARG_POSITION = "position";
    TextView plot;
    TextView rating;
    TextView time;
    TextView date;
    TextView name;
    ImageView imageView;
    OkHttpClient client;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_fragment,
                container, false);
        String myValue = this.getArguments().getString("id");
String url = "https://api.themoviedb.org/3/movie/"+myValue+"?api_key=183ca5d3a0f8e8239913bd2cda7c732e";

        imageView = (ImageView)view.findViewById(R.id.poster);
        name = (TextView)view.findViewById(R.id.header);
        date = (TextView)view.findViewById(R.id.date);
        time = (TextView)view.findViewById(R.id.time);
        rating = (TextView)view.findViewById(R.id.rating);
        plot = (TextView)view.findViewById(R.id.plot);
        try {
            run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
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
                   final String poster_path = "http://image.tmdb.org/t/p/w185/"+films.getString("poster_path");
                    final String date1 = films.getString("release_date");
                    final  String name1 = films.getString("original_title");
                    final String rating1 = films.getString("vote_average")+"/10";
                    final String time1 =  films.getString("runtime");
                    final    String plot1 = films.getString("overview");
                    imageView.post(new Runnable() {
                        public void run() {
                            Picasso.with(getActivity()).load(poster_path).placeholder(R.drawable.fg)
                                    .into(imageView);
                        }
                    });
                    date.post(new Runnable() {
                        public void run() {
                          date.setText(date1);
                        }
                        });
                    name.post(new Runnable() {
                        public void run() {
                            name.setText(name1);
                        }
                    });
                    rating.post(new Runnable() {
                        public void run() {
                            rating.setText(rating1);
                        }
                    });
                    time.post(new Runnable() {
                        public void run() {
                            time.setText(time1);
                        }
                    });
                    plot.post(new Runnable() {

                        public void run() {
                           plot.setText(plot1);
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
