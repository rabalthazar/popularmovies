package com.example.popularmovies.util

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Simple class to load images using the Picasso library
 * Created by rafael on 17/06/16.
 */
object ImageLoader {
    fun loadFromPosterPath(posterPath: String, imageView: ImageView) {
        Picasso
                .get()
                .load(MoviePosterUriBuilder.from(posterPath))
                .into(imageView)
    }
}
