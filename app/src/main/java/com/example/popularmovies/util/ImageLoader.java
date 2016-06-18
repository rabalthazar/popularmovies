package com.example.popularmovies.util;

import android.content.Context;
import android.widget.ImageView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by rafael on 17/06/16.
 */
public class ImageLoader {
    public static void loadFromMovie(Context context, Movie movie, ImageView imageView) {
        Picasso.with(context)
            .load(MoviePosterUriBuilder.from(movie))
            .into(imageView);
    }
}
