package com.example.popularmovies.util

import android.net.Uri

import com.example.popularmovies.BuildConfig

/**
 * Helper class to build movie posters' URI
 * Created by rafael on 16/06/16.
 */
object MoviePosterUriBuilder {
    private const val BASE_URI = "http://image.tmdb.org/t/p/"

    // TODO: use a dynamic image size according to the screen size
    private const val IMG_SIZE  = "w185"

    private const val API_KEY_PARAM = "api_key"

    fun from(posterPath: String): Uri {
        return Uri.parse(BASE_URI).buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(posterPath)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build()
    }
}
