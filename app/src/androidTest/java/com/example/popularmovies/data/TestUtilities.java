package com.example.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.popularmovies.data.MoviesContract.MovieEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by rafael on 25/06/16.
 */
public class TestUtilities extends AndroidTestCase {
    public static final Long TEST_MOVIE_ID = 1891L;
    public static final String TEST_MOVIE_TITLE = "The Empire Strikes Back";
    public static final String TEST_MOVIE_OVERVIEW = "The epic saga continues as Luke Skywalker, " +
            "in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from " +
            "aging master Yoda. But Darth Vader is more determined than ever to capture Luke. " +
            "Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO " +
            "and R2-D2 are thrown into various stages of capture, betrayal and despair.";
    public static final Integer TEST_MOVIE_RELEASE = 1466844600; // May 17, 1980
    public static final String TEST_MOVIE_POSTER_PATH = "/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg";

    public static ContentValues createEmpireStrikesBackValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry._ID, TEST_MOVIE_ID);
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE_TITLE);
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE_OVERVIEW);
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE_RELEASE);
        movieValues.put(MovieEntry.COLUMN_ADULT, 0);
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, 5.8);
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE_POSTER_PATH);
        return movieValues;
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