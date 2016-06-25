package com.example.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by rafael on 24/06/16.
 */
public class MoviesContract {
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_ADULT = "adult";

        public static final String COLUMN_VOTE_AVG = "vote_avg";
    }

    public static final class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "list";

        public static final String COLUMN_ORDER = "movies_order";

        public static final String COLUMN_DATE_FETCHED = "date_fetched";

    }

    public static final class MovieListEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie_list";

        public static final String COLUMN_LIST_KEY = "id_list";

        public static final String COLUMN_MOVIE_KEY = "id_movie";

        public static final String COLUMN_ORDER = "movie_order";
    }
}
