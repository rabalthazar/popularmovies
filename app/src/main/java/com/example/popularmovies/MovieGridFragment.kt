package com.example.popularmovies

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.popularmovies.data.MoviesContract
import com.example.popularmovies.data.MoviesLoader
import com.example.popularmovies.util.MovieArrayAdapter
import kotlinx.android.synthetic.main.fragment_movie_grid.*

/**
 * Displays a grid with a list of movie posters
 */
class MovieGridFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The adapter that feeds the movie grid
     */
    private var mMoviesAdapter: MovieArrayAdapter? = null

    private var mForceFetch: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(MOVIES_LOADER, null, this).forceLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mMoviesAdapter = MovieArrayAdapter(activity!!, null, 0)

        return inflater.inflate(R.layout.fragment_movie_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get a reference to the GridView, and attach this adapter to it.
        moviesGrid.adapter = mMoviesAdapter
        moviesGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val cursor = mMoviesAdapter?.getItem(position) as Cursor?
            if (cursor != null) {
                val movieUri = MoviesContract.MovieEntry.buildUri(cursor.getLong(COL_MOVIE_ID))
                val intent = Intent(activity, MovieDetailActivity::class.java)
                        .setData(movieUri)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movies_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                mForceFetch = true
                onSelectionChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onSelectionChanged() {
        loaderManager.restartLoader(MOVIES_LOADER, null, this).forceLoad()
        mForceFetch = false
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Log.d(FRAGMENT_TAG, "CreateLoader")
        return MoviesLoader(context!!, mForceFetch)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mMoviesAdapter?.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mMoviesAdapter?.swapCursor(null)
    }

    companion object {
        val FRAGMENT_TAG: String
            get() = MovieGridFragment::class.java.simpleName

        private const val MOVIES_LOADER = 0

        const val COL_MOVIE_ID = 0
        const val COL_MOVIE_TITLE = 1
        const val COL_MOVIE_OVERVIEW = 2
        const val COL_MOVIE_RELEASE_DATE = 3
        const val COL_MOVIE_VOTE_AVG = 4
        const val COL_MOVIE_POSTER_PATH = 5
        const val COL_MOVIE_ADULT = 6
    }
}
