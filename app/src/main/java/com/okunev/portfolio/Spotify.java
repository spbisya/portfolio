package com.okunev.portfolio;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by gwa on 3/19/16.
 */
public class Spotify extends AppCompatActivity implements GridFragment.OnFragmentSelectedListener {
    String url1;
    String currurl;

    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);
        currurl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
        getSupportActionBar().setTitle("Pop movies");
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.gr_fragment) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                GridFragment firstFragment = new GridFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", savedInstanceState.getString("url1"));
                firstFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, firstFragment).commit();
                return;
            }
            GridFragment firstFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.gr_fragment, firstFragment).commit();
        } else {
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            GridFragment firstFragment = new GridFragment();
            Bundle bundle = new Bundle();

            url1 = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            bundle.putString("url", url1);
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(bundle);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, firstFragment).commit();
        }
    }


    @Override
    public void onMovieSelected(int position, String id) {
        getSupportActionBar().setTitle("Movie details");
        GridFragment articleFrag;
        try {
            articleFrag = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
        } catch (Exception l) {
            articleFrag = null;
        }
        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=183ca5d3a0f8e8239913bd2cda7c732e";
//              try {
//                  articleFrag.run(url);
//              } catch (IOException e) {
//                  e.printStackTrace();
//              }
            MovieFragment newFragment = new MovieFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container, newFragment);
          //  transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            MovieFragment newFragment = new MovieFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportActionBar().getTitle().equals("Movie details")) {
//            Boolean twoPanel = false;
//            GridFragment articleFrag;
//            try {
//                articleFrag = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
//            } catch (Exception l) {
//                articleFrag = null;
//            }
//            if (articleFrag != null) {
//                twoPanel = true;
//                // If article frag is available, we're in two-pane layout...
//            }

            getSupportActionBar().setTitle("Pop movies");
//            Bundle bundle = new Bundle();
//            bundle.putString("url", currurl);
//            if (twoPanel) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.gr_fragment, articleFrag).commit();
//            } else {
//                GridFragment fragInfo = new GridFragment();
//                fragInfo.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, fragInfo).commit();
//            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Boolean twoPanel = false;
        GridFragment articleFrag;
        try {
            articleFrag = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.gr_fragment);
        } catch (Exception l) {
            articleFrag = null;
        }
        if (articleFrag != null) {
            twoPanel = true;
            // If article frag is available, we're in two-pane layout...
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSupportActionBar().setTitle("Pop movies");
            Bundle bundle = new Bundle();
            url1 = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            currurl = url1;
            bundle.putString("url", url1);
            if (twoPanel) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gr_fragment, articleFrag).commit();
            } else {
                GridFragment fragInfo = new GridFragment();
                fragInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragInfo).commit();
            }

        } else {
            getSupportActionBar().setTitle("Pop movies");
            Bundle bundle = new Bundle();
            url1 = "http://api.themoviedb.org/3/discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc";
            currurl = url1;
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putString("url1", url1);
    }

}
