package com.example.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.popularmovies.util.MoviePosterUriBuilder;

import androidx.annotation.NonNull;

/**
 * Defines tables, columns and content provider helper methods for the movies cache database
 * Created by rafael on 24/06/16.
 */
public class MoviesContract {
    public enum ListSelections {
        MOST_POPULAR("popular"),
        TOP_RATED("top_rated");

        private final String selection;

        ListSelections(final String selection) {
            this.selection = selection;
        }

        @Override
        public String toString() {
            return selection;
        }

        public static boolean contains(String test) {
            for (ListSelections selection: ListSelections.values()) {
                if (test.equals(selection.toString())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Content provider authority domain
     */
    public static final String CONTENT_AUTHORITY = "com.example.popularmovies";

    /**
     * Content provider base URI
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Content provider movie URI path
     */
    public static final String MOVIE_LOCATION = "movie";

    /**
     * Content provider list URI path
     */
    public static final String LIST_LOCATION = "list";

    /**
     * Content provider movie_list URI path
     */
    public static final String MOVIE_LIST_LOCATION = "movie_list";

    /**
     * Defines the contract for the movie table
     */
    public static final class MovieEntry implements BaseColumns {
        /**
         * Movie's content provider URI
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_LOCATION).build();

        /**
         * Movie's content provider dir MIME type
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_LOCATION;

        /**
         * Movie's content provider item MIME type
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_LOCATION;

        /**
         * The entry table name
         */
        public static final String TABLE_NAME = "movie";

        /**
         * Movie title column
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * Movie release date in unix timestamp
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /**
         * Movie relative poster path in TMDB site.
         * @see MoviePosterUriBuilder
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /**
         * Movie brief description
         */
        public static final String COLUMN_OVERVIEW = "overview";

        /**
         * Adult rate indicator. 1 for adult rated movies, 0 for non adult.
         */
        public static final String COLUMN_ADULT = "adult";

        /**
         * TMDB movie votes average
         */
        public static final String COLUMN_VOTE_AVG = "vote_avg";

        public static Long getIdFromUri(Uri movieByIdUri) {
            try {
                return Long.parseLong(movieByIdUri.getPathSegments().get(1));
            } catch (Exception exception) {
                return -1L;
            }
        }

        public static String getSelectionFromUri(Uri moviesBySelectionUri) {
            return moviesBySelectionUri.getPathSegments().get(1);
        }

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildBySelectionUri(String selection) {
            return CONTENT_URI.buildUpon().appendPath(selection).build();
        }
    }

    /**
     * Defines the contract for the list table
     */
    public static final class ListEntry implements BaseColumns {
        /**
         * List's content provider URI
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(LIST_LOCATION).build();

        /**
         * List's content provider dir MIME type
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + LIST_LOCATION;

        /**
         * List's content provider item MIME type
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + LIST_LOCATION;

        /**
         * The entry table name
         */
        public static final String TABLE_NAME = "list";

        /**
         * List's movie selection: top rated or most popular
         */
        public static final String COLUMN_SELECTION = "selection";

        /**
         * Date the list was fetched (timestamp)
         */
        public static final String COLUMN_DATE_FETCHED = "date_fetched";

        @NonNull
        public static Long getIdFromUri(Uri listByIdUri) {
            try {
                return Long.parseLong(listByIdUri.getPathSegments().get(1));
            } catch (Exception exception){
                return -1L;
            }
        }

        public static String getSelectionFromUri(Uri listBySelectionUri) {
            return listBySelectionUri.getPathSegments().get(1);
        }

        public static Uri buildUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildBySelectionUri(String selection) {
            return CONTENT_URI.buildUpon().appendPath(selection).build();
        }
    }

    /**
     * Relates movies and lists in many to many relationship
     */
    public static final class MovieListEntry implements BaseColumns {
        /**
         * Movie List content provider URI
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_LIST_LOCATION).build();

        /**
         * Movie List content provider dir MIME type
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_LIST_LOCATION;

        /**
         * Movie List content provider item MIME type
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_LIST_LOCATION;


        /**
         * The entry table name
         */
        public static final String TABLE_NAME = "movie_list";

        /**
         * List id column
         */
        public static final String COLUMN_LIST_KEY = "id_list";

        /**
         * Movie id column
         */
        public static final String COLUMN_MOVIE_KEY = "id_movie";

        /**
         * Movie order in the list
         */
        public static final String COLUMN_ORDER = "movie_order";

        public static Long getIdFromUri(Uri movieListByIdUri) {
            try {
                return Long.parseLong(movieListByIdUri.getPathSegments().get(1));
            } catch (Exception exception){
                return -1L;
            }
        }

        public static String getSelectionFromUri(Uri movieListBySelection) {
            return movieListBySelection.getPathSegments().get(1);
        }

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildBySelectionUri(String selection) {
            return CONTENT_URI.buildUpon().appendPath(selection).build();
        }

        public static Uri buildByListAndMovieId(Long listId, Long movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(listId))
                    .appendPath(Long.toString(movieId))
                    .build();
        }
    }
}
