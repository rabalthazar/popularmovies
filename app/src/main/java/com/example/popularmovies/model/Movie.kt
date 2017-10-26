package com.example.popularmovies.model

import java.util.Date

/**
 * Model class for a movie
 * Created by rafael on 23/05/16.
 */
data class Movie (
    /**
     * TMDB movie id
     */
    var id: Long,

    /**
     * Movie title
     */
    var title: String,

    /**
     * Movie release date
     */
    var releaseDate: Date,

    /**
     * Poster path in TMDB
     */
    var posterPath: String,

    /**
     * Movie overview
     */
    var overview: String,

    /**
     * Adult rated?
     */
    var adult: Boolean,

    /**
     * User rating
     */
    var voteAverage: Double
)
