package com.example.popularmovies.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Content fetcher
 * Created by rafael on 23/05/16.
 */
public class FetchUri {
    private static String LOG_TAG = FetchUri.class.getSimpleName();

    /**
     * Fetchs the text content from an URI via HTTP
     * @param uri URI Object wiht the
     * @return
     */
    @Nullable
    public static String fetch(Uri uri) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        try {
            URL url = new URL(uri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getMessage(), e);
        } finally {
            if (connection == null) {
                return null;
            }
            connection.disconnect();
            if(reader == null) {
                return null;
            }
            try {
                reader.close();
            } catch (IOException e) {
                Log.d(LOG_TAG, e.getMessage(), e);
            }
        }

        return buffer.toString();
    }
}
