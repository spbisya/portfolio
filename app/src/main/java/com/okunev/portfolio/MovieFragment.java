package com.okunev.portfolio;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.okunev.portfolio.Utils.DBHelper;
import com.okunev.portfolio.Utils.Trailer;
import com.okunev.portfolio.Utils.TrailersAdapter;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
public class MovieFragment extends Fragment {
    TextView plot;
    TextView rating;
    TextView time;
    TextView date;
    TextView name;
    ImageView imageView;
    ListView trailers_list;
    ListView review;
    OkHttpClient client;
    String myValue;
    Button addToFav;
    private ArrayList<Trailer> data = new ArrayList<>();
    private ArrayList<String> data2 = new ArrayList<>();
    private ArrayList<Trailer> data3 = new ArrayList<>();
    private ArrayList<String> data4 = new ArrayList<>();
    ArrayList<String> trailers = new ArrayList<>();
    TrailersAdapter adapter = new TrailersAdapter(data, getActivity());
    ArrayList<String> links = new ArrayList<>();

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    DBHelper mydb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.movie_fragment,
                container, false);
        try{
        if (savedInstanceState != null) {
            myValue = savedInstanceState.getString("id");
        }
        }
        catch (Exception l){
            Toast.makeText(getActivity(),l.getMessage(),Toast.LENGTH_LONG).show();
        }
        imageView = (ImageView) view.findViewById(R.id.poster);
        name = (TextView) view.findViewById(R.id.header);
        date = (TextView) view.findViewById(R.id.date);
        time = (TextView) view.findViewById(R.id.time);
        rating = (TextView) view.findViewById(R.id.rating);
        plot = (TextView) view.findViewById(R.id.plot);
        trailers_list = (ListView) view.findViewById(R.id.trailer);
        review = (ListView) view.findViewById(R.id.review);
        addToFav = (Button) view.findViewById(R.id.addToFav);

        return view;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public ArrayList<ArrayList<String>> getInf(String s) {
        JSONObject traile = null;
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        ArrayList<String> url_list = new ArrayList<>(), names_list = new ArrayList<>();
        try {
            traile = new JSONObject(s);
            JSONArray array = traile.getJSONArray("trailers");
            Integer length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject part = array.getJSONObject(i);
                String name = part.getString("name");
                String link = part.getString("link");
                names_list.add(name);
                url_list.add(link);
            }
            arrayLists.add(names_list);
            arrayLists.add(url_list);
            return arrayLists;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public void onStart() {
        super.onStart();
        addToFav.setText("Add to favourites");
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailers_arr = "{\"trailers\" : [";
                for (int i = 0; i < trailers.size(); i++) {
                    trailers_arr += "{\"name\" : \"" + trailers.get(i) + "\", \"link\" : \"" + links.get(i) + "\"},";
                }
                trailers_arr = trailers_arr.substring(0, trailers_arr.length() - 1);
                trailers_arr += "]}";

                String reviews_arr = "{\"trailers\" : [";
                for (int i = 0; i < names.size(); i++) {
                    reviews_arr += "{\"name\" : \"" + names.get(i) + "\", \"link\" : \"" + urls.get(i) + "\"},";
                }
                reviews_arr = reviews_arr.substring(0, reviews_arr.length() - 1);
                reviews_arr += "]}";
                mydb = new DBHelper(getActivity());
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();
                byte[] data = getBitmapAsByteArray(bitmap);
                if(mydb.getFilmbyNumber(myValue).getCount()==0) {
                    if (mydb.insertFilm(myValue, name.getText().toString(), date.getText().toString(), time.getText().toString(),
                            rating.getText().toString(), plot.getText().toString(), trailers_arr, reviews_arr, data)) {
                        Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                        addToFav.setText("In favourites");
                        addToFav.setEnabled(false);
                    } else {
                        Toast.makeText(getContext(), "Err: not added!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Already in favourites!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bundle args = getArguments();
        if (args != null) {
            myValue = args.getString("id");
            if (args.getBoolean("isLocal")) {
                addToFav.setText("In favourites");
                addToFav.setEnabled(false);
                mydb = new DBHelper(getActivity());
                Cursor rs = mydb.getFilmbyNumber(myValue);
                rs.moveToFirst();
                name.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME)));
                date.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_DATE)));
                time.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TIME)));
                rating.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_RATE)));
                plot.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PLOT)));
                final String trails = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TRAILERS));
                for (String item : getInf(trails).get(0)) {
                    data3.add(new Trailer(item));
                }
                adapter = new TrailersAdapter(data3, getActivity());
                trailers_list.post(new Runnable() {
                    public void run() {
                        trailers_list.setAdapter(adapter);
                        trailers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (!links.get(position).equals("null")) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getInf(trails).get(1).get(position)));
                                    startActivity(browserIntent);
                                }
                            }
                        });
                    }
                });
                final String reviews = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_REVIEWS));
                for (String item : getInf(reviews).get(0)) {
                    data4.add(item);
                }
                final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, data4);
                review.post(new Runnable() {
                    public void run() {
                        review.setAdapter(adapter1);
                        review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getInf(reviews).get(1).get(position)));
                                startActivity(browserIntent);
                            }
                        });


                    }

                });
                byte[] imgByte = rs.getBlob(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_POSTER));
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                if (!rs.isClosed()) {
                    rs.close();
                }
            } else {
                String url = "https://api.themoviedb.org/3/movie/" + myValue + "?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
                try {
                    run(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                url = "https://api.themoviedb.org/3/movie/" + myValue + "/trailers?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
                try {
                    loadTrailer(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                url = "https://api.themoviedb.org/3/movie/" + myValue + "/reviews?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
                try {
                    loadReview(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("DRE", "pss " + myValue);
            String url = "https://api.themoviedb.org/3/movie/" + myValue + "?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
            try {
                run(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            url = "https://api.themoviedb.org/3/movie/" + myValue + "/trailers?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
            try {
                loadTrailer(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            url = "https://api.themoviedb.org/3/movie/" + myValue + "/reviews?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
            try {
                loadReview(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadReview(String url) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("DRE", "request is built");
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
                    films = new JSONObject(s);
                    JSONArray tr = films.getJSONArray("results");
                    int length = tr.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            JSONObject un = tr.getJSONObject(i);
                            //    String text =  un.getString("content");
                            String link = un.getString("url");
                            String name = un.getString("author");
                            names.add(name);
                            urls.add(link);
                        }
                        for (String item : names) {
                            data2.add(item);
                        }
                    } else {
                        names.add("No reviews available");
                        urls.add("null");
                    }
                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, data2);

                    review.post(new Runnable() {
                        public void run() {
                            review.setAdapter(adapter);
                            review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls.get(position)));
                                    startActivity(browserIntent);
                                }
                            });


                        }

                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadTrailer(String url) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("DRE", "request is built");
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
                String bif = "https://www.youtube.com/watch?v=";
                JSONObject films = null;
                try {
                    films = new JSONObject(s);
                    JSONArray tr = films.getJSONArray("youtube");
                    int length = tr.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            JSONObject un = tr.getJSONObject(i);
                            String link = bif + un.getString("source");
                            String name = un.getString("name");
                            trailers.add(name);
                            links.add(link);
                        }
                    } else {
                        links.add("null");
                        trailers.add("No trailers available");
                    }
                    ((Spotify)getActivity()).setYoutube_url(links.get(0));
                    for (String item : trailers) {
                        data.add(new Trailer(item));
                    }
                    adapter = new TrailersAdapter(data, getActivity());
                    trailers_list.post(new Runnable() {
                        public void run() {
                            trailers_list.setAdapter(adapter);
                            trailers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (!links.get(position).equals("null")) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(position)));
                                        startActivity(browserIntent);
                                    }
                                }
                            });
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void run(String url) throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("DRE", "request is built");
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
                    final String poster_path = "http://image.tmdb.org/t/p/w185/" + films.getString("poster_path");
                    final String date1 = films.getString("release_date");
                    final String name1 = films.getString("original_title");
                    final String rating1 = films.getString("vote_average") + "/10";
                    final String time1 = films.getString("runtime");
                    final String plot1 = films.getString("overview");
                    imageView.post(new Runnable() {
                        public void run() {
                            Picasso.with(getActivity()).load(poster_path).placeholder(R.drawable.fg).error(R.drawable.err)
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", myValue);
    }

}
