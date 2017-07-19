package com.example.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.util.ImageLoader;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Shows a movie details as original title, release date, movie overview, etc.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MOVIE_LOADER = 0;

    public static final String[] MOVIE_COLUMNS = new String[] {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_VOTE_AVG,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_ADULT
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_OVERVIEW = 2;
    public static final int COL_MOVIE_RELEASE_DATE = 3;
    public static final int COL_MOVIE_VOTE_AVG = 4;
    public static final int COL_MOVIE_POSTER_PATH = 5;
    public static final int COL_MOVIE_ADULT = 6;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        Uri movieByIdUri = intent.getData();
        return new CursorLoader(getActivity(), movieByIdUri, MOVIE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }
        View view = getView();
        if (view == null) {
            return;
        }
        ImageView moviePoster = view.findViewById(R.id.movie_poster);
        ImageLoader.loadFromPosterPath(getContext(), data.getString(COL_MOVIE_POSTER_PATH), moviePoster);
        TextView movieTitle = view.findViewById(R.id.movie_title);
        movieTitle.setText(data.getString(COL_MOVIE_TITLE));
        TextView movieOverview = view.findViewById(R.id.movie_overview);
        movieOverview.setText(data.getString(COL_MOVIE_OVERVIEW));
        TextView movieRelease = view.findViewById(R.id.movie_release);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        movieRelease.setText(dateFormat.format(new Date(data.getLong(COL_MOVIE_RELEASE_DATE))));
        TextView movieRating = view.findViewById(R.id.movie_rating);
        movieRating.setText(NumberFormat.getNumberInstance().format(data.getDouble(COL_MOVIE_VOTE_AVG)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Method is not necessary. Just here for interface implementation completeness
    }
}
