package com.example.popularmovies.data;

import android.provider.BaseColumns;

import com.example.popularmovies.util.MoviePosterUriBuilder;

/**
 * Defines tables, columns and content provider helper methods for the movies cache database
 * Created by rafael on 24/06/16.
 */
public class MoviesContract {
    /**
     * Defines the table contents of the movie table
     */
    public static final class MovieEntry implements BaseColumns {
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
    }

    /**
     * Defines the table contents of the list table
     */
    public static final class ListEntry implements BaseColumns {
        /**
         * The entry table name
         */
        public static final String TABLE_NAME = "list";

        /**
         * List's movie order: top rated or most popular
         */
        public static final String COLUMN_ORDER = "movies_order";

        /**
         * Date the list was fetched (timestamp)
         */
        public static final String COLUMN_DATE_FETCHED = "date_fetched";

    }

    /**
     * Relates movies and lists in many to many relationship
     */
    public static final class MovieListEntry implements BaseColumns {
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
    }
}
