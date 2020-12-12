package com.example.popularmovies.data

import android.app.Application
import com.example.popularmovies.model.Movie
import com.example.popularmovies.util.MoviesFetcher
import com.example.popularmovies.util.Utilities
import java.util.*

class MoviesLoader(
        private val application: Application,
        private var forceFetch: Boolean,
) {

    private val database = MoviesDbHelper.getDatabase(application)

    fun doLoad(): List<Movie> {
        val moviesOrder = Utilities.getPreferredSelection(application)
        if (needsFetching(moviesOrder)) {
            val moviesFetcher = MoviesFetcher(application)
            moviesFetcher.fetchMovies(moviesOrder)
        }
        val listDao = database.listDao()
        val movieDao = database.movieDao()
        val movieListDao = database.movieListDao()
        val list = listDao.findBySelection(moviesOrder) ?: return emptyList()
        val movieLists = movieListDao.findByListId(list.id)
        val movieIds = movieLists.map { it.movieId }
        return movieDao.loadByIds(movieIds.toLongArray())
    }

    private fun needsFetching(moviesOrder: String): Boolean {
        if (forceFetch) {
            return true
        }
        val dateFetched  = database.listDao().findBySelection(moviesOrder)?.dateFetched?.time ?: return true
        val now = Date().time
        return now - dateFetched > FETCH_THRESHOLD
    }

    companion object {
        private const val FETCH_THRESHOLD = 3600000L
    }
}
