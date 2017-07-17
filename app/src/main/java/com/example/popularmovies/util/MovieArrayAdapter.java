package com.example.popularmovies.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.MovieGridFragment;
import com.example.popularmovies.R;

/**
 * An adapter to fill lists that display movie posters
 * Created by rafael on 16/06/16.
 */
public class MovieArrayAdapter extends CursorAdapter {
    public MovieArrayAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return createFromLayoutResource(parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view;
        ImageLoader.loadFromPosterPath(context, cursor.getString(MovieGridFragment.COL_MOVIE_POSTER_PATH), imageView);
    }

    /**
     * Creates an ImageView from the constructor given item resource
     * @param parent The ImageView parent
     * @return The new ImageView
     */
    private ImageView createFromLayoutResource(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ImageView view = (ImageView) inflater.inflate(R.layout.grid_item_movie, parent, false);
        return view;
    }
}
