package com.example.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TestProvider {
    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void deleteAllRecordsFromProvider() {
        ContentResolver contentResolver = mContext.getContentResolver();
        contentResolver.delete(MoviesContract.MovieListEntry.CONTENT_URI, null, null);
        contentResolver.delete(MoviesContract.ListEntry.CONTENT_URI, null, null);
        contentResolver.delete(MoviesContract.MovieEntry.CONTENT_URI, null, null);
    }

    @Test
    public void testProviderRegistry() {
        PackageManager packageManager = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MoviesProvider.class.getName());
        try {
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);
            assertEquals("Error: MoviesProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    MoviesContract.CONTENT_AUTHORITY, providerInfo.authority);
        } catch (PackageManager.NameNotFoundException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void testProviderIsEmpty() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor listCursor = contentResolver.query(MoviesContract.ListEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: List table is not empty", 0, listCursor.getCount());
        listCursor.close();

        Cursor movieCursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Movie table is not empty", 0, movieCursor.getCount());
        movieCursor.close();

        Cursor movieListCursor = contentResolver.query(MoviesContract.MovieListEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Movie List table is not empty", 0, movieListCursor.getCount());
        movieListCursor.close();
    }

    @Test
    public void testListContent() {
        ContentResolver contentResolver = mContext.getContentResolver();
        UriMatcher uriMatcher = MoviesProvider.buildUriMatcher();

        ContentValues list = TestUtilities.createTopRatedListValues();
        Uri listUri = contentResolver.insert(MoviesContract.ListEntry.CONTENT_URI, list);
        Integer listMatch = uriMatcher.match(listUri);
        assertThat(listMatch, equalTo(MoviesProvider.LIST_BY_ID));

        Long listId = MoviesContract.ListEntry.getIdFromUri(listUri);
        assertThat(listId, greaterThan(0L));

        Cursor cursor = contentResolver.query(listUri, null, null, null, null);
        assertTrue("Error: No Records returned from list query for uri " + listUri, cursor.moveToFirst());

        int idx = cursor.getColumnIndex(MoviesContract.ListEntry._ID);
        assertThat("Error: List id does not match", listId, equalTo(cursor.getLong(idx)));

        TestUtilities.validateCurrentRecord("Error: List Query Validation Failed",
                cursor, list);

        assertFalse( "Error: More than one record returned from list query",
                cursor.moveToNext() );
        cursor.close();

        cursor = contentResolver.query(MoviesContract.ListEntry.CONTENT_URI, null, null, null, null);
        assertTrue("Error: No Records returned from list query for uri " + listUri, cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: List Query Validation Failed",
                cursor, list);

        assertFalse( "Error: More than one record returned from list query",
                cursor.moveToNext() );
        cursor.close();
    }

    @Test
    public void testMovieContent() {
        ContentResolver contentResolver = mContext.getContentResolver();
        UriMatcher uriMatcher = MoviesProvider.buildUriMatcher();

        ContentValues movie = TestUtilities.createEmpireStrikesBackValues();
        Uri movieUri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie);
        Integer movieMatch = uriMatcher.match(movieUri);
        assertThat(movieMatch, equalTo(MoviesProvider.MOVIE_BY_ID));

        Long movieId = MoviesContract.ListEntry.getIdFromUri(movieUri);
        assertThat(movieId, equalTo(TestUtilities.TEST_MOVIE2_ID));

        Cursor cursor = contentResolver.query(movieUri, null, null, null, null);
        assertTrue("Error: No Records returned from movie query for uri " + movieUri, cursor.moveToFirst());

        int idx = cursor.getColumnIndex(MoviesContract.MovieEntry._ID);
        assertThat("Error: Movie id does not match", movieId, equalTo(cursor.getLong(idx)));
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movie);
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );
        cursor.close();

        cursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        assertTrue("Error: No Records returned from movie query for movie uri ", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movie);
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );
        cursor.close();
    }

    @Test
    public void testMovieListContent() {
        ContentResolver contentResolver = mContext.getContentResolver();
        UriMatcher uriMatcher = MoviesProvider.buildUriMatcher();

        ContentValues list = TestUtilities.createMostPopularListValues();
        Uri listUri = contentResolver.insert(MoviesContract.ListEntry.CONTENT_URI, list);
        Integer listMatch = uriMatcher.match(listUri);
        assertThat(listMatch, equalTo(MoviesProvider.LIST_BY_ID));
        Long listId = MoviesContract.ListEntry.getIdFromUri(listUri);
        assertThat(listId, greaterThan(0L));

        ContentValues movie1 = TestUtilities.createReturnOfTheJediValues();
        Uri movie1Uri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie1);
        Integer movie1Match = uriMatcher.match(movie1Uri);
        assertThat(movie1Match, equalTo(MoviesProvider.MOVIE_BY_ID));
        Long movie1Id = MoviesContract.ListEntry.getIdFromUri(movie1Uri);
        assertThat(movie1Id, equalTo(TestUtilities.TEST_MOVIE3_ID));

        ContentValues movie2 = TestUtilities.createStarWarsValues();
        Uri movie2Uri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie2);
        Integer movie2Match = uriMatcher.match(movie2Uri);
        assertThat(movie2Match, equalTo(MoviesProvider.MOVIE_BY_ID));
        Long movie2Id = MoviesContract.ListEntry.getIdFromUri(movie2Uri);
        assertThat(movie2Id, equalTo(TestUtilities.TEST_MOVIE1_ID));

        ContentValues movieList1 = TestUtilities.createMovieListValues(listId, movie1Id, 0);
        Uri movieList1Uri = contentResolver.insert(MoviesContract.MovieListEntry.CONTENT_URI, movieList1);
        Integer movieList1Match = uriMatcher.match(movieList1Uri);
        assertThat(movieList1Match, equalTo(MoviesProvider.MOVIE_LIST_BY_ID));
        Long movieList1Id = MoviesContract.MovieListEntry.getIdFromUri(movieList1Uri);

        ContentValues movieList2 = TestUtilities.createMovieListValues(listId, movie2Id, 1);
        Uri movieList2Uri = contentResolver.insert(MoviesContract.MovieListEntry.CONTENT_URI, movieList2);
        Integer movieList2Match = uriMatcher.match(movieList2Uri);
        assertThat(movieList2Match, equalTo(MoviesProvider.MOVIE_LIST_BY_ID));

        Cursor cursor = contentResolver.query(movieList1Uri, null, null, null, null);
        assertTrue("Error: No Records returned from movie query for uri " + movieList1Uri, cursor.moveToFirst());

        int idx = cursor.getColumnIndex(MoviesContract.MovieListEntry._ID);
        assertThat("Error: Movie id does not match", movieList1Id, equalTo(cursor.getLong(idx)));
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movieList1);
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );
        cursor.close();

        cursor = contentResolver.query(MoviesContract.MovieListEntry.CONTENT_URI, null, null, null,
                MoviesContract.MovieListEntry.COLUMN_ORDER + " ASC");
        assertTrue("Error: No Records returned from movie query for movie list uri ", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList1);

        assertTrue( "Error: Just one record returned from movie list query",
                cursor.moveToNext() );
        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList2);

        assertFalse( "Error: More than two records returned from movie list query",
                cursor.moveToNext() );
        cursor.close();
    }
}
