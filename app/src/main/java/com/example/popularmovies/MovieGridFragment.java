package com.example.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.util.FetchMoviesTask;
import com.example.popularmovies.util.MovieArrayAdapter;
import com.example.popularmovies.util.MovieFactory;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment {
    public static final String EXTRA_MOVIE = "movie_intent_bundle";

    protected MovieArrayAdapter mMoviesAdapter;

    public MovieGridFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMoviesAdapter =
                new MovieArrayAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.grid_item_movie, // The name of the layout ID.
                        new ArrayList<Movie>());

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_movies);
        gridView.setAdapter(mMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra(EXTRA_MOVIE, MovieFactory.toBundle(movie));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        fillGrid();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            fillGrid();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillGrid() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String moviesOrder = prefs.getString(getString(R.string.pref_order_key),
                getString(R.string.pref_order_default));
        FetchMoviesTask task = new FetchMoviesTask();
        task.setAdapter(mMoviesAdapter);
        task.execute(moviesOrder);
    }
}
