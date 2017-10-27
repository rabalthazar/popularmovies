package com.example.popularmovies.data

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TestDb {
    private val mContext = InstrumentationRegistry.getTargetContext()

    @Before fun deleteDatabase() {
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME)
    }

    @Test fun testCreateDb() {
        val tableNameHashSet = HashSet<String>()
        tableNameHashSet.add(MoviesContract.MovieEntry.TABLE_NAME)
        tableNameHashSet.add(MoviesContract.ListEntry.TABLE_NAME)
        tableNameHashSet.add(MoviesContract.MovieListEntry.TABLE_NAME)

        deleteDatabase()
        val db = MoviesDbHelper(mContext).writableDatabase
        assertEquals(true, db.isOpen)

        var c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", // columns to group by
                null)
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst())

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0))
        } while (c.moveToNext())
        c.close()
        assertTrue("Error: Your database was created without movie, list and movie_list entry tables",
                tableNameHashSet.isEmpty())

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MovieEntry.TABLE_NAME + ")", null)

        assertTrue("Error: Unable to query the database for movie table information.",
                c.moveToFirst())

        // Build a HashSet of all of the column names we want to look for
        val movieColumnHashSet = HashSet<String>()
        movieColumnHashSet.add(MoviesContract.MovieEntry._ID)
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_TITLE)
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_OVERVIEW)
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_ADULT)
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_VOTE_AVG)

        var columnNameIndex = c.getColumnIndex("name")
        do {
            val columnName = c.getString(columnNameIndex)
            movieColumnHashSet.remove(columnName)
        } while (c.moveToNext())
        c.close()
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty())

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.ListEntry.TABLE_NAME + ")", null)

        assertTrue("Error: Unable to query the database for list table information.",
                c.moveToFirst())

        // Build a HashSet of all of the column names we want to look for
        val listColumnHashSet = HashSet<String>()
        listColumnHashSet.add(MoviesContract.ListEntry._ID)
        listColumnHashSet.add(MoviesContract.ListEntry.COLUMN_SELECTION)
        listColumnHashSet.add(MoviesContract.ListEntry.COLUMN_DATE_FETCHED)

        columnNameIndex = c.getColumnIndex("name")
        do {
            val columnName = c.getString(columnNameIndex)
            listColumnHashSet.remove(columnName)
        } while (c.moveToNext())
        c.close()
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required list entry columns",
                listColumnHashSet.isEmpty())

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MovieListEntry.TABLE_NAME + ")", null)

        assertTrue("Error: Unable to query the database for list table information.",
                c.moveToFirst())

        // Build a HashSet of all of the column names we want to look for
        val movieListColumnHashSet = HashSet<String>()
        movieListColumnHashSet.add(MoviesContract.MovieListEntry._ID)
        movieListColumnHashSet.add(MoviesContract.MovieListEntry.COLUMN_LIST_KEY)
        movieListColumnHashSet.add(MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY)
        movieListColumnHashSet.add(MoviesContract.MovieListEntry.COLUMN_ORDER)

        columnNameIndex = c.getColumnIndex("name")
        do {
            val columnName = c.getString(columnNameIndex)
            movieListColumnHashSet.remove(columnName)
        } while (c.moveToNext())
        c.close()
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie list entry columns",
                movieListColumnHashSet.isEmpty())

        db.close()
    }

    @Test fun testMovieTable() {
        val dbHelper = MoviesDbHelper(mContext)
        val db = dbHelper.writableDatabase

        val movie = TestUtilities.createEmpireStrikesBackValues()
        val movieId: Long?
        movieId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movie)
        assertThat(movieId, equalTo(TestUtilities.TEST_MOVIE2_ID))

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        val cursor = db.query(
                MoviesContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null// sort order
        )// Table to Query
        // all columns
        // Columns for the "where" clause
        // Values for the "where" clause
        // columns to group by
        // columns to filter by row groups

        assertTrue("Error: No Records returned from movie query", cursor.moveToFirst())

        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movie)

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext())

        // Close Cursor and Database
        cursor.close()
        db.close()
    }

    @Test fun testListTable() {
        val dbHelper = MoviesDbHelper(mContext)
        val db = dbHelper.writableDatabase

        val list = TestUtilities.createMostPopularListValues()
        val listId: Long?
        listId = db.insert(MoviesContract.ListEntry.TABLE_NAME, null, list)
        assertThat(listId, greaterThan(0L))

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        val cursor = db.query(
                MoviesContract.ListEntry.TABLE_NAME, null, null, null, null, null, null// sort order
        )// Table to Query
        // all columns
        // Columns for the "where" clause
        // Values for the "where" clause
        // columns to group by
        // columns to filter by row groups

        assertTrue("Error: No Records returned from movie query", cursor.moveToFirst())

        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, list)

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext())

        // Close Cursor and Database
        cursor.close()
        db.close()
    }

    @Test fun testMovieListTable() {
        val dbHelper = MoviesDbHelper(mContext)
        val db = dbHelper.writableDatabase

        val list = TestUtilities.createTopRatedListValues()
        val listId = db.insert(MoviesContract.ListEntry.TABLE_NAME, null, list)
        assertThat(listId, greaterThan(0L))

        val movie1 = TestUtilities.createReturnOfTheJediValues()
        val movieId1 = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movie1)
        assertThat(movieId1, equalTo(TestUtilities.TEST_MOVIE3_ID))

        val movie2 = TestUtilities.createStarWarsValues()
        val movieId2 = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movie2)
        assertThat(movieId2, equalTo(TestUtilities.TEST_MOVIE1_ID))

        val movieList1 = TestUtilities.createMovieListValues(listId, movieId1, 0)
        val movieListId1 = db.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, movieList1)
        assertThat(movieListId1, greaterThan(0L))

        val movieList2 = TestUtilities.createMovieListValues(listId, movieId2, 1)
        val movieListId2 = db.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, movieList2)
        assertThat(movieListId2, greaterThan(0L))

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        val cursor = db.query(
                MoviesContract.MovieListEntry.TABLE_NAME, null, null, null, null, null, // columns to filter by row groups
                MoviesContract.MovieListEntry.COLUMN_ORDER + " ASC"
        )// Table to Query
        // all columns
        // Columns for the "where" clause
        // Values for the "where" clause

        assertTrue("Error: No Records returned from movie list query", cursor.moveToFirst())

        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList1)

        assertTrue("Error: Just one record returned from movie list query",
                cursor.moveToNext())

        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList2)

        assertFalse("Error: More than two records returned from movie list query",
                cursor.moveToNext())

        // Close Cursor and Database
        cursor.close()
        db.close()
    }
}
