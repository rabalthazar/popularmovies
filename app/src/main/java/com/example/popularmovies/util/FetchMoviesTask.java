package com.example.popularmovies.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rafael on 23/05/16.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
    final private String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    MovieArrayAdapter mAdapter;

    final private String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    final private String MOVIES_POPULAR_PATH = "popular";
    final private String API_KEY_PARAM = "api_key";

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        return fetchByPopularity();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();
        for(Movie movie: movies) {
            mAdapter.add(movie);
        }
        mAdapter.notifyDataSetChanged();
    }

    public FetchMoviesTask setAdapter(MovieArrayAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public ArrayList<Movie> fetchByPopularity() {
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

    private ArrayList<Movie> parseMovieListJson(String jsonStr) {
        final String TMDB_RESULTS = "results";

        JSONObject json;
        ArrayList<Movie> movies = new ArrayList<>();
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
        return movies;
    }
}
