package com.example.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesLoader;
import com.example.popularmovies.util.MovieArrayAdapter;

/**
 * Displays a grid with a list of movie posters
 */
public class MovieGridFragment extends Fragment {
    public static String FRAGMENT_TAG = MovieGridFragment.class.getSimpleName();

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_OVERVIEW = 2;
    public static final int COL_MOVIE_RELEASE_DATE = 3;
    public static final int COL_MOVIE_VOTE_AVG = 4;
    public static final int COL_MOVIE_POSTER_PATH = 5;
    public static final int COL_MOVIE_ADULT = 6;

    /**
     * The adapter that feeds the movie grid
     */
    protected MovieArrayAdapter mMoviesAdapter;

    protected MoviesLoader mMoviesLoader;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoviesLoader = new MoviesLoader(getActivity(), mMoviesAdapter);
        getLoaderManager().initLoader(MoviesLoader.MOVIES_LOADER, null, mMoviesLoader);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMoviesAdapter = new MovieArrayAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_movies);
        gridView.setAdapter(mMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) mMoviesAdapter.getItem(position);
                if (cursor != null) {
                    Uri movieUri = MoviesContract.MovieEntry.buildUri(cursor.getLong(COL_MOVIE_ID));
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                            .setData(movieUri);
                    startActivity(intent);
                }
            }
        });

        return rootView;
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
            getLoaderManager().restartLoader(MoviesLoader.MOVIES_LOADER, null, mMoviesLoader);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
