package com.example.popularmovies.util

import android.net.Uri

import com.example.popularmovies.BuildConfig

/**
 * Helper class to build movie posters' URI
 * Created by rafael on 16/06/16.
 */
object MoviePosterUriBuilder {
    private val BASE_URI
        get() = "http://image.tmdb.org/t/p/"
    // TODO: use a dynamic image size according to the screen size
    private val IMG_SIZE
        get() = "w185"
    private val API_KEY_PARAM
        get() = "api_key"

    fun from(posterPath: String): Uri {
        return Uri.parse(BASE_URI).buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(posterPath)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
    }
}
