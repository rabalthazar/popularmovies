package com.example.popularmovies.util

import android.content.Context
import com.example.popularmovies.BuildConfig
import com.example.popularmovies.data.MoviesDbHelper
import com.example.popularmovies.model.Movie
import com.example.popularmovies.model.MovieList
import com.example.popularmovies.service.TheMovieDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            operator fun contains(test: String): Boolean =
                    ListSelections.values().map { it.toString() }.contains(test)
        }
    }

    private val database = MoviesDbHelper.getDatabase(context)

    /**
     * Fetches the json movie list from the TMDB API and returns it a List class
     * @param selection The list from where to fetch
     */
    fun fetchMovies(selection: String) {
        if (!MoviesFetcher.ListSelections.contains(selection)) {
            return
        }

        val service = this.getService()

        val call = service.getMovies(selection, BuildConfig.THE_MOVIE_DB_API_KEY)
        val response = call.execute()
        val movies = response.body() ?: return

        saveMovieList(selection, movies.results)
    }

    private fun saveMovieList(selection: String, movies: List<Movie>) {
        val list = insertOrUpdateList(selection)
        movies.forEach {
            insertOrUpdateMovie(it)
        }
        return fillMovieList(list, movies).also {
            clearOrphanMovies()
        }
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
                id = movieListDao.insert(this)
            }
        }
    }

    private fun clearOrphanMovies() {
        val movieDao = database.movieDao()
        val orphans = movieDao.findOrphans()
        orphans.forEach {
            movieDao.delete(it)
        }
    }

    private fun getService(): TheMovieDBService {
        val retrofit = Retrofit.Builder()
                .baseUrl(MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(TheMovieDBService::class.java)
    }

    companion object {
        /**
         * TMDB base API URL
         */
        private const val MOVIES_BASE_URL = "http://api.themoviedb.org/3/"
    }
}
