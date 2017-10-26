package com.example.popularmovies.util

import android.net.Uri
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Content fetcher
 * Created by rafael on 23/05/16.
 */
object UriFetcher {
    private val LOG_TAG: String
        get() = UriFetcher::class.java.simpleName

    /**
     * Fetchs the text content from an URI via HTTP
     * @param uri URI to be fetched
     * @return the fetched contents of the URI
     */
    fun fetch(uri: Uri): String? {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        val buffer = StringBuilder()
        try {
            val url = URL(uri.toString())

            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val inputStream = connection.inputStream ?: return null
            reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                buffer.append(line).append("\n")
                line = reader.readLine()
            }
            if (buffer.length == 0) {
                return null
            }
        } catch (e: IOException) {
            Log.d(LOG_TAG, e.message, e)
        }

        if (connection == null) {
            return null
        }
        connection.disconnect()
        if (reader == null) {
            return null
        }
        try {
            reader.close()
        } catch (e: IOException) {
            Log.d(LOG_TAG, e.message, e)
        }

        return buffer.toString()
    }
}
