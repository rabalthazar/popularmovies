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
 * Created by rafael on 16/06/16.
 */
public class MovieArrayAdapter extends BaseAdapter {

    /**
     * Resource ID for the item layout where an item is going to be drawn
     */
    private int mItemResource;

    private Context mContext;

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

        Picasso.with(context)
                .load(MoviePosterUriBuilder.from(getItem(position)))
                .into(imageView);

        return imageView;
    }

    private ImageView createFromLayoutResource(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ImageView view = (ImageView) inflater.inflate(mItemResource, parent, false);
        return view;
    }

    public void add(Movie movie) {
        synchronized (mMovies) {
            if (mMovies != null) {
                mMovies.add(movie);
            }
        }
    }

    public void clear() {
        synchronized (mMovies) {
            if (mMovies != null) {
                mMovies.clear();
            }
        }
    }
}
