package com.example.popularmovies.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.popularmovies.databinding.GridItemMovieBinding
import com.example.popularmovies.model.Movie

/**
 * An adapter to fill lists that display movie posters
 * Created by rafael on 16/06/16.
 */
class MovieArrayAdapter(context: Context, resource: Int) : ArrayAdapter<Movie>(context, resource) {
    private lateinit var binding: GridItemMovieBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView = if (convertView is ImageView) convertView else createFromLayoutResource(parent)
        val movie = this.getItem(position)
        movie?.posterPath?.let {
            ImageLoader.loadFromPosterPath(it, imageView)
        }
        return imageView
    }

    /**
     * Creates an ImageView from the constructor given item resource
     * @param parent The ImageView parent
     * @return The new ImageView
     */
    private fun createFromLayoutResource(parent: ViewGroup): ImageView {
        val inflater = LayoutInflater.from(parent.context)
        binding = GridItemMovieBinding.inflate(inflater, parent, false)
        return binding.gridPosterImageView
    }
}
