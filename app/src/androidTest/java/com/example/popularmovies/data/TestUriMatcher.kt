package com.example.popularmovies.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestUriMatcher {

    @Test fun matchUris() {
        val uriMatcher = MoviesProvider.buildUriMatcher()
        assertEquals(MoviesProvider.LIST.toLong(), uriMatcher.match(TEST_LIST_DIR).toLong())
        assertEquals(MoviesProvider.LIST_BY_ID.toLong(), uriMatcher.match(TEST_LIST_ITEM).toLong())

        assertEquals(MoviesProvider.MOVIE.toLong(), uriMatcher.match(TEST_MOVIE_DIR).toLong())
        assertEquals(MoviesProvider.MOVIE_BY_ID.toLong(), uriMatcher.match(TEST_MOVIE_ITEM).toLong())
        //assertEquals(MoviesProvider.MOVIES_BY_SELECTION, uriMatcher.match(TEST_MOVIES_BY_SELECTION));

        assertEquals(MoviesProvider.MOVIE_LIST.toLong(), uriMatcher.match(TEST_MOVIE_LIST_DIR).toLong())
        assertEquals(MoviesProvider.MOVIE_LIST_BY_ID.toLong(), uriMatcher.match(TEST_MOVIE_LIST_ITEM).toLong())
    }

    companion object {
        private const val LIST_ID: Long = 12
        private const val MOVIE_ID: Long = 234
        private const val MOVIE_LIST_ID: Long = 5

        private val TEST_LIST_DIR = MoviesContract.ListEntry.CONTENT_URI
        private val TEST_LIST_ITEM = MoviesContract.ListEntry.buildUri(LIST_ID)

        private val TEST_MOVIE_DIR = MoviesContract.MovieEntry.CONTENT_URI
        private val TEST_MOVIE_ITEM = MoviesContract.MovieEntry.buildUri(MOVIE_ID)
        //private static final Uri TEST_MOVIES_BY_SELECTION = MoviesContract.MovieEntry.buildMoviesBySelectionUri(LIST_SELECTOR_POPULAR);

        private val TEST_MOVIE_LIST_DIR = MoviesContract.MovieListEntry.CONTENT_URI
        private val TEST_MOVIE_LIST_ITEM = MoviesContract.MovieListEntry.buildUri(MOVIE_LIST_ID)
    }
}
