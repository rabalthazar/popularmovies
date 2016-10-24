package com.example.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.example.popularmovies.model.List;

/**
 * A content provider for movies and list of movies
 * Created by rafael on 01/07/16.
 */
public class MoviesProvider extends ContentProvider {
    /**
     * Constant for list requests
     */
    public static final int LIST = 100;

    /**
     * Constant for movie list requests (ordered by popularity by default)
     */
    public static final int LIST_BY_ID = 101;

    /**
     * Constant for specific movie requests
     */
    public static final int MOVIE = 200;

    /**
     * Constant for specific movie requests
     */
    public static final int MOVIE_BY_ID = 201;

    public static final int MOVIES_BY_SELECTION = 202;


    public static final int MOVIE_LIST = 300;

    public static final int MOVIE_LIST_BY_ID = 301;

    /**
     * Static UriMatcher for the provider
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mDbOpener;

    private static final String BY_ID_SELECTION = BaseColumns._ID + "=?";

    private static final SQLiteQueryBuilder sMoviesByListQueryBuilder = new SQLiteQueryBuilder();

    private static final String sMovieListSelectionByOrder =
            MoviesContract.MovieListEntry.COLUMN_LIST_KEY + "=?";

    private static final String sMovieOrder = MoviesContract.MovieEntry.COLUMN_VOTE_AVG + " DESC";

    static {
        sMoviesByListQueryBuilder.setTables(
                MoviesContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                MoviesContract.MovieListEntry.TABLE_NAME + " ON " +
                MoviesContract.MovieEntry._ID + "=" + MoviesContract.MovieListEntry.COLUMN_MOVIE_KEY +
                " INNER JOIN " + MoviesContract.ListEntry.TABLE_NAME + " ON " +
                MoviesContract.ListEntry._ID + "=" + MoviesContract.MovieListEntry.COLUMN_LIST_KEY
        );
    }

    /**
     * Builds an UriMatcher for the provider
     * @return An UriMatcher
     */
    public static UriMatcher buildUriMatcher() {
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(authority, MoviesContract.LIST_LOCATION, LIST);
        uriMatcher.addURI(authority, MoviesContract.LIST_LOCATION + "/#", LIST_BY_ID);
        uriMatcher.addURI(authority, MoviesContract.MOVIE_LOCATION, MOVIE);
        uriMatcher.addURI(authority, MoviesContract.MOVIE_LOCATION + "/#", MOVIE_BY_ID);
        uriMatcher.addURI(authority, MoviesContract.MOVIE_LOCATION + "/*", MOVIES_BY_SELECTION);
        uriMatcher.addURI(authority, MoviesContract.MOVIE_LIST_LOCATION, MOVIE_LIST);
        uriMatcher.addURI(authority, MoviesContract.MOVIE_LIST_LOCATION + "/#", MOVIE_LIST_BY_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbOpener = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = mDbOpener.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        switch(match) {
            case LIST:
                retCursor = db.query(
                        MoviesContract.ListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LIST_BY_ID:
                Long listId = MoviesContract.ListEntry.getIdFromUri(uri);
                retCursor = db.query(
                        MoviesContract.ListEntry.TABLE_NAME,
                        projection,
                        BY_ID_SELECTION,
                        new String[] {Long.toString(listId)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE:
                retCursor = db.query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_BY_ID:
                retCursor = getMovieById(uri, projection);
                break;
            case MOVIE_LIST:
                retCursor = db.query(
                        MoviesContract.MovieListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_LIST_BY_ID:
                Long movieListId = MoviesContract.MovieListEntry.getIdFromUri(uri);
                retCursor = db.query(
                        MoviesContract.MovieListEntry.TABLE_NAME,
                        projection,
                        BY_ID_SELECTION,
                        new String[] {Long.toString(movieListId)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown query uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case LIST:
                return MoviesContract.ListEntry.CONTENT_TYPE;
            case LIST_BY_ID:
                return MoviesContract.ListEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return MoviesContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_BY_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIES_BY_SELECTION:
                return MoviesContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_LIST:
                return MoviesContract.MovieListEntry.CONTENT_TYPE;
            case MOVIE_LIST_BY_ID:
                return MoviesContract.MovieListEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown content URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mDbOpener.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case LIST:
                Long _id = database.insert(MoviesContract.ListEntry.TABLE_NAME, null, values);
                if (_id <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                returnUri = MoviesContract.ListEntry.buildUri(_id);
                break;
            case MOVIE:
                _id = database.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
                if (_id <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                returnUri = MoviesContract.MovieEntry.buildUri(_id);
                break;
            case MOVIE_LIST:
                _id = database.insert(MoviesContract.MovieListEntry.TABLE_NAME, null, values);
                if (_id <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                returnUri = MoviesContract.MovieListEntry.buildUri(_id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbOpener.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case LIST:
                rowsDeleted = db.delete(MoviesContract.ListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_LIST:
                rowsDeleted = db.delete(MoviesContract.MovieListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private Cursor getMovieById(Uri uri, String[] projection) {
        Long id = MoviesContract.MovieEntry.getIdFromUri(uri);
        return mDbOpener.getReadableDatabase().query(
                MoviesContract.MovieEntry.TABLE_NAME,
                projection,
                BY_ID_SELECTION,
                new String[] { Long.toString(id) },
                null,
                null,
                null
        );
    }

    private Cursor getMovies(String[] projection, @Nullable String listSelector, @Nullable  String sortOrder) {
        SQLiteDatabase db = mDbOpener.getReadableDatabase();
        if (null == listSelector) listSelector = List.POPULAR;
        if (null == sortOrder) sortOrder = sMovieOrder;
        return sMoviesByListQueryBuilder.query(
                db,
                projection,
                BY_ID_SELECTION,
                new String[] { listSelector },
                null,
                null,
                sortOrder
        );
    }
}
