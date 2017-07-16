package com.example.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.popularmovies.util.FetchMoviesTask;
import com.example.popularmovies.util.Utilities;

public class MoviesLoader extends AsyncTaskLoader<Cursor> {
    public static final String[] MOVIE_COLUMNS = new String[] {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_VOTE_AVG,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_ADULT
    };

    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        final String moviesOrder = Utilities.getPreferredSelection(getContext());
        final Uri moviesUri = MoviesContract.MovieEntry.buildBySelectionUri(moviesOrder);
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getContext());
        fetchMoviesTask.fetchMovies(moviesOrder);
        Log.d("MoviesLoader", moviesUri.toString());
        return getContext().getContentResolver().query(moviesUri, MOVIE_COLUMNS, null, null, null);
    }
}
