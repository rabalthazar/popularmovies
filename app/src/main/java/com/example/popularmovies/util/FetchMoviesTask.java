package com.example.popularmovies.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rafael on 23/05/16.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, MovieList> {
    final private String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    MovieArrayAdapter mAdapter;

    final private String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    final private String API_KEY_PARAM = "api_key";

    @Override
    protected MovieList doInBackground(String... params) {
        // The order param is required
        if (params.length == 0) {
            return null;
        }

        return fetchMovies(params[0]);
    }

    @Override
    protected void onPostExecute(MovieList movies) {
        if (mAdapter == null || movies == null) {
            return;
        }
        mAdapter.clear();
        for(Movie movie: movies.getMovies()) {
            mAdapter.add(movie);
        }
        mAdapter.notifyDataSetChanged();
    }

    public FetchMoviesTask setAdapter(MovieArrayAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    @Nullable
    private MovieList fetchMovies(String order) {
        if (!order.equals("popular") && !order.equals("top_rated")) {
            return null;
        }

        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(order)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        String jsonMovieListStr = FetchUri.fetch(uri);
        if (jsonMovieListStr == null) {
            return null;
        }
        MovieList movies = parseMovieListJson(jsonMovieListStr);
        if (movies != null) {
            movies.setDateFetched(new Date())
                  .setOrder(order);
        }
        return movies;
    }

    @Nullable
    private MovieList parseMovieListJson(String jsonStr) {
        final String TMDB_RESULTS = "results";

        JSONObject json;
        MovieList movies = new MovieList();
        try {
            json = new JSONObject(jsonStr);
            JSONArray resultsJson = json.getJSONArray(TMDB_RESULTS);
            for(int i = 0; i < resultsJson.length() ; i++) {
                Movie movie = MovieFactory.fromTMDBJsonObject(resultsJson.getJSONObject(i));
                movies.add(movie);

            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage(), e);
            return null;
        }
        return movies;
    }
}
