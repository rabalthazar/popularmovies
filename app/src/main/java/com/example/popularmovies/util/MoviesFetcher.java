package com.example.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.model.List;
import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Loads data from the TMDB API
 * Created by rafael on 23/05/16.
 */
public class MoviesFetcher {
    /**
     * Log tag for debugging purposes
     */
    final private String LOG_TAG = MoviesFetcher.class.getSimpleName();

    private final Context mContext;

    /**
     * TMDB base API URL
     */
    final private String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";

    /**
     * TMDB API key parameter name
     */
    final private String API_KEY_PARAM = "api_key";

    public MoviesFetcher(Context context) {
        mContext = context;
    }

    /**
     * Fetches the json movie list from the TMDB API and returns it a List class
     * @param selection The list from where to fetch
     */
    public void fetchMovies(String selection) {
        if (!MoviesContract.ListSelections.contains(selection)) {
            return;
        }

        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(selection)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        String jsonMovieListStr = UriFetcher.fetch(uri);
        if (jsonMovieListStr == null) {
            return;
        }
        List movies = parseMovieListJson(jsonMovieListStr);
        if (movies != null) {
            movies.setDateFetched(new Date())
                  .setSelection(selection);
            addMovieList(movies);
        }
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

    @Nullable
    private Integer addMovieList(List list) {
        Long listId = insertOrUpdateList(list);
        if (listId <= 0) {
            return null;
        }
        list.setId(listId);
        for (Movie movie : list.getMovies()) {
            insertOrUpdateMovie(movie);
        }
        return fillMovieList(list);
    }

    private Long insertOrUpdateList(List list) {
        Uri listUri = MoviesContract.ListEntry.buildBySelectionUri(list.getSelection());
        Uri listUriById;
        ContentValues listValues = new ContentValues();
        listValues.put(MoviesContract.ListEntry.COLUMN_SELECTION, list.getSelection());
        listValues.put(MoviesContract.ListEntry.COLUMN_DATE_FETCHED, list.getDateFetched().getTime());
        Cursor listCursor = mContext.getContentResolver().query(listUri, null, null, null, null);
        if (listCursor == null || !listCursor.moveToFirst()) {
            try {
                listUriById = mContext.getContentResolver().insert(MoviesContract.ListEntry.CONTENT_URI, listValues);
                if (listCursor != null) listCursor.close();
                return MoviesContract.ListEntry.getIdFromUri(listUriById);
            } catch (SQLException exception) {
                Log.d(LOG_TAG, "Failed to insert list into database");
                return -1L;
            }
        }
        Integer listIdCol = listCursor.getColumnIndex(MoviesContract.ListEntry._ID);
        Long listId = listCursor.getLong(listIdCol);
        listCursor.close();
        listUriById = MoviesContract.ListEntry.buildUri(listId);
        if (mContext.getContentResolver().update(listUriById, listValues, null, null) <= 0) {
            Log.d(LOG_TAG, "Failed to update list into database");
        }
        return listId;
    }

    private Long insertOrUpdateMovie(final Movie movie) {
        final Uri movieUri = MoviesContract.MovieEntry.buildUri(movie.getId());
        ContentValues movieValues = new ContentValues();
        movieValues.put(MoviesContract.MovieEntry._ID, movie.getId());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_ADULT, movie.isAdult());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVG, movie.getVoteAverage());
        Cursor movieCursor = mContext.getContentResolver().query(movieUri, null, null, null, null);
        if (movieCursor == null || !movieCursor.moveToFirst()) {
            try {
                Uri newMovieUri = mContext.getContentResolver().insert(
                        MoviesContract.MovieEntry.CONTENT_URI,
                        movieValues
                );
                if (movieCursor!= null) movieCursor.close();
                return MoviesContract.MovieEntry.getIdFromUri(newMovieUri);
            } catch (SQLException exception) {
                Log.d(LOG_TAG, "Failed to insert movie into database");
                return -1L;
            }
        }
        movieCursor.close();
        if (mContext.getContentResolver().update(movieUri, movieValues, null, null) <= 0) {
            Log.d(LOG_TAG, "Failed to update movie into database");
        }
        return movie.getId();
    }

    private Integer fillMovieList(List list) {
        Uri movieListBySelectionUri = MoviesContract.MovieListEntry.buildBySelectionUri(list.getSelection());
        Integer movieListOrder = 1;
        ArrayList<ContentValues> movieListValuesList = new ArrayList<>();
        for (Movie movie : list.getMovies()) {
            ContentValues movieListValues = new ContentValues();
            movieListValues.put(MoviesContract.MovieListEntry.COLUMN_LIST_KEY, list.getId());
            movieListValues.put(MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY, movie.getId());
            movieListValues.put(MoviesContract.MovieListEntry.COLUMN_ORDER, movieListOrder++);
            movieListValuesList.add(movieListValues);
        }
        ContentValues[] movieListValuesArray = new ContentValues[movieListValuesList.size()];
        movieListValuesArray = movieListValuesList.toArray(movieListValuesArray);
        return  mContext.getContentResolver().bulkInsert(movieListBySelectionUri, movieListValuesArray);
    }
}
