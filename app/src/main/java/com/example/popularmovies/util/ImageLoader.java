package com.example.popularmovies.util;

import android.content.Context;
import android.widget.ImageView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Simple class to load images using the Picasso library
 * Created by rafael on 17/06/16.
 */
public class ImageLoader {
    /**
     * Loads a movie poster into an ImageView
     * @param context The activity context
     * @param movie The movie instance
     * @param imageView The ImageView to load image into
     */
    public static void loadFromMovie(Context context, Movie movie, ImageView imageView) {
        loadFromPosterPath(context, movie.getPosterPath(), imageView);
    }

    public static void loadFromPosterPath(Context context, String posterPath, ImageView imageView) {
        Picasso.with(context)
                .load(MoviePosterUriBuilder.from(posterPath))
                .into(imageView);
    }
}
