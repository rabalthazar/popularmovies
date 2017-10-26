package com.example.popularmovies.util

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.popularmovies.MovieGridFragment
import com.example.popularmovies.R

/**
 * An adapter to fill lists that display movie posters
 * Created by rafael on 16/06/16.
 */
class MovieArrayAdapter(context: Context, c: Cursor?, flags: Int) : CursorAdapter(context, c, flags) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return createFromLayoutResource(parent)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val imageView = view as ImageView
        ImageLoader.loadFromPosterPath(context, cursor.getString(MovieGridFragment.COL_MOVIE_POSTER_PATH), imageView)
    }

    /**
     * Creates an ImageView from the constructor given item resource
     * @param parent The ImageView parent
     * @return The new ImageView
     */
    private fun createFromLayoutResource(parent: ViewGroup): ImageView {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.grid_item_movie, parent, false) as ImageView
    }
}
