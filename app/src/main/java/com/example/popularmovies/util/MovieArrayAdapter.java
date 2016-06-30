package com.example.popularmovies.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * An adapter to fill lists that display movie posters
 * Created by rafael on 16/06/16.
 */
public class MovieArrayAdapter extends BaseAdapter {

    /**
     * Resource ID for the item layout where to draw a movie poster
     */
    private int mItemResource;

    /**
     * Activity context
     */
    private Context mContext;

    /**
     * The list of movies
     */
    private ArrayList<Movie> mMovies;

    public MovieArrayAdapter(Context context, int resource, ArrayList<Movie> movies) {
        mContext = context;
        mItemResource = resource;
        mMovies = movies;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if ((convertView != null) && (convertView instanceof ImageView)) {
            imageView = (ImageView) convertView;
        } else {
            imageView = createFromLayoutResource(parent);
        }

        Context context = mContext != null ? mContext : parent.getContext();

        ImageLoader.loadFromMovie(context, getItem(position), imageView);

        return imageView;
    }

    /**
     * Creates an ImageView from the constructor given item resource
     * @param parent The ImageView parent
     * @return The new ImageView
     */
    private ImageView createFromLayoutResource(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ImageView view = (ImageView) inflater.inflate(mItemResource, parent, false);
        return view;
    }

    /**
     * Adds a movie to the adapter
     * @param movie
     */
    public void add(Movie movie) {
        synchronized (mMovies) {
            if (mMovies != null) {
                mMovies.add(movie);
            }
        }
    }

    /**
     * Clears the list of movies
     */
    public void clear() {
        synchronized (mMovies) {
            if (mMovies != null) {
                mMovies.clear();
            }
        }
    }
}
