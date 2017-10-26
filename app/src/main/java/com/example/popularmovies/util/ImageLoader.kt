package com.example.popularmovies.util

import android.content.Context
import android.widget.ImageView

import com.squareup.picasso.Picasso

/**
 * Simple class to load images using the Picasso library
 * Created by rafael on 17/06/16.
 */
object ImageLoader {
    fun loadFromPosterPath(context: Context, posterPath: String, imageView: ImageView) {
        Picasso.with(context)
                .load(MoviePosterUriBuilder.from(posterPath))
                .into(imageView)
    }
}
