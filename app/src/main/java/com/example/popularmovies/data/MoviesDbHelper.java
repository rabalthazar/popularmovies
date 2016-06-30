package com.example.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.popularmovies.data.MoviesContract.MovieEntry;
import com.example.popularmovies.data.MoviesContract.ListEntry;
import com.example.popularmovies.data.MoviesContract.MovieListEntry;
import com.example.popularmovies.model.Movie;

/**
 * A SQLite helper to create, update and instance the app's database
 * Created by rafael on 24/06/16.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {
    /**
     * The current database version. Must be increased every time the db structure changes
     */
    private static final Integer DATABASE_VERSION = 1;

    /**
     * The database name
     */
    public static final String DATABASE_NAME = "movies.db";

    /**
     * Object constructor
     * @param context The app context
     */
    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is invoked whenever the app needs to create a new database
     * @param db Database handler
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Movie table creation command
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL," +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL," +
                MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL" +
                " );";

        // List table creation command
        final String SQL_CREATE_LIST_TABLE = "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ListEntry.COLUMN_ORDER + " TEXT NOT NULL," +
                ListEntry.COLUMN_DATE_FETCHED + " INTEGER NOT NULL" +
                " );";

        // Movies and lists relation table creation command
        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " + MovieListEntry.TABLE_NAME + " (" +
                MovieListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieListEntry.COLUMN_LIST_KEY + " INTEGER NOT NULL," +
                MovieListEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL," +
                MovieListEntry.COLUMN_ORDER + " INTEGER NOT NULL," +
                " FOREIGN KEY (" + MovieListEntry.COLUMN_LIST_KEY + ") REFERENCES " +
                ListEntry.TABLE_NAME + "(" + ListEntry._ID + ")," +
                " FOREIGN KEY (" + MovieListEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + "(" + MovieEntry._ID + ")" +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_LIST_TABLE);
        db.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    /**
     * This method is called whenever the database version is changed.
     * As the database is just a cache, just drop the tables and recreate them.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieListEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
