package com.okunev.portfolio;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.InetAddress;

/**
 * Created by gwa on 3/19/16.
 */
public class Spotify extends AppCompatActivity implements GridFragment.OnFragmentSelectedListener {
    String url1;
    Menu myMenu;
    public String youtube_url = "";

    public void setYoutube_url(String youtube_url) {
        this.youtube_url = youtube_url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);
        if(isNetworkConnected()) {
            url1 = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            getSupportActionBar().setTitle("Pop movies");
            if (findViewById(R.id.gr_fragment) != null) {
                if (savedInstanceState != null) {
                    try {
                        GridFragment firstFragment = new GridFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", savedInstanceState.getString("url1"));
                        firstFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.gr_fragment, firstFragment).commit();
                    }
                    catch (Exception l){
                        Toast.makeText(this,l.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                try{
                GridFragment firstFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, firstFragment).commit();
                }
                catch (Exception l){
                    Toast.makeText(this,l.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                if (savedInstanceState != null) {
                    return;
                }
                try{
                GridFragment firstFragment = new GridFragment();
                Bundle bundle = new Bundle();
                url1 = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                bundle.putString("url", url1);
                firstFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, firstFragment).commit();
                }
                catch (Exception l){
                    Toast.makeText(this,l.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            try{
            getSupportActionBar().setTitle("Favourites");
            Bundle bundle = new Bundle();
            url1 = "local";
            bundle.putString("url", url1);
            if (findViewById(R.id.gr_fragment) != null) {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, fragInfo).commit();
            } else {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragInfo).commit();
            }
            }
            catch (Exception l){
                Toast.makeText(this,l.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onMovieSelected(int position, String id, Boolean isLocal) {
        try{
        getSupportActionBar().setTitle("Movie details");
        if(myMenu != null){
            myMenu.findItem(R.id.action_share)
                    .setVisible(true);
            myMenu.findItem(R.id.action_settings)
                    .setVisible(false);
            myMenu.findItem(R.id.action_settings1)
                    .setVisible(false);
            myMenu.findItem(R.id.action_local)
                    .setVisible(false);
        }
        GridFragment gridFragment;
        try {
            gridFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
        } catch (Exception l) {
            gridFragment = null;
        }
        if (gridFragment != null) {
            MovieFragment newFragment = new MovieFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putBoolean("isLocal", isLocal);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mov_fragment, newFragment);
            transaction.commit();
        } else {
            MovieFragment newFragment = new MovieFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putBoolean("isLocal", isLocal);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        }
        catch (Exception l){
            Toast.makeText(this,l.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportActionBar().getTitle().equals("Movie details")) {
            getSupportActionBar().setTitle("Pop movies");
            if(myMenu != null){
                myMenu.findItem(R.id.action_share)
                        .setVisible(false);
                myMenu.findItem(R.id.action_settings)
                        .setVisible(true);
                myMenu.findItem(R.id.action_settings1)
                        .setVisible(true);
                myMenu.findItem(R.id.action_local)
                        .setVisible(true);
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Boolean twoPanel = false;
        GridFragment gr;
        try {
            gr = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
        } catch (Exception l) {
            gr = null;
        }
        if (gr != null) {
            twoPanel = true;
            // If article frag is available, we're in two-pane layout...
        }
        if (id == R.id.action_settings) {
            getSupportActionBar().setTitle("Pop movies");
            Bundle bundle = new Bundle();
            url1 = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            bundle.putString("url", url1);
            if (twoPanel) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, gr).commit();
            } else {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragInfo).commit();
            }
        } else if (id == R.id.action_settings1) {
            getSupportActionBar().setTitle("Pop movies");
            Bundle bundle = new Bundle();
            url1 = "http://api.themoviedb.org/3/discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc";
            bundle.putString("url", url1);
            if (twoPanel) {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, fragInfo).commit();
            } else {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragInfo).commit();
            }
        } else if (id == R.id.action_local){
            getSupportActionBar().setTitle("Favourites");
            Bundle bundle = new Bundle();
            url1 = "local";
            bundle.putString("url", url1);
            if (twoPanel) {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, fragInfo).commit();
            } else {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragInfo).commit();
            }
        }
        else{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is the best movie! You should watch it!!!\n"
            +youtube_url);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url1", url1);
    }

}
