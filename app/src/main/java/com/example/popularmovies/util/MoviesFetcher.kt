package com.example.popularmovies.util

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.net.Uri
import android.util.Log
import com.example.popularmovies.BuildConfig
import com.example.popularmovies.data.MoviesContract
import com.example.popularmovies.model.List
import com.example.popularmovies.model.Movie
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Loads data from the TMDB API
 * Created by rafael on 23/05/16.
 */
class MoviesFetcher(private val mContext: Context) {
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

    /**
     * Fetches the json movie list from the TMDB API and returns it a List class
     * @param selection The list from where to fetch
     */
    fun fetchMovies(selection: String) {
        if (!MoviesContract.ListSelections.contains(selection)) {
            return
        }

        val uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(selection)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()

        val jsonMovieListStr = UriFetcher.fetch(uri) ?: return
        val movies = List()
        if (parseMovieListJson(movies, jsonMovieListStr)!!) {
            movies.selection = selection
            addMovieList(movies)
        }
    }

    private fun parseMovieListJson(movies: List, jsonStr: String): Boolean? {
        val TMDB_RESULTS = "results"

        val json: JSONObject
        try {
            json = JSONObject(jsonStr)
            val resultsJson = json.getJSONArray(TMDB_RESULTS)
            (0 until resultsJson.length()).forEach { i ->
                val movie = MovieFactory.fromTMDBJsonObject(resultsJson.getJSONObject(i))
                movies.add(movie)
            }
        } catch (e: JSONException) {
            Log.d(LOG_TAG, e.message, e)
            return false
        }

        return true
    }

    private fun addMovieList(list: List): Int? {
        val listId = insertOrUpdateList(list)
        if (listId == null || listId <= 0) {
            return null
        }
        list.id = listId
        for (movie in list.movies) {
            insertOrUpdateMovie(movie)
        }
        return fillMovieList(list)
    }

    private fun insertOrUpdateList(list: List): Long? {
        val listUri = MoviesContract.ListEntry.buildBySelectionUri(list.selection)
        val listUriById: Uri?
        val listValues = ContentValues()
        listValues.put(MoviesContract.ListEntry.COLUMN_SELECTION, list.selection)
        listValues.put(MoviesContract.ListEntry.COLUMN_DATE_FETCHED, list.dateFetched.time)
        val listCursor = mContext.contentResolver.query(listUri, null, null, null, null)
        if (listCursor == null || !listCursor.moveToFirst()) {
            return try {
                listUriById = mContext.contentResolver.insert(MoviesContract.ListEntry.CONTENT_URI, listValues)
                listCursor?.close()
                MoviesContract.ListEntry.getIdFromUri(listUriById)
            } catch (exception: SQLException) {
                Log.d(LOG_TAG, "Failed to insert list into database")
                -1L
            }

        }
        val listIdCol = listCursor.getColumnIndex(MoviesContract.ListEntry._ID)
        val listId = listCursor.getLong(listIdCol)
        listCursor.close()
        listUriById = MoviesContract.ListEntry.buildUri(listId)
        if (mContext.contentResolver.update(listUriById!!, listValues, null, null) <= 0) {
            Log.d(LOG_TAG, "Failed to update list into database")
        }
        return listId
    }

    private fun insertOrUpdateMovie(movie: Movie): Long? {
        val movieUri = MoviesContract.MovieEntry.buildUri(movie.id)
        val movieValues = ContentValues()
        movieValues.put(MoviesContract.MovieEntry._ID, movie.id)
        movieValues.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.title)
        movieValues.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.overview)
        movieValues.put(MoviesContract.MovieEntry.COLUMN_ADULT, movie.adult)
        movieValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate?.time)
        movieValues.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, movie.posterPath)
        movieValues.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVG, movie.voteAverage)
        val movieCursor = mContext.contentResolver.query(movieUri, null, null, null, null)
        if (movieCursor == null || !movieCursor.moveToFirst()) {
            movieCursor?.close()
            return try {
                val newMovieUri = mContext.contentResolver.insert(
                        MoviesContract.MovieEntry.CONTENT_URI,
                        movieValues
                )
                MoviesContract.MovieEntry.getIdFromUri(newMovieUri)
            } catch (exception: SQLException) {
                Log.d(LOG_TAG, "Failed to insert movie into database")
                -1L
            }

        }
        movieCursor.close()
        if (mContext.contentResolver.update(movieUri, movieValues, null, null) <= 0) {
            Log.d(LOG_TAG, "Failed to update movie into database")
        }
        return movie.id
    }

    private fun fillMovieList(list: List): Int {
        val movieListBySelectionUri = MoviesContract.MovieListEntry.buildBySelectionUri(list.selection)
        var movieListOrder = 1
        val movieListValuesList = ArrayList<ContentValues>()
        for ((id) in list.movies) {
            val movieListValues = ContentValues()
            movieListValues.put(MoviesContract.MovieListEntry.COLUMN_LIST_KEY, list.id)
            movieListValues.put(MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY, id)
            movieListValues.put(MoviesContract.MovieListEntry.COLUMN_ORDER, movieListOrder++)
            movieListValuesList.add(movieListValues)
        }
        val movieListValuesArray = movieListValuesList.toTypedArray()
        return mContext.contentResolver.bulkInsert(movieListBySelectionUri, movieListValuesArray)
    }
}
