package com.example.popularmovies.util;

import android.os.Bundle;

import com.example.popularmovies.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rafael on 23/05/16.
 */
public class MovieFactory {
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RELEASE = "release_date";
    public static final String MOVIE_ADULT = "adult";
    public static final String MOVIE_POSTER = "poster_path";
    public static final String MOVIE_RATING = "vote_average";

    public static Movie fromTMDBJsonObject(JSONObject movieJson) throws JSONException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate;
        try {
            releaseDate = format.parse(movieJson.getString(MOVIE_RELEASE));
        } catch (ParseException e) {
            releaseDate = null;
        }
        return new Movie()
                .setId(movieJson.getLong(MOVIE_ID))
                .setTitle(movieJson.getString(MOVIE_TITLE))
                .setOverview(movieJson.getString(MOVIE_OVERVIEW))
                .setReleaseDate(releaseDate)
                .setAdult(movieJson.getBoolean(MOVIE_ADULT))
                .setPosterPath(movieJson.getString(MOVIE_POSTER))
                .setVoteAverage(movieJson.getDouble(MOVIE_RATING));
    }

    public static Bundle toBundle(Movie movie) {
        Bundle movieBundle = new Bundle();
        movieBundle.putLong(MOVIE_ID, movie.getId());
        movieBundle.putString(MOVIE_TITLE, movie.getTitle());
        movieBundle.putString(MOVIE_OVERVIEW, movie.getOverview());
        movieBundle.putLong(MOVIE_RELEASE, movie.getReleaseDate().getTime());
        movieBundle.putBoolean(MOVIE_ADULT, movie.isAdult());
        movieBundle.putString(MOVIE_POSTER, movie.getPosterPath());
        movieBundle.putDouble(MOVIE_RATING, movie.getVoteAverage());
        return movieBundle;
    }

    public static Movie fromBundle(Bundle movieBundle) {
        return new Movie()
                .setId(movieBundle.getLong(MOVIE_ID))
                .setTitle(movieBundle.getString(MOVIE_TITLE))
                .setOverview(movieBundle.getString(MOVIE_OVERVIEW))
                .setReleaseDate(new Date(movieBundle.getLong(MOVIE_RELEASE)))
                .setAdult(movieBundle.getBoolean(MOVIE_ADULT))
                .setPosterPath(movieBundle.getString(MOVIE_POSTER))
                .setVoteAverage(movieBundle.getDouble(MOVIE_RATING));
    }
}
