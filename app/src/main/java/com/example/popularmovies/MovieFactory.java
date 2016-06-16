package com.example.popularmovies;

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
    public static Movie fromTMDBJsonObject(JSONObject movieJson) throws JSONException {
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_MOVIE_TITLE = "title";
        final String TMDB_MOVIE_OVERVIEW = "overview";
        final String TMDB_MOVIE_RELEASE = "release_date";
        final String TMDB_MOVIE_ADULT = "adult";
        final String TMDB_MOVIE_POSTER = "poster_path";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate;
        try {
            releaseDate = format.parse(movieJson.getString(TMDB_MOVIE_RELEASE));
        } catch (ParseException e) {
            releaseDate = null;
        }
        return new Movie()
                .setId(movieJson.getLong(TMDB_MOVIE_ID))
                .setTitle(movieJson.getString(TMDB_MOVIE_TITLE))
                .setOverview(movieJson.getString(TMDB_MOVIE_OVERVIEW))
                .setReleaseDate(releaseDate)
                .setAdult(movieJson.getBoolean(TMDB_MOVIE_ADULT))
                .setPosterPath(movieJson.getString(TMDB_MOVIE_POSTER));
    }
}
