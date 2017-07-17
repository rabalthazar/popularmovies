package com.example.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestDb {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void deleteDatabase() {
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
    }

    @Test
    public void testCreateDb() {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MoviesContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.ListEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.MovieListEntry.TABLE_NAME);

        deleteDatabase();
        SQLiteDatabase db = new MoviesDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );
        c.close();
        assertTrue("Error: Your database was created without movie, list and movie_list entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: Unable to query the database for movie table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(MoviesContract.MovieEntry._ID);
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_POSTER_PATH);
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_ADULT);
        movieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_VOTE_AVG);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        c.close();
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.ListEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: Unable to query the database for list table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> listColumnHashSet = new HashSet<>();
        listColumnHashSet.add(MoviesContract.ListEntry._ID);
        listColumnHashSet.add(MoviesContract.ListEntry.COLUMN_SELECTION);
        listColumnHashSet.add(MoviesContract.ListEntry.COLUMN_DATE_FETCHED);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            listColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        c.close();
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required list entry columns",
                listColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MovieListEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: Unable to query the database for list table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieListColumnHashSet = new HashSet<>();
        movieListColumnHashSet.add(MoviesContract.MovieListEntry._ID);
        movieListColumnHashSet.add(MoviesContract.MovieListEntry.COLUMN_LIST_KEY);
        movieListColumnHashSet.add(MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY);
        movieListColumnHashSet.add(MoviesContract.MovieListEntry.COLUMN_ORDER);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieListColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        c.close();
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie list entry columns",
                movieListColumnHashSet.isEmpty());

        db.close();
    }

    @Test
    public void testMovieTable() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movie = TestUtilities.createEmpireStrikesBackValues();
        Long movieId;
        movieId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movie);
        assertThat(movieId, equalTo(TestUtilities.TEST_MOVIE2_ID));

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MoviesContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from movie query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movie);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );

        // Close Cursor and Database
        cursor.close();
        db.close();
    }

    @Test
    public void testListTable() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues list = TestUtilities.createMostPopularListValues();
        Long listId;
        listId = db.insert(MoviesContract.ListEntry.TABLE_NAME, null, list);
        assertThat(listId, greaterThan(0L));

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MoviesContract.ListEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from movie query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, list);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );

        // Close Cursor and Database
        cursor.close();
        db.close();
    }

    @Test
    public void testMovieListTable() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues list = TestUtilities.createTopRatedListValues();
        Long listId = db.insert(MoviesContract.ListEntry.TABLE_NAME, null, list);
        assertThat(listId, greaterThan(0L));

        ContentValues movie1 = TestUtilities.createReturnOfTheJediValues();
        Long movieId1 = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movie1);
        assertThat(movieId1, equalTo(TestUtilities.TEST_MOVIE3_ID));

        ContentValues movie2 = TestUtilities.createStarWarsValues();
        Long movieId2 = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movie2);
        assertThat(movieId2, equalTo(TestUtilities.TEST_MOVIE1_ID));

        ContentValues movieList1 = TestUtilities.createMovieListValues(listId, movieId1, 0);
        Long movieListId1 = db.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, movieList1);
        assertThat(movieListId1, greaterThan(0L));

        ContentValues movieList2 = TestUtilities.createMovieListValues(listId, movieId2, 1);
        Long movieListId2 = db.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, movieList2);
        assertThat(movieListId2, greaterThan(0L));

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MoviesContract.MovieListEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                MoviesContract.MovieListEntry.COLUMN_ORDER + " ASC"
        );

        assertTrue( "Error: No Records returned from movie list query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList1);

        assertTrue( "Error: Just one record returned from movie list query",
                cursor.moveToNext() );

        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList2);

        assertFalse( "Error: More than two records returned from movie list query",
                cursor.moveToNext() );

        // Close Cursor and Database
        cursor.close();
        db.close();
    }
}
