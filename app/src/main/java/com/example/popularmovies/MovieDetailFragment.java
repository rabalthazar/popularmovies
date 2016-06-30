package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.util.ImageLoader;
import com.example.popularmovies.util.MovieFactory;

import java.text.DateFormat;

/**
 * Shows a movie details as original title, release date, movie overview, etc.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * Holds the movie data to be displayed
     */
    protected Movie mMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MovieGridFragment.EXTRA_MOVIE)) {
            // Loads movie data from Intent and assign its data to the UI elements
            Bundle movieBundle = intent.getBundleExtra(MovieGridFragment.EXTRA_MOVIE);
            mMovie = MovieFactory.fromBundle(movieBundle);
            ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
            ImageLoader.loadFromMovie(getContext(), mMovie, moviePoster);
            TextView movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
            movieTitle.setText(mMovie.getTitle());
            TextView movieOverview = (TextView) rootView.findViewById(R.id.movie_overview);
            movieOverview.setText(mMovie.getOverview());
            TextView movieRelease = (TextView) rootView.findViewById(R.id.movie_release);
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            movieRelease.setText(dateFormat.format(mMovie.getReleaseDate()));
            TextView movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
            movieRating.setText(mMovie.getVoteAverage().toString());
        }

        return rootView;
    }
}
