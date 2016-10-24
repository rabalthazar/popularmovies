package com.example.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.popularmovies.data.MoviesContract.ListEntry;
import com.example.popularmovies.data.MoviesContract.MovieEntry;
import com.example.popularmovies.data.MoviesContract.MovieListEntry;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

class TestUtilities {
    public static final String TEST_LIST1_SELECTION = "popular";
    public static final Integer TEST_LIST1_DATE = 1477326793; // Oct 24, 2016 14:33:13 (GMT-02)

    public static final String TEST_LIST2_SELECTION = "toprated";
    public static final Integer TEST_LIST2_DATE = 1477326979; // Oct 24, 2016 14:36:19 (GMT-02)

    public static final Long TEST_MOVIE1_ID = 11L;
    public static final String TEST_MOVIE1_TITLE = "Star Wars";
    public static final String TEST_MOVIE1_OVERVIEW = "Princess Leia is captured and held hostage " +
            "by the evil Imperial forces in their effort to take over the galactic Empire. " +
            "Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable " +
            "robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire.";
    public static final Integer TEST_MOVIE1_RELEASE = 227674800; // Mar 20, 1977
    public static final Double TEST_MOVIE1_VOTE_AVG = 7.9;
    public static final String TEST_MOVIE1_POSTER_PATH = "/tvSlBzAdRE29bZe5yYWrJ2ds137.jpg";

    public static final Long TEST_MOVIE2_ID = 1891L;
    public static final String TEST_MOVIE2_TITLE = "The Empire Strikes Back";
    public static final String TEST_MOVIE2_OVERVIEW = "The epic saga continues as Luke Skywalker, " +
            "in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from " +
            "aging master Yoda. But Darth Vader is more determined than ever to capture Luke. " +
            "Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO " +
            "and R2-D2 are thrown into various stages of capture, betrayal and despair.";
    public static final Integer TEST_MOVIE2_RELEASE = 327380400; // May 17, 1980
    public static final Double TEST_MOVIE2_VOTE_AVG = 5.8;
    public static final String TEST_MOVIE2_POSTER_PATH = "/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg";

    public static final Long TEST_MOVIE3_ID = 1892L;
    public static final String TEST_MOVIE3_TITLE = "Return of the Jedi";
    public static final String TEST_MOVIE3_OVERVIEW = "As Rebel leaders map their strategy for " +
            "an all-out attack on the Emperor's newer, bigger Death Star. Han Solo remains frozen " +
            "in the cavernous desert fortress of Jabba the Hutt, the most loathsome outlaw in " +
            "the universe, who is also keeping Princess Leia as a slave girl. Now a master of the " +
            "Force, Luke Skywalker rescues his friends, but he cannot become a true Jedi Knight " +
            "until he wages his own crucial battle against Darth Vader, who has sworn to win Luke " +
            "over to the dark side of the Force.";
    public static final Integer TEST_MOVIE3_RELEASE = 422506800; // May 23, 1983
    public static final Double TEST_MOVIE3_VOTE_AVG = 7.7;
    public static final String TEST_MOVIE3_POSTER_PATH = "/ghd5zOQnDaDW1mxO7R5fXXpZMu.jpg";

    public static ContentValues createMostPopularListValues() {
        ContentValues listValues = new ContentValues();
        listValues.put(ListEntry.COLUMN_SELECTION, TEST_LIST1_SELECTION);
        listValues.put(ListEntry.COLUMN_DATE_FETCHED, TEST_LIST1_DATE);
        return listValues;
    }

    public static ContentValues createTopRatedListValues() {
        ContentValues listValues = new ContentValues();
        listValues.put(ListEntry.COLUMN_SELECTION, TEST_LIST2_SELECTION);
        listValues.put(ListEntry.COLUMN_DATE_FETCHED, TEST_LIST2_DATE);
        return listValues;
    }

    public static ContentValues createStarWarsValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry._ID, TEST_MOVIE1_ID);
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE1_TITLE);
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE1_OVERVIEW);
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE1_RELEASE);
        movieValues.put(MovieEntry.COLUMN_ADULT, 0);
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, TEST_MOVIE1_VOTE_AVG);
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE1_POSTER_PATH);
        return movieValues;
    }

    public static ContentValues createEmpireStrikesBackValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry._ID, TEST_MOVIE2_ID);
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE2_TITLE);
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE2_OVERVIEW);
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE2_RELEASE);
        movieValues.put(MovieEntry.COLUMN_ADULT, 0);
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, TEST_MOVIE2_VOTE_AVG);
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE2_POSTER_PATH);
        return movieValues;
    }

    public static ContentValues createReturnOfTheJediValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry._ID, TEST_MOVIE3_ID);
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE3_TITLE);
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE3_OVERVIEW);
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE3_RELEASE);
        movieValues.put(MovieEntry.COLUMN_ADULT, 0);
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, TEST_MOVIE3_VOTE_AVG);
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE3_POSTER_PATH);
        return movieValues;
    }

    public static ContentValues createMovieListValues(Long listId, Long movieId, Integer order) {
        ContentValues movieListValues = new ContentValues();
        movieListValues.put(MovieListEntry.COLUMN_LIST_KEY, listId);
        movieListValues.put(MovieListEntry.COLUMN_MOVIE_KEY, movieId);
        movieListValues.put(MovieListEntry.COLUMN_ORDER, order);
        return movieListValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
