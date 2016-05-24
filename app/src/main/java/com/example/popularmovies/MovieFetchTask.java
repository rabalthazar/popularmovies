package com.example.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 23/05/16.
 */
public class MovieFetchTask {
    final private String LOG_TAG = MovieFetchTask.class.getSimpleName();

    final private String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    final private String MOVIES_POPULAR_PATH = "popular";
    final private String API_KEY_PARAM = "api_key";

    public Movie[] fetchByPopularity() {
        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(MOVIES_POPULAR_PATH)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        String jsonMovieListStr = FetchUri.fetch(uri);
        if (jsonMovieListStr == null) {
            return null;
        }
        return parseMovieListJson(jsonMovieListStr);
    }

    private Movie[] parseMovieListJson(String jsonStr) {
        final String TMDB_RESULTS = "results";

        JSONObject json;
        List<Movie> movies = new ArrayList<>();
        try {
            json = new JSONObject(jsonStr);
            JSONArray resultsJson = json.getJSONArray(TMDB_RESULTS);
            for(int i = 0; i < resultsJson.length() ; i++) {
                Movie movie = MovieFactory.fromTMDBJsonObject(resultsJson.getJSONObject(i));
                movies.add(movie);

            }
        } catch (JSONException e) {
            Log.v(LOG_TAG, e.getMessage(), e);
            return null;
        }
        return movies.toArray(new Movie[0]);
    }
}
