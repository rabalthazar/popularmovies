package com.example.popularmovies.util;

import android.net.Uri;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.model.Movie;

/**
 * Helper class to build movie posters' URI
 * Created by rafael on 16/06/16.
 */
public class MoviePosterUriBuilder {
    public static Uri from(Movie movie) {
        final String BASE_URI = "http://image.tmdb.org/t/p/";
        // TODO: use a dynamic image size according to the screen size
        final String IMG_SIZE = "w185";
        final String API_KEY_PARAM = "api_key";

        return Uri.parse(BASE_URI).buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(movie.getPosterPath())
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
    }
}
