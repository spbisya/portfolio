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

/**
 * Created by gwa on 3/19/16.
 */
public class Spotify extends AppCompatActivity implements GridFragment.OnFragmentSelectedListener {

    /**
     * Called when the activity is first created.
     */
    Menu menui;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);
        getSupportActionBar().setTitle("Pop movies");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            GridFragment firstFragment = new GridFragment();
            Bundle bundle = new Bundle();
            String myMessage = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            bundle.putString("url", myMessage);
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(bundle);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, firstFragment).commit();
        }
    }


    @Override
    public void onMovieSelected(int position, String id) {
        getSupportActionBar().setTitle("Movie details");
        //  MovieFragment articleFrag = (MovieFragment)
        //      getSupportFragmentManager().findFragmentById(R.id.container);

        ///  if (articleFrag != null) {
        // If article frag is available, we're in two-pane layout...

        // Call a method in the ArticleFragment to update its content
        //   articleFrag.updateArticleView(position);
        //  } else {
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
        //   }
    }

    @Override
    public void onBackPressed() {
        if (getSupportActionBar().getTitle().equals("Movie details")) {
            getSupportActionBar().setTitle("Pop movies");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSupportActionBar().setTitle("Pop movies");
            Bundle bundle = new Bundle();
            String myMessage = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            bundle.putString("url", myMessage);
            GridFragment fragInfo = new GridFragment();
            fragInfo.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragInfo).commit();

        } else {
            getSupportActionBar().setTitle("Pop movies");
            Bundle bundle = new Bundle();
            String myMessage = "http://api.themoviedb.org/3/discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc";
            bundle.putString("url", myMessage);
            GridFragment fragInfo = new GridFragment();
            fragInfo.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragInfo).commit();
        }
        return super.onOptionsItemSelected(item);
    }


}
