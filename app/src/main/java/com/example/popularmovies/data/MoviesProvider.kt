package com.example.popularmovies.data

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.provider.BaseColumns

/**
 * A content provider for movies and list of movies
 * Created by rafael on 01/07/16.
 */
class MoviesProvider : ContentProvider() {

    private var mDbOpener: MoviesDbHelper? = null

    override fun onCreate(): Boolean {
        mDbOpener = MoviesDbHelper(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val db = mDbOpener?.readableDatabase ?: return null

        val match = sUriMatcher.match(uri)
        val retCursor: Cursor? = when (match) {
            LIST -> db.query(
                    MoviesContract.ListEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            LIST_BY_ID -> {
                val listId = MoviesContract.ListEntry.getIdFromUri(uri)
                db.query(
                        MoviesContract.ListEntry.TABLE_NAME,
                        projection,
                        BY_ID_SELECTION,
                        arrayOf(java.lang.Long.toString(listId)), null, null,
                        sortOrder
                )
            }
            LIST_BY_SELECTION -> {
                val listSelection = MoviesContract.ListEntry.getSelectionFromUri(uri)
                db.query(
                        MoviesContract.ListEntry.TABLE_NAME,
                        projection,
                        LIST_SELECTION,
                        arrayOf(listSelection), null, null,
                        sortOrder
                )
            }
            MOVIE -> db.query(
                    MoviesContract.MovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            MOVIE_BY_ID -> getMovieById(uri, projection)
            MOVIES_BY_SELECTION -> {
                val listSelection = MoviesContract.MovieEntry.getSelectionFromUri(uri)
                getMovies(projection, listSelection, sortOrder)
            }
            MOVIE_LIST -> db.query(
                    MoviesContract.MovieListEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            MOVIE_LIST_BY_ID -> {
                val movieListId = MoviesContract.MovieListEntry.getIdFromUri(uri)
                db.query(
                        MoviesContract.MovieListEntry.TABLE_NAME,
                        projection,
                        BY_ID_SELECTION,
                        arrayOf(java.lang.Long.toString(movieListId!!)), null, null,
                        sortOrder
                )
            }
            MOVIE_LIST_BY_SELECTION -> {
                val listSelection = MoviesContract.MovieListEntry.getSelectionFromUri(uri)
                db.query(
                        MoviesContract.MovieListEntry.TABLE_NAME,
                        projection,
                        BY_LIST_SELECTION_SELECTION,
                        arrayOf(listSelection), null, null,
                        sortOrder
                )
            }
            else -> throw UnsupportedOperationException("Unknown query uri: " + uri)
        }
        val contentResolver = context?.contentResolver
        if (contentResolver != null) {
            retCursor?.setNotificationUri(contentResolver, uri)
        }
        return retCursor
    }

    override fun getType(uri: Uri): String {
        val match = sUriMatcher.match(uri)

        return when (match) {
            LIST -> MoviesContract.ListEntry.CONTENT_TYPE
            LIST_BY_ID -> MoviesContract.ListEntry.CONTENT_ITEM_TYPE
            LIST_BY_SELECTION -> MoviesContract.ListEntry.CONTENT_ITEM_TYPE
            MOVIE -> MoviesContract.MovieEntry.CONTENT_TYPE
            MOVIE_BY_ID -> MoviesContract.MovieEntry.CONTENT_ITEM_TYPE
            MOVIES_BY_SELECTION -> MoviesContract.MovieEntry.CONTENT_TYPE
            MOVIE_LIST -> MoviesContract.MovieListEntry.CONTENT_TYPE
            MOVIE_LIST_BY_ID -> MoviesContract.MovieListEntry.CONTENT_ITEM_TYPE
            MOVIE_LIST_BY_SELECTION -> MoviesContract.MovieListEntry.CONTENT_TYPE
            else -> throw UnsupportedOperationException("Unknown content URI: " + uri)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val database = mDbOpener?.writableDatabase ?: return null
        val match = sUriMatcher.match(uri)
        return when (match) {
            LIST -> {
                val _id = database.insert(MoviesContract.ListEntry.TABLE_NAME, null, values)
                if (_id <= 0) {
                    throw SQLException("Failed to insert row into " + uri)
                }
                MoviesContract.ListEntry.buildUri(_id)
            }
            MOVIE -> {
                val _id = database.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values)
                if (_id <= 0) {
                    throw SQLException("Failed to insert row into " + uri)
                }
                MoviesContract.MovieEntry.buildUri(_id)
            }
            MOVIE_LIST -> {
                val _id = database.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, values)
                if (_id <= 0) {
                    throw SQLException("Failed to insert row into " + uri)
                }
                MoviesContract.MovieListEntry.buildUri(_id)
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val db = mDbOpener?.writableDatabase ?: return -1
        val match = sUriMatcher.match(uri)
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1"
        val rowsDeleted: Int = when (match) {
            LIST -> db.delete(MoviesContract.ListEntry.TABLE_NAME, selection, selectionArgs)
            MOVIE -> db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs)
            MOVIE_LIST -> db.delete(MoviesContract.MovieListEntry.TABLE_NAME, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
        val contentResolver = context?.contentResolver
        if (rowsDeleted > 0 && contentResolver != null) {
            contentResolver.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mDbOpener?.writableDatabase ?: return -1
        val match = sUriMatcher.match(uri)
        val rowsUpdated: Int = when (match) {
            LIST_BY_ID -> {
                val listId = MoviesContract.ListEntry.getIdFromUri(uri)
                db.update(
                        MoviesContract.ListEntry.TABLE_NAME,
                        values,
                        BY_ID_SELECTION,
                        arrayOf(java.lang.Long.toString(listId))
                )
            }
            MOVIE_BY_ID -> {
                val movieId = MoviesContract.MovieEntry.getIdFromUri(uri)
                db.update(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        values,
                        BY_ID_SELECTION,
                        arrayOf(java.lang.Long.toString(movieId!!))
                )
            }
            else -> throw UnsupportedOperationException("Unknown update uri: " + uri)
        }
        val contentResolver = context?.contentResolver
        if (rowsUpdated > 0 && contentResolver != null) {
            contentResolver.notifyChange(uri, null)
        }

        return rowsUpdated
    }

    private fun getMovieById(uri: Uri, projection: Array<String>?): Cursor? {
        val id = MoviesContract.MovieEntry.getIdFromUri(uri)
        return mDbOpener?.readableDatabase?.query(
                MoviesContract.MovieEntry.TABLE_NAME,
                projection,
                BY_ID_SELECTION,
                arrayOf(java.lang.Long.toString(id)), null, null, null
        )
    }

    private fun getMovies(projection: Array<String>?, listSelector: String?, sortOrder: String?): Cursor? {
        var listSelector = listSelector
        var sortOrder = sortOrder
        val db = mDbOpener?.readableDatabase ?: return null
        if (null == listSelector) {
            listSelector = MoviesContract.ListSelections.MOST_POPULAR.toString()
        }
        if (null == sortOrder) {
            sortOrder = sMovieOrder
        }
        return sMoviesByListQueryBuilder.query(
                db,
                projection,
                LIST_SELECTION,
                arrayOf(listSelector), null, null,
                sortOrder
        )
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        val match = sUriMatcher.match(uri)
        val db = mDbOpener?.writableDatabase ?: return -1
        val rowsDeleted: Int
        var rowsInserted = 0
        when (match) {
            MOVIE_LIST_BY_SELECTION -> {
                val listSelection = MoviesContract.MovieListEntry.getSelectionFromUri(uri)
                val listByIdUri = MoviesContract.ListEntry.buildBySelectionUri(listSelection)
                rowsDeleted = clearMovieListBySelection(listByIdUri)
                values
                        .map { db.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, it) }
                        .filter { it > 0 }
                        .forEach { rowsInserted++ }
                clearOrphanedMovies()
            }
            else -> throw UnsupportedOperationException("Unknown bulk insert uri: " + uri)
        }
        val contentResolver = context?.contentResolver
        if (rowsDeleted + rowsInserted > 0 && contentResolver != null) {
            contentResolver.notifyChange(uri, null)
        }
        return rowsInserted
    }

    private fun clearMovieListBySelection(listBySelectionUri: Uri): Int {
        val listCursor = query(listBySelectionUri, null, null, null, null) ?: return 0
        if (!listCursor.moveToFirst()) {
            listCursor.close()
            return 0
        }
        val idIndex = listCursor.getColumnIndex(MoviesContract.ListEntry._ID)
        val listId = listCursor.getLong(idIndex)
        listCursor.close()
        val db = mDbOpener?.writableDatabase ?: return 0
        val byListIdSelection = MoviesContract.MovieListEntry.COLUMN_LIST_KEY + "=?"
        return db.delete(
                MoviesContract.MovieListEntry.TABLE_NAME,
                byListIdSelection,
                arrayOf(java.lang.Long.toString(listId))
        )
    }

    private fun clearOrphanedMovies(): Int {
        val db = mDbOpener?.writableDatabase ?: return 0
        val orphanedSelection = MoviesContract.MovieEntry._ID + " NOT IN (SELECT " +
                MoviesContract.MovieListEntry.TABLE_NAME + "." + MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY +
                " FROM " + MoviesContract.MovieListEntry.TABLE_NAME + ")"
        return db.delete(MoviesContract.MovieEntry.TABLE_NAME, orphanedSelection, null)
    }

    companion object {
        /**
         * Constant for list requests
         */
        val LIST = 100

        /**
         * Constant for movie list requests (ordered by popularity by default)
         */
        val LIST_BY_ID = 101

        val LIST_BY_SELECTION = 102

        /**
         * Constant for specific movie requests
         */
        val MOVIE = 200

        /**
         * Constant for specific movie requests
         */
        val MOVIE_BY_ID = 201

        val MOVIES_BY_SELECTION = 202


        val MOVIE_LIST = 300

        val MOVIE_LIST_BY_ID = 301

        val MOVIE_LIST_BY_SELECTION = 302

        /**
         * Static UriMatcher for the provider
         */
        private val sUriMatcher = buildUriMatcher()

        private val BY_ID_SELECTION = BaseColumns._ID + "=?"

        private val LIST_SELECTION = MoviesContract.ListEntry.TABLE_NAME + "." +
                MoviesContract.ListEntry.COLUMN_SELECTION + "=?"

        private val BY_LIST_SELECTION_SELECTION = MoviesContract.MovieListEntry.COLUMN_LIST_KEY +
                " IN (SELECT " + MoviesContract.ListEntry.TABLE_NAME + "." + MoviesContract.ListEntry._ID +
                " FROM " + MoviesContract.ListEntry.TABLE_NAME + " WHERE " + MoviesContract.ListEntry.TABLE_NAME +
                "." + MoviesContract.ListEntry.COLUMN_SELECTION + "=?)"

        private val sMovieOrder = MoviesContract.MovieEntry.COLUMN_VOTE_AVG + " DESC"

        private val sMoviesByListQueryBuilder = SQLiteQueryBuilder()

        init {
            sMoviesByListQueryBuilder.tables = MoviesContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                    MoviesContract.MovieListEntry.TABLE_NAME + " ON " +
                    MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID + "=" +
                    MoviesContract.MovieListEntry.TABLE_NAME + "." + MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY +
                    " INNER JOIN " + MoviesContract.ListEntry.TABLE_NAME + " ON " +
                    MoviesContract.ListEntry.TABLE_NAME + "." + MoviesContract.ListEntry._ID + "=" +
                    MoviesContract.MovieListEntry.TABLE_NAME + "." + MoviesContract.MovieListEntry.COLUMN_LIST_KEY
        }

        /**
         * Builds an UriMatcher for the provider
         * @return An UriMatcher
         */
        fun buildUriMatcher(): UriMatcher {
            val authority = MoviesContract.CONTENT_AUTHORITY
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(authority, MoviesContract.LIST_LOCATION, LIST)
            uriMatcher.addURI(authority, MoviesContract.LIST_LOCATION + "/#", LIST_BY_ID)
            uriMatcher.addURI(authority, MoviesContract.LIST_LOCATION + "/*", LIST_BY_SELECTION)
            uriMatcher.addURI(authority, MoviesContract.MOVIE_LOCATION, MOVIE)
            uriMatcher.addURI(authority, MoviesContract.MOVIE_LOCATION + "/#", MOVIE_BY_ID)
            uriMatcher.addURI(authority, MoviesContract.MOVIE_LOCATION + "/*", MOVIES_BY_SELECTION)
            uriMatcher.addURI(authority, MoviesContract.MOVIE_LIST_LOCATION, MOVIE_LIST)
            uriMatcher.addURI(authority, MoviesContract.MOVIE_LIST_LOCATION + "/#", MOVIE_LIST_BY_ID)
            uriMatcher.addURI(authority, MoviesContract.MOVIE_LIST_LOCATION + "/*", MOVIE_LIST_BY_SELECTION)

            return uriMatcher
        }
    }
}
