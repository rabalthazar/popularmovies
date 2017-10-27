package com.example.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {
    private static final long LIST_ID = 12;
    private static final long MOVIE_ID = 234;
    private static final long MOVIE_LIST_ID = 5;

    private static final Uri TEST_LIST_DIR = MoviesContract.ListEntry.CONTENT_URI;
    private static final Uri TEST_LIST_ITEM = MoviesContract.ListEntry.buildUri(LIST_ID);

    private static final Uri TEST_MOVIE_DIR = MoviesContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_ITEM = MoviesContract.MovieEntry.buildUri(MOVIE_ID);
    //private static final Uri TEST_MOVIES_BY_SELECTION = MoviesContract.MovieEntry.buildMoviesBySelectionUri(LIST_SELECTOR_POPULAR);

    private static final Uri TEST_MOVIE_LIST_DIR = MoviesContract.MovieListEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_LIST_ITEM = MoviesContract.MovieListEntry.buildUri(MOVIE_LIST_ID);

    @Test
    public void matchUris() {
        UriMatcher uriMatcher = MoviesProvider.Companion.buildUriMatcher();
        assertEquals(MoviesProvider.Companion.getLIST(), uriMatcher.match(TEST_LIST_DIR));
        assertEquals(MoviesProvider.Companion.getLIST_BY_ID(), uriMatcher.match(TEST_LIST_ITEM));

        assertEquals(MoviesProvider.Companion.getMOVIE(), uriMatcher.match(TEST_MOVIE_DIR));
        assertEquals(MoviesProvider.Companion.getMOVIE_BY_ID(), uriMatcher.match(TEST_MOVIE_ITEM));
        //assertEquals(MoviesProvider.MOVIES_BY_SELECTION, uriMatcher.match(TEST_MOVIES_BY_SELECTION));

        assertEquals(MoviesProvider.Companion.getMOVIE_LIST(), uriMatcher.match(TEST_MOVIE_LIST_DIR));
        assertEquals(MoviesProvider.Companion.getMOVIE_LIST_BY_ID(), uriMatcher.match(TEST_MOVIE_LIST_ITEM));
    }
}
