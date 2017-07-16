package com.example.popularmovies.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.popularmovies.util.FetchMoviesTask;
import com.example.popularmovies.util.Utilities;

import java.util.Date;

public class MoviesLoader extends AsyncTaskLoader<Cursor> {
    private static final Long FETCH_THRESHOLD = 3600000L;

    private Cursor mMoviesCursor;

    public static final String[] MOVIE_COLUMNS = new String[] {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_VOTE_AVG,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_ADULT
    };

    private Boolean mForceFetch = false;

    public MoviesLoader(Context context, Boolean forceFetch) {
        super(context);
        mForceFetch = forceFetch;
    }

    @Override
    public Cursor loadInBackground() {
        final String moviesOrder = Utilities.getPreferredSelection(getContext());
        if (needsFetching(moviesOrder)) {
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getContext());
            fetchMoviesTask.fetchMovies(moviesOrder);
        }
        final Uri moviesUri = MoviesContract.MovieEntry.buildBySelectionUri(moviesOrder);
        return getContext().getContentResolver().query(moviesUri, MOVIE_COLUMNS, null, null, null);
    }

    private Boolean needsFetching(String moviesOrder) {
        if (mForceFetch) {
            return true;
        }
        final Uri listUri = MoviesContract.ListEntry.buildBySelectionUri(moviesOrder);
        Cursor listCursor = getContext().getContentResolver().query(listUri, null, null, null, null);
        if (!listCursor.moveToFirst()) {
            listCursor.close();
            return true;
        }
        final Integer dateFetchedColumn = listCursor.getColumnIndex(MoviesContract.ListEntry.COLUMN_DATE_FETCHED);
        final Long dateFetched = listCursor.getLong(dateFetchedColumn);
        final Long now = new Date().getTime();
        if (now - dateFetched > FETCH_THRESHOLD) {
            listCursor.close();
            return true;
        }
        listCursor.close();
        return false;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (cursor != null) {
                onReleaseResources(cursor);
            }
        }
        Cursor oldCursor = mMoviesCursor;
        mMoviesCursor = cursor;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(cursor);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldCursor != null) {
            onReleaseResources(oldCursor);
        }
    }

    private void onReleaseResources(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    protected void finalize() {
        onReleaseResources(mMoviesCursor);
    }
}
