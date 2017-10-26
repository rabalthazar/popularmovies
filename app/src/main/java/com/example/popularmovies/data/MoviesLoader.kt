package com.example.popularmovies.data

import android.content.Context
import android.database.Cursor
import android.support.v4.content.AsyncTaskLoader

import com.example.popularmovies.util.MoviesFetcher
import com.example.popularmovies.util.Utilities

import java.util.Date

class MoviesLoader(context: Context, private var forceFetch: Boolean) : AsyncTaskLoader<Cursor>(context) {

    private var mMoviesCursor: Cursor? = null

    override fun loadInBackground(): Cursor {
        val moviesOrder = Utilities.getPreferredSelection(context)
        if (needsFetching(moviesOrder)) {
            val moviesFetcher = MoviesFetcher(context)
            moviesFetcher.fetchMovies(moviesOrder)
        }
        val moviesUri = MoviesContract.MovieEntry.buildBySelectionUri(moviesOrder)
        return context.contentResolver.query(moviesUri, MOVIE_COLUMNS, null, null, null)
    }

    private fun needsFetching(moviesOrder: String): Boolean {
        if (forceFetch) {
            return true
        }
        val listUri = MoviesContract.ListEntry.buildBySelectionUri(moviesOrder)
        val listCursor = context.contentResolver.query(listUri, null, null, null, null) ?: return true
        if (!listCursor.moveToFirst()) {
            listCursor.close()
            return true
        }
        val dateFetchedColumn = listCursor.getColumnIndex(MoviesContract.ListEntry.COLUMN_DATE_FETCHED)
        val dateFetched = listCursor.getLong(dateFetchedColumn)
        val now = Date().time
        if (now - dateFetched > FETCH_THRESHOLD) {
            listCursor.close()
            return true
        }
        listCursor.close()
        return false
    }

    override fun deliverResult(cursor: Cursor) {
        if (isReset) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (cursor != null) {
                onReleaseResources(cursor)
            }
        }
        var oldCursor: Cursor? = mMoviesCursor
        mMoviesCursor = cursor

        if (isStarted) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(cursor)
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldCursor != null) {
            onReleaseResources(oldCursor)
        }
    }

    private fun onReleaseResources(cursor: Cursor) {
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
    }

    companion object {
        private const val FETCH_THRESHOLD = 3600000L

        val MOVIE_COLUMNS: Array<String>
            get() = arrayOf(
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_TITLE,
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_OVERVIEW,
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_VOTE_AVG,
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_ADULT
            )
    }
}
