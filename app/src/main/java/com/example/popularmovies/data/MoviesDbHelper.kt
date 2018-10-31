package com.example.popularmovies.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.popularmovies.data.MoviesContract.*

/**
 * A SQLite helper to create, update and instance the app's database
 * Created by rafael on 24/06/16.
 */
class MoviesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Movie table creation command
    private val SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY," +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
            MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
            MovieEntry.COLUMN_RELEASE_DATE + " INTEGER," +
            MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
            MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL," +
            MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL" +
            " );"

    // List table creation command
    private val SQL_CREATE_LIST_TABLE = "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
            ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ListEntry.COLUMN_SELECTION + " TEXT NOT NULL," +
            ListEntry.COLUMN_DATE_FETCHED + " INTEGER NOT NULL" +
            " );"

    // Movies and lists relation table creation command
    private val SQL_CREATE_MOVIE_LIST_TABLE = "CREATE TABLE " + MovieListEntry.TABLE_NAME + " (" +
            MovieListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MovieListEntry.COLUMN_LIST_KEY + " INTEGER NOT NULL," +
            MovieListEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL," +
            MovieListEntry.COLUMN_ORDER + " INTEGER NOT NULL," +
            " FOREIGN KEY (" + MovieListEntry.COLUMN_LIST_KEY + ") REFERENCES " +
            ListEntry.TABLE_NAME + "(" + ListEntry._ID + ")," +
            " FOREIGN KEY (" + MovieListEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
            MovieEntry.TABLE_NAME + "(" + MovieEntry._ID + ")" +
            " );"

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(SQL_CREATE_MOVIE_TABLE)
        db.execSQL(SQL_CREATE_LIST_TABLE)
        db.execSQL(SQL_CREATE_MOVIE_LIST_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieListEntry.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        /**
         * The current database version. Must be increased every time the db structure changes
         */
        private const val DATABASE_VERSION = 2

        /**
         * The database name
         */
        const val DATABASE_NAME = "movies.db"
    }
}
