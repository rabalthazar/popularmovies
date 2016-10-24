package com.example.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by rafael on 22/10/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestProvider {
    private static Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(MoviesContract.MovieListEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(MoviesContract.ListEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, null, null);
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
        Cursor movieCursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Movie table is not empty", 0, movieCursor.getCount());
        Cursor movieListCursor = contentResolver.query(MoviesContract.MovieListEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Movie List table is not empty", 0, movieListCursor.getCount());
    }
}
