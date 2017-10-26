package com.example.popularmovies.util;

import android.os.Bundle;

import com.example.popularmovies.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Helper Factory class to convert movies to and from the various formats used in the app
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

    /**
     * Creates a Movie object from a JSON in the TMDB format
     * @param movieJson Movie JSON object
     * @return Movie object
     * @throws JSONException if JSON can't be parsed
     */
    public static Movie fromTMDBJsonObject(JSONObject movieJson) throws JSONException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date releaseDate;
        try {
            releaseDate = format.parse(movieJson.getString(MOVIE_RELEASE));
        } catch (ParseException e) {
            releaseDate = null;
        }
        return new Movie(
                movieJson.getLong(MOVIE_ID),
                movieJson.getString(MOVIE_TITLE),
                releaseDate,
                movieJson.getString(MOVIE_POSTER),
                movieJson.getString(MOVIE_OVERVIEW),
                movieJson.getBoolean(MOVIE_ADULT),
                movieJson.getDouble(MOVIE_RATING)
        );
    }

    /**
     * Creates a Bundle with a movie data that can be put into Intents
     * @param movie Movie object
     * @return Bundle object with Movie data
     */
    public static Bundle toBundle(Movie movie) {
        Bundle movieBundle = new Bundle();
        movieBundle.putLong(MOVIE_ID, movie.getId());
        movieBundle.putString(MOVIE_TITLE, movie.getTitle());
        movieBundle.putString(MOVIE_OVERVIEW, movie.getOverview());
        movieBundle.putLong(MOVIE_RELEASE, movie.getReleaseDate().getTime());
        movieBundle.putBoolean(MOVIE_ADULT, movie.getAdult());
        movieBundle.putString(MOVIE_POSTER, movie.getPosterPath());
        movieBundle.putDouble(MOVIE_RATING, movie.getVoteAverage());
        return movieBundle;
    }

    /**
     * Creates a Movie object from a movie Bundle
     * @param movieBundle The movie Bundle
     * @return A Movie object with the Bundle data
     */
    public static Movie fromBundle(Bundle movieBundle) {
        return new Movie(
                movieBundle.getLong(MOVIE_ID),
                movieBundle.getString(MOVIE_TITLE),
                new Date(movieBundle.getLong(MOVIE_RELEASE)),
                movieBundle.getString(MOVIE_POSTER),
                movieBundle.getString(MOVIE_OVERVIEW),
                movieBundle.getBoolean(MOVIE_ADULT),
                movieBundle.getDouble(MOVIE_RATING));
    }
}
