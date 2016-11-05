package com.example.popularmovies.util;

import android.net.Uri;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.model.Movie;

/**
 * Helper class to build movie posters' URI
 * Created by rafael on 16/06/16.
 */
public class MoviePosterUriBuilder {
    private static final String BASE_URI = "http://image.tmdb.org/t/p/";
    // TODO: use a dynamic image size according to the screen size
    private static final String IMG_SIZE = "w185";
    private static final String API_KEY_PARAM = "api_key";

    public static Uri from(Movie movie) {
        return from(movie.getPosterPath());
    }

    public static Uri from(String posterPath) {
        return Uri.parse(BASE_URI).buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(posterPath)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
    }
}
