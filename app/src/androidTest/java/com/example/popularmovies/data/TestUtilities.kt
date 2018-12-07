package com.example.popularmovies.data

import com.example.popularmovies.model.Movie
import com.example.popularmovies.model.MovieList
import com.example.popularmovies.util.MoviesFetcher
import java.util.*
import com.example.popularmovies.model.List as ListModel

internal object TestUtilities {
    private val TEST_LIST1_SELECTION = MoviesFetcher.ListSelections.MOST_POPULAR.toString()
    private const val TEST_LIST1_DATE = 1477326793L // Oct 24, 2016 14:33:13 (GMT-02)

    private val TEST_LIST2_SELECTION = MoviesFetcher.ListSelections.TOP_RATED.toString()
    private const val TEST_LIST2_DATE = 1477326979L // Oct 24, 2016 14:36:19 (GMT-02)

    const val TEST_MOVIE1_ID = 11L
    private const val TEST_MOVIE1_TITLE = "Star Wars"
    private const val TEST_MOVIE1_OVERVIEW = "Princess Leia is captured and held hostage " +
            "by the evil Imperial forces in their effort to take over the galactic Empire. " +
            "Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable " +
            "robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire."
    private const val TEST_MOVIE1_RELEASE = 227674800L // Mar 20, 1977
    private const val TEST_MOVIE1_VOTE_AVG = 7.9
    private const val TEST_MOVIE1_POSTER_PATH = "/tvSlBzAdRE29bZe5yYWrJ2ds137.jpg"

    const val TEST_MOVIE2_ID = 1891L
    private const val TEST_MOVIE2_TITLE = "The Empire Strikes Back"
    private const val TEST_MOVIE2_OVERVIEW = "The epic saga continues as Luke Skywalker, " +
            "in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from " +
            "aging master Yoda. But Darth Vader is more determined than ever to capture Luke. " +
            "Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO " +
            "and R2-D2 are thrown into various stages of capture, betrayal and despair."
    private const val TEST_MOVIE2_RELEASE = 327380400L // May 17, 1980
    private const val TEST_MOVIE2_VOTE_AVG = 5.8
    private const val TEST_MOVIE2_POSTER_PATH = "/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg"

    const val TEST_MOVIE3_ID = 1892L
    private const val TEST_MOVIE3_TITLE = "Return of the Jedi"
    private const val TEST_MOVIE3_OVERVIEW = "As Rebel leaders map their strategy for " +
            "an all-out attack on the Emperor's newer, bigger Death Star. Han Solo remains frozen " +
            "in the cavernous desert fortress of Jabba the Hutt, the most loathsome outlaw in " +
            "the universe, who is also keeping Princess Leia as a slave girl. Now a master of the " +
            "Force, Luke Skywalker rescues his friends, but he cannot become a true Jedi Knight " +
            "until he wages his own crucial battle against Darth Vader, who has sworn to win Luke " +
            "over to the dark side of the Force."
    private const val TEST_MOVIE3_RELEASE = 422506800L // May 23, 1983
    private const val TEST_MOVIE3_VOTE_AVG = 7.7
    private const val TEST_MOVIE3_POSTER_PATH = "/ghd5zOQnDaDW1mxO7R5fXXpZMu.jpg"

    fun createMostPopularListValues(): ListModel =
        ListModel(0, TEST_LIST1_SELECTION, Date(TEST_LIST1_DATE))


    fun createTopRatedListValues(): ListModel =
        ListModel(0, TEST_LIST2_SELECTION, Date(TEST_LIST2_DATE))


    fun createStarWarsValues(): Movie =
            Movie(
                    TEST_MOVIE1_ID, TEST_MOVIE1_TITLE, Date(TEST_MOVIE1_RELEASE), TEST_MOVIE1_POSTER_PATH,
                    TEST_MOVIE1_OVERVIEW, false, TEST_MOVIE1_VOTE_AVG
            )

    fun createEmpireStrikesBackValues(): Movie =
            Movie(
                    TEST_MOVIE2_ID, TEST_MOVIE2_TITLE, Date(TEST_MOVIE2_RELEASE), TEST_MOVIE2_POSTER_PATH,
                    TEST_MOVIE2_OVERVIEW, false, TEST_MOVIE2_VOTE_AVG
            )

    fun createReturnOfTheJediValues(): Movie =
            Movie(
                    TEST_MOVIE3_ID, TEST_MOVIE3_TITLE, Date(TEST_MOVIE3_RELEASE), TEST_MOVIE3_POSTER_PATH,
                    TEST_MOVIE3_OVERVIEW, false, TEST_MOVIE3_VOTE_AVG
            )

    fun createMovieListValues(listId: Long, movieId: Long, order: Int): MovieList =
            MovieList(0, listId, movieId, order)

    fun compareMovies(movie1: Movie, movie2: Movie): Boolean =
            movie1.id == movie2.id &&
            movie1.title == movie2.title &&
            movie1.overview == movie2.overview &&
            movie1.releaseDate == movie2.releaseDate &&
            movie1.posterPath == movie2.posterPath &&
            movie1.voteAverage == movie2.voteAverage

    fun compareLists(list1: ListModel, list2: ListModel): Boolean =
            list1.id == list2.id &&
            list1.selection == list2.selection &&
            list1.dateFetched.time == list2.dateFetched.time

    fun compareMovieLists(movieList1: MovieList, movieList2: MovieList): Boolean =
            movieList1.id == movieList2.id &&
            movieList1.listId == movieList2.listId &&
            movieList1.movieId == movieList2.movieId &&
            movieList1.order == movieList2.order


//    fun validateCurrentRecord(error: String, valueCursor: Cursor, expectedValues: ContentValues) {
//        expectedValues.valueSet().forEach { (columnName, value) ->
//            val idx = valueCursor.getColumnIndex(columnName)
//            assertFalse("Column '$columnName' not found. $error", idx == -1)
//            val expectedValue = value.toString()
//            assertEquals("Value '" + valueCursor.getString(idx) +
//                    "' did not match the expected value '" +
//                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx))
//        }
//    }
}
