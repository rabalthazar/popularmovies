package com.example.popularmovies.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.popularmovies.util.MoviesFetcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TestDb {
    private val mContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test fun testCreateDb() {
        val tableNameHashSet = HashSet<String>()
        tableNameHashSet.add("movie")
        tableNameHashSet.add("list")
        tableNameHashSet.add("movie_list")

        val db = Room.inMemoryDatabaseBuilder(mContext, MoviesDatabase::class.java).build().openHelper.writableDatabase
        assertEquals(true, db.isOpen)

        var c = db.query("SELECT name FROM sqlite_master WHERE type='table'", // columns to group by
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
        c = db.query("PRAGMA table_info(movie)", null)

        assertTrue("Error: Unable to query the database for movie table information.",
                c.moveToFirst())

        // Build a HashSet of all of the column names we want to look for
        val movieColumnHashSet = HashSet<String>()
        movieColumnHashSet.add("_id")
        movieColumnHashSet.add("title")
        movieColumnHashSet.add("overview")
        movieColumnHashSet.add("poster_path")
        movieColumnHashSet.add("release_date")
        movieColumnHashSet.add("adult")
        movieColumnHashSet.add("vote_average")

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

        c = db.query("PRAGMA table_info(list)", null)

        assertTrue("Error: Unable to query the database for list table information.",
                c.moveToFirst())

        // Build a HashSet of all of the column names we want to look for
        val listColumnHashSet = HashSet<String>()
        listColumnHashSet.add("_id")
        listColumnHashSet.add("selection")
        listColumnHashSet.add("date_fetched")

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

        c = db.query("PRAGMA table_info(movie_list)", null)

        assertTrue("Error: Unable to query the database for list table information.",
                c.moveToFirst())

        // Build a HashSet of all of the column names we want to look for
        val movieListColumnHashSet = HashSet<String>()
        movieListColumnHashSet.add("_id")
        movieListColumnHashSet.add("list_id")
        movieListColumnHashSet.add("movie_id")
        movieListColumnHashSet.add("order")

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
        val db = Room.inMemoryDatabaseBuilder(mContext, MoviesDatabase::class.java).build()

        val movieDao = db.movieDao()
        val movie = TestUtilities.createEmpireStrikesBackValues()
        val movieId: Long?
        movieId = movieDao.insert(movie)
        assertThat(movieId, equalTo(TestUtilities.TEST_MOVIE2_ID))

        val fetchedMovie = movieDao.loadById(TestUtilities.TEST_MOVIE2_ID)

        assertNotNull("Error: No Records returned from movie query", fetchedMovie)

        assertTrue("Error: Movie Query Validation Failed", TestUtilities.compareMovies(movie, fetchedMovie!!))

        db.close()
    }

    @Test fun testListTable() {
        val db = Room.inMemoryDatabaseBuilder(mContext, MoviesDatabase::class.java).build()

        val listDao = db.listDao()
        val list = TestUtilities.createMostPopularListValues()
        val listId: Long?
        listId = listDao.insert(list)
        assertThat(listId, greaterThan(0L))
        list.id = listId

        val fetchedList = listDao.findBySelection(MoviesFetcher.ListSelections.MOST_POPULAR.toString())

        assertNotNull("Error: No Records returned from list query", fetchedList)

        assertTrue("Error: List Query Validation Failed", TestUtilities.compareLists(list, fetchedList!!))

        db.close()
    }

    @Test fun testMovieListTable() {
        val db = Room.inMemoryDatabaseBuilder(mContext, MoviesDatabase::class.java).build()

        val listDao = db.listDao()
        val movieDao = db.movieDao()
        val movieListDao = db.movieListDao()

        val list = TestUtilities.createTopRatedListValues()
        val listId = listDao.insert(list)
        assertThat(listId, greaterThan(0L))

        val movie1 = TestUtilities.createReturnOfTheJediValues()
        val movieId1 = movieDao.insert(movie1)
        assertThat(movieId1, equalTo(TestUtilities.TEST_MOVIE3_ID))

        val movie2 = TestUtilities.createStarWarsValues()
        val movieId2 = movieDao.insert(movie2)
        assertThat(movieId2, equalTo(TestUtilities.TEST_MOVIE1_ID))

        val movieList1 = TestUtilities.createMovieListValues(listId, movieId1, 0)
        val movieListId1 = movieListDao.insert(movieList1)
        assertThat(movieListId1, greaterThan(0L))
        movieList1.id = movieListId1

        val movieList2 = TestUtilities.createMovieListValues(listId, movieId2, 1)
        val movieListId2 = movieListDao.insert(movieList2)
        assertThat(movieListId2, greaterThan(0L))
        movieList2.id = movieListId2

        val movieLists = movieListDao.findByListId(listId)

        assertEquals("Error: No Records returned from movie list query", 2, movieLists.count())

        assertTrue("Error: Movie List Query Validation Failed", TestUtilities.compareMovieLists(movieList1, movieLists[0]))

        assertTrue("Error: Movie List Query Validation Failed", TestUtilities.compareMovieLists(movieList2, movieLists[1]))

        db.close()
    }
}
