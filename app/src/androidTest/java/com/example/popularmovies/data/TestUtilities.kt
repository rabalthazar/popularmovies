package com.example.popularmovies.data

import android.content.ContentValues
import android.database.Cursor

import com.example.popularmovies.data.MoviesContract.ListEntry
import com.example.popularmovies.data.MoviesContract.MovieEntry
import com.example.popularmovies.data.MoviesContract.MovieListEntry

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

internal object TestUtilities {
    private val TEST_LIST1_SELECTION = MoviesContract.ListSelections.MOST_POPULAR.toString()
    private val TEST_LIST1_DATE = 1477326793 // Oct 24, 2016 14:33:13 (GMT-02)

    private val TEST_LIST2_SELECTION = MoviesContract.ListSelections.TOP_RATED.toString()
    private val TEST_LIST2_DATE = 1477326979 // Oct 24, 2016 14:36:19 (GMT-02)

    val TEST_MOVIE1_ID = 11L
    private val TEST_MOVIE1_TITLE = "Star Wars"
    private val TEST_MOVIE1_OVERVIEW = "Princess Leia is captured and held hostage " +
            "by the evil Imperial forces in their effort to take over the galactic Empire. " +
            "Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable " +
            "robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire."
    private val TEST_MOVIE1_RELEASE = 227674800 // Mar 20, 1977
    private val TEST_MOVIE1_VOTE_AVG = 7.9
    private val TEST_MOVIE1_POSTER_PATH = "/tvSlBzAdRE29bZe5yYWrJ2ds137.jpg"

    val TEST_MOVIE2_ID = 1891L
    private val TEST_MOVIE2_TITLE = "The Empire Strikes Back"
    private val TEST_MOVIE2_OVERVIEW = "The epic saga continues as Luke Skywalker, " +
            "in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from " +
            "aging master Yoda. But Darth Vader is more determined than ever to capture Luke. " +
            "Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO " +
            "and R2-D2 are thrown into various stages of capture, betrayal and despair."
    private val TEST_MOVIE2_RELEASE = 327380400 // May 17, 1980
    private val TEST_MOVIE2_VOTE_AVG = 5.8
    private val TEST_MOVIE2_POSTER_PATH = "/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg"

    val TEST_MOVIE3_ID = 1892L
    private val TEST_MOVIE3_TITLE = "Return of the Jedi"
    private val TEST_MOVIE3_OVERVIEW = "As Rebel leaders map their strategy for " +
            "an all-out attack on the Emperor's newer, bigger Death Star. Han Solo remains frozen " +
            "in the cavernous desert fortress of Jabba the Hutt, the most loathsome outlaw in " +
            "the universe, who is also keeping Princess Leia as a slave girl. Now a master of the " +
            "Force, Luke Skywalker rescues his friends, but he cannot become a true Jedi Knight " +
            "until he wages his own crucial battle against Darth Vader, who has sworn to win Luke " +
            "over to the dark side of the Force."
    private val TEST_MOVIE3_RELEASE = 422506800 // May 23, 1983
    private val TEST_MOVIE3_VOTE_AVG = 7.7
    private val TEST_MOVIE3_POSTER_PATH = "/ghd5zOQnDaDW1mxO7R5fXXpZMu.jpg"

    fun createMostPopularListValues(): ContentValues {
        val listValues = ContentValues()
        listValues.put(ListEntry.COLUMN_SELECTION, TEST_LIST1_SELECTION)
        listValues.put(ListEntry.COLUMN_DATE_FETCHED, TEST_LIST1_DATE)
        return listValues
    }

    fun createTopRatedListValues(): ContentValues {
        val listValues = ContentValues()
        listValues.put(ListEntry.COLUMN_SELECTION, TEST_LIST2_SELECTION)
        listValues.put(ListEntry.COLUMN_DATE_FETCHED, TEST_LIST2_DATE)
        return listValues
    }

    fun createStarWarsValues(): ContentValues {
        val movieValues = ContentValues()
        movieValues.put(MovieEntry._ID, TEST_MOVIE1_ID)
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE1_TITLE)
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE1_OVERVIEW)
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE1_RELEASE)
        movieValues.put(MovieEntry.COLUMN_ADULT, 0)
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, TEST_MOVIE1_VOTE_AVG)
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE1_POSTER_PATH)
        return movieValues
    }

    fun createEmpireStrikesBackValues(): ContentValues {
        val movieValues = ContentValues()
        movieValues.put(MovieEntry._ID, TEST_MOVIE2_ID)
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE2_TITLE)
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE2_OVERVIEW)
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE2_RELEASE)
        movieValues.put(MovieEntry.COLUMN_ADULT, 0)
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, TEST_MOVIE2_VOTE_AVG)
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE2_POSTER_PATH)
        return movieValues
    }

    fun createReturnOfTheJediValues(): ContentValues {
        val movieValues = ContentValues()
        movieValues.put(MovieEntry._ID, TEST_MOVIE3_ID)
        movieValues.put(MovieEntry.COLUMN_TITLE, TEST_MOVIE3_TITLE)
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, TEST_MOVIE3_OVERVIEW)
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_MOVIE3_RELEASE)
        movieValues.put(MovieEntry.COLUMN_ADULT, 0)
        movieValues.put(MovieEntry.COLUMN_VOTE_AVG, TEST_MOVIE3_VOTE_AVG)
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, TEST_MOVIE3_POSTER_PATH)
        return movieValues
    }

    fun createMovieListValues(listId: Long?, movieId: Long?, order: Int?): ContentValues {
        val movieListValues = ContentValues()
        movieListValues.put(MovieListEntry.COLUMN_LIST_KEY, listId)
        movieListValues.put(MovieListEntry.COLUMN_MOVIE_KEY, movieId)
        movieListValues.put(MovieListEntry.COLUMN_ORDER, order)
        return movieListValues
    }

    fun validateCurrentRecord(error: String, valueCursor: Cursor, expectedValues: ContentValues) {
        val valueSet = expectedValues.valueSet()
        for ((columnName, value) in valueSet) {
            val idx = valueCursor.getColumnIndex(columnName)
            assertFalse("Column '$columnName' not found. $error", idx == -1)
            val expectedValue = value.toString()
            assertEquals("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx))
        }
    }
}
