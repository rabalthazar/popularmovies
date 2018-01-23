package com.example.popularmovies

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.popularmovies.data.MoviesContract
import com.example.popularmovies.util.ImageLoader
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import java.text.NumberFormat
import java.util.*

/**
 * Shows a movie details as original title, release date, movie overview, etc.
 */
class MovieDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(MOVIE_LOADER, null, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        val intent = activity?.intent
        val movieByIdUri = intent?.data
        if (activity != null && movieByIdUri != null) {
            return CursorLoader(activity!!, movieByIdUri, MOVIE_COLUMNS, null, null, null)
        }
        return null
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        if (!data.moveToFirst()) {
            return
        }
        val context = context ?: return
        ImageLoader.loadFromPosterPath(context, data.getString(COL_MOVIE_POSTER_PATH), moviePoster)
        movieTitle.text = data.getString(COL_MOVIE_TITLE)
        movieOverview.text = data.getString(COL_MOVIE_OVERVIEW)
        val dateFormat = android.text.format.DateFormat.getDateFormat(context)
        movieRelease.text = dateFormat.format(Date(data.getLong(COL_MOVIE_RELEASE_DATE)))
        movieRating.text = NumberFormat.getNumberInstance().format(data.getDouble(COL_MOVIE_VOTE_AVG))
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // Method is not necessary. Just here for interface implementation completeness
    }

    companion object {
        private const val MOVIE_LOADER = 0

        val MOVIE_COLUMNS = arrayOf(
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_TITLE,
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_OVERVIEW,
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_VOTE_AVG,
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry.COLUMN_ADULT
        )

        const val COL_MOVIE_ID = 0
        const val COL_MOVIE_TITLE = 1
        const val COL_MOVIE_OVERVIEW = 2
        const val COL_MOVIE_RELEASE_DATE = 3
        const val COL_MOVIE_VOTE_AVG = 4
        const val COL_MOVIE_POSTER_PATH = 5
        const val COL_MOVIE_ADULT = 6
    }
}
