package com.example.popularmovies.model

import java.util.ArrayList
import java.util.Date

/**
 * Holds a list of movies, given an determined order: most popular or top rated
 * Created by rafael on 23/06/16.
 */
data class List (
    /**
     * List order. Actually popular (@string/pref_order_popular) or top_rated (@string/pref_order_toprated)
     */
    var selection: String,

    /**
     * Date the list was fetched
     */
    var dateFetched: Date,

    /**
     * Movie list id
     */
    var id: Long? = null
) {
    /**
     * List of movies in the list
     */
    val movies = ArrayList<Movie>()

    fun getMovie(idx: Int): Movie? {
        return if (movies.size < idx)
            movies[idx]
        else
            null
    }

    fun add(movie: Movie): List {
        movies.add(movie)
        return this
    }

    fun remove(movie: Movie): List {
        movies.remove(movie)
        return this
    }

    fun clear(): List {
        movies.clear()
        return this
    }
}
