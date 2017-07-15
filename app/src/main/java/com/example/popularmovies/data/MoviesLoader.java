package com.example.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.popularmovies.util.MovieArrayAdapter;
import com.example.popularmovies.util.Utilities;

public class MoviesLoader  implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final Integer MOVIES_LOADER = 0;

    public static final String[] MOVIE_COLUMNS = new String[] {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_VOTE_AVG,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_ADULT
    };

    /**
     * The adapter that feeds the movie grid
     */
    protected MovieArrayAdapter mMoviesAdapter;

    protected Context mContext;

    public MoviesLoader(Context context, MovieArrayAdapter moviesAdapter) {
        mContext = context;
        mMoviesAdapter = moviesAdapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String moviesOrder = Utilities.getPreferredSelection(mContext);
        Uri moviesUri = MoviesContract.MovieEntry.buildBySelectionUri(moviesOrder);
        return new CursorLoader(mContext, moviesUri, MOVIE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
