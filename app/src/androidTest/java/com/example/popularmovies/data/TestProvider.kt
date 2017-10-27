package com.example.popularmovies.data

import android.content.ComponentName
import android.content.pm.PackageManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestProvider {
    private val mContext = InstrumentationRegistry.getTargetContext()

    @Before fun deleteAllRecordsFromProvider() {
        val contentResolver = mContext.contentResolver
        contentResolver.delete(MoviesContract.MovieListEntry.CONTENT_URI, null, null)
        contentResolver.delete(MoviesContract.ListEntry.CONTENT_URI, null, null)
        contentResolver.delete(MoviesContract.MovieEntry.CONTENT_URI, null, null)
    }

    @Test fun testProviderRegistry() {
        val packageManager = mContext.packageManager

        val componentName = ComponentName(mContext.packageName,
                MoviesProvider::class.java.name)
        try {
            val providerInfo = packageManager.getProviderInfo(componentName, 0)
            assertEquals("Error: MoviesProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    MoviesContract.CONTENT_AUTHORITY, providerInfo.authority)
        } catch (exception: PackageManager.NameNotFoundException) {
            fail(exception.message)
        }

    }

    @Test fun testProviderIsEmpty() {
        val contentResolver = mContext.contentResolver
        val listCursor = contentResolver.query(MoviesContract.ListEntry.CONTENT_URI, null, null, null, null)
        assertNotNull(listCursor)
        assertEquals("Error: List table is not empty", 0, listCursor!!.count.toLong())
        listCursor.close()

        val movieCursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null)
        assertNotNull(movieCursor)
        assertEquals("Error: Movie table is not empty", 0, movieCursor!!.count.toLong())
        movieCursor.close()

        val movieListCursor = contentResolver.query(MoviesContract.MovieListEntry.CONTENT_URI, null, null, null, null)
        assertNotNull(movieListCursor)
        assertEquals("Error: Movie List table is not empty", 0, movieListCursor!!.count.toLong())
        movieListCursor.close()
    }

    @Test fun testListContent() {
        val contentResolver = mContext.contentResolver
        val uriMatcher = MoviesProvider.buildUriMatcher()

        val list = TestUtilities.createTopRatedListValues()
        val listUri = contentResolver.insert(MoviesContract.ListEntry.CONTENT_URI, list)
        assertNotNull(listUri)
        val listMatch = uriMatcher.match(listUri)
        assertThat(listMatch, equalTo(MoviesProvider.LIST_BY_ID))

        val listId = MoviesContract.ListEntry.getIdFromUri(listUri)
        assertThat(listId, greaterThan(0L))

        var cursor = contentResolver.query(listUri!!, null, null, null, null)
        assertNotNull(cursor)
        assertTrue("Error: No Records returned from list query for uri " + listUri, cursor!!.moveToFirst())

        val idx = cursor.getColumnIndex(MoviesContract.ListEntry._ID)
        assertThat("Error: List id does not match", listId, equalTo(cursor.getLong(idx)))

        TestUtilities.validateCurrentRecord("Error: List Query Validation Failed",
                cursor, list)

        assertFalse("Error: More than one record returned from list query",
                cursor.moveToNext())
        cursor.close()

        cursor = contentResolver.query(MoviesContract.ListEntry.CONTENT_URI, null, null, null, null)
        assertNotNull(cursor)
        assertTrue("Error: No Records returned from list query for uri " + listUri, cursor!!.moveToFirst())

        TestUtilities.validateCurrentRecord("Error: List Query Validation Failed",
                cursor, list)

        assertFalse("Error: More than one record returned from list query",
                cursor.moveToNext())
        cursor.close()
    }

    @Test fun testMovieContent() {
        val contentResolver = mContext.contentResolver
        val uriMatcher = MoviesProvider.buildUriMatcher()

        val movie = TestUtilities.createEmpireStrikesBackValues()
        val movieUri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie)
        assertNotNull(movieUri)
        val movieMatch = uriMatcher.match(movieUri)
        assertThat(movieMatch, equalTo(MoviesProvider.MOVIE_BY_ID))

        val movieId = MoviesContract.ListEntry.getIdFromUri(movieUri)
        assertThat(movieId, equalTo(TestUtilities.TEST_MOVIE2_ID))

        var cursor = contentResolver.query(movieUri!!, null, null, null, null)
        assertNotNull(cursor)
        assertTrue("Error: No Records returned from movie query for uri " + movieUri, cursor!!.moveToFirst())

        val idx = cursor.getColumnIndex(MoviesContract.MovieEntry._ID)
        assertThat("Error: Movie id does not match", movieId, equalTo(cursor.getLong(idx)))
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movie)
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext())
        cursor.close()

        cursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null)
        assertNotNull(cursor)
        assertTrue("Error: No Records returned from movie query for movie uri ", cursor!!.moveToFirst())
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movie)
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext())
        cursor.close()
    }

    @Test fun testMovieListContent() {
        val contentResolver = mContext.contentResolver
        val uriMatcher = MoviesProvider.buildUriMatcher()

        val list = TestUtilities.createMostPopularListValues()
        val listUri = contentResolver.insert(MoviesContract.ListEntry.CONTENT_URI, list)
        val listMatch = uriMatcher.match(listUri)
        assertThat(listMatch, equalTo(MoviesProvider.LIST_BY_ID))
        val listId = MoviesContract.ListEntry.getIdFromUri(listUri)
        assertThat(listId, greaterThan(0L))

        val movie1 = TestUtilities.createReturnOfTheJediValues()
        val movie1Uri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie1)
        val movie1Match = uriMatcher.match(movie1Uri)
        assertThat(movie1Match, equalTo(MoviesProvider.MOVIE_BY_ID))
        val movie1Id = MoviesContract.ListEntry.getIdFromUri(movie1Uri)
        assertThat(movie1Id, equalTo(TestUtilities.TEST_MOVIE3_ID))

        val movie2 = TestUtilities.createStarWarsValues()
        val movie2Uri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie2)
        val movie2Match = uriMatcher.match(movie2Uri)
        assertThat(movie2Match, equalTo(MoviesProvider.MOVIE_BY_ID))
        val movie2Id = MoviesContract.ListEntry.getIdFromUri(movie2Uri)
        assertThat(movie2Id, equalTo(TestUtilities.TEST_MOVIE1_ID))

        val movieList1 = TestUtilities.createMovieListValues(listId, movie1Id, 0)
        val movieList1Uri = contentResolver.insert(MoviesContract.MovieListEntry.CONTENT_URI, movieList1)
        assertNotNull(movieList1Uri)
        val movieList1Match = uriMatcher.match(movieList1Uri)
        assertThat(movieList1Match, equalTo(MoviesProvider.MOVIE_LIST_BY_ID))
        val movieList1Id = MoviesContract.MovieListEntry.getIdFromUri(movieList1Uri)

        val movieList2 = TestUtilities.createMovieListValues(listId, movie2Id, 1)
        val movieList2Uri = contentResolver.insert(MoviesContract.MovieListEntry.CONTENT_URI, movieList2)
        val movieList2Match = uriMatcher.match(movieList2Uri)
        assertThat(movieList2Match, equalTo(MoviesProvider.MOVIE_LIST_BY_ID))

        var cursor = contentResolver.query(movieList1Uri!!, null, null, null, null)
        assertNotNull(cursor)
        assertTrue("Error: No Records returned from movie query for uri " + movieList1Uri, cursor!!.moveToFirst())

        val idx = cursor.getColumnIndex(MoviesContract.MovieListEntry._ID)
        assertThat("Error: Movie id does not match", movieList1Id, equalTo(cursor.getLong(idx)))
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, movieList1)
        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext())
        cursor.close()

        cursor = contentResolver.query(MoviesContract.MovieListEntry.CONTENT_URI, null, null, null,
                MoviesContract.MovieListEntry.COLUMN_ORDER + " ASC")
        assertNotNull(cursor)
        assertTrue("Error: No Records returned from movie query for movie list uri ", cursor!!.moveToFirst())

        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList1)

        assertTrue("Error: Just one record returned from movie list query",
                cursor.moveToNext())
        TestUtilities.validateCurrentRecord("Error: Movie List Query Validation Failed",
                cursor, movieList2)

        assertFalse("Error: More than two records returned from movie list query",
                cursor.moveToNext())
        cursor.close()
    }
}
