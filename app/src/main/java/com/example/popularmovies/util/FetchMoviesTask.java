package com.example.popularmovies.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * An asynchronous task class that loads data from the TMDB API
 * Created by rafael on 23/05/16.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List> {
    /**
     * Log tag for debugging purposes
     */
    final private String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    /**
     * The adapter to which the fetched list will be added to
     */
    MovieArrayAdapter mAdapter;

    /**
     * TMDB base API URL
     */
    final private String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";

    /**
     * TMDB API key parameter name
     */
    final private String API_KEY_PARAM = "api_key";

    /**
     * AsyncTask invoker method
     * @param params params[0] must be the fetch order: popular or top_rated
     * @return The fetched movie list
     */
    @Override
    protected List doInBackground(String... params) {
        // The order param is required
        if (params.length == 0) {
            return null;
        }

        return fetchMovies(params[0]);
    }

    /**
     * Fills the adapter with the fetched movies
     * @param movies
     */
    @Override
    protected void onPostExecute(List movies) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();
        if (movies == null) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        for(Movie movie: movies.getMovies()) {
            mAdapter.add(movie);
        }
        mAdapter.notifyDataSetChanged();
    }

    public FetchMoviesTask setAdapter(MovieArrayAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    /**
     * Fetches the json movie list from the TMDB API and returns it a List class
     * @param order The list from where to fetch
     * @return A List of fetched movies
     */
    @Nullable
    private List fetchMovies(String order) {
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
        List movies = parseMovieListJson(jsonMovieListStr);
        if (movies != null) {
            movies.setDateFetched(new Date())
                  .setOrder(order);
        }
        return movies;
    }

    @Nullable
    private List parseMovieListJson(String jsonStr) {
        final String TMDB_RESULTS = "results";

        JSONObject json;
        List movies = new List();
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
