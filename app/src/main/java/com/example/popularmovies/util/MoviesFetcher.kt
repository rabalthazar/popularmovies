package com.example.popularmovies.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.popularmovies.BuildConfig
import com.example.popularmovies.data.MoviesDbHelper
import com.example.popularmovies.model.Movie
import com.example.popularmovies.model.MovieList
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import com.example.popularmovies.model.List as ListModel

/**
 * Loads data from the TMDB API
 * Created by rafael on 23/05/16.
 */
class MoviesFetcher(context: Context) {

    enum class ListSelections constructor(private val selection: String) {
        MOST_POPULAR("popular"),
        TOP_RATED("top_rated");

        override fun toString(): String {
            return selection
        }

        companion object {

            operator fun contains(test: String): Boolean {
                for (selection in ListSelections.values()) {
                    if (test == selection.toString()) {
                        return true
                    }
                }
                return false
            }
        }
    }

    /**
     * Log tag for debugging purposes
     */
    private val LOG_TAG: String
        get() = MoviesFetcher::class.java.simpleName

    /**
     * TMDB base API URL
     */
    private val MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie"

    /**
     * TMDB API key parameter name
     */
    private val API_KEY_PARAM = "api_key"

    private val TMDB_RESULTS = "results"

    private val database = MoviesDbHelper.getDatabase(context)

    /**
     * Fetches the json movie list from the TMDB API and returns it a List class
     * @param selection The list from where to fetch
     */
    fun fetchMovies(selection: String) {
        if (!MoviesFetcher.ListSelections.contains(selection)) {
            return
        }

        val uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(selection)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()

        val jsonMovieListStr = UriFetcher.fetch(uri) ?: return
        val movies = parseMovieListJson(jsonMovieListStr)
        saveMovieList(selection, movies)
    }

    private fun parseMovieListJson(jsonStr: String): List<Movie> {
        val json: JSONObject
        return try {
            json = JSONObject(jsonStr)
            val resultsJson = json.getJSONArray(TMDB_RESULTS)
            val movies = mutableListOf<Movie>()
            (0 until resultsJson.length() - 1).forEach { i ->
                val movie = MovieFactory.fromTMDBJsonObject(resultsJson.getJSONObject(i))
                movies.add(movie)
            }
            movies
        } catch (e: JSONException) {
            Log.d(LOG_TAG, e.message, e)
            emptyList()
        }
    }

    private fun saveMovieList(selection: String, movies: List<Movie>) {
        val list = insertOrUpdateList(selection)
        for (movie in movies) {
            insertOrUpdateMovie(movie)
        }
        return fillMovieList(list, movies)
    }

    private fun insertOrUpdateList(selection: String): ListModel {
        val listDao = database.listDao()
        val list = listDao.findBySelection(selection) ?: ListModel()
        list.apply {
            this.selection = selection
            dateFetched = Date()
        }
        listDao.insert(list)
        return list
    }

    private fun insertOrUpdateMovie(movie: Movie) {
        val movieDao = database.movieDao()
        movieDao.insert(movie)
    }

    private fun fillMovieList(list: ListModel, movies: List<Movie>) {
        val movieListDao = database.movieListDao()
        val movieLists = movieListDao.findByListId(list.id)
        movieLists.forEach {
            movieListDao.delete(it)
        }
        movies.forEachIndexed {idx, movie ->
            MovieList().apply {
                listId = list.id
                movieId = movie.id
                order = idx
                movieListDao.insert(this)
            }
        }
    }
}
