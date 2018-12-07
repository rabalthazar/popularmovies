package com.example.popularmovies.util

import com.example.popularmovies.model.Movie
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper Factory class to convert movies to and from the various formats used in the app
 * Created by rafael on 23/05/16.
 */
object MovieFactory {
    private const val MOVIE_ID = "id"
    private const val MOVIE_TITLE = "title"
    private const val MOVIE_OVERVIEW = "overview"
    private const val MOVIE_RELEASE = "release_date"
    private const val MOVIE_ADULT = "adult"
    private const val MOVIE_POSTER = "poster_path"
    private const val MOVIE_RATING = "vote_average"

    /**
     * Creates a Movie object from a JSON in the TMDB format
     * @param movieJson Movie JSON object
     * @return Movie object
     * @throws JSONException if JSON can't be parsed
     */
    @Throws(JSONException::class)
    fun fromTMDBJsonObject(movieJson: JSONObject): Movie {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val releaseDate: Date? = try {
            format.parse(movieJson.getString(MOVIE_RELEASE))
        } catch (e: ParseException) {
            null
        }

        return Movie(
                movieJson.getLong(MOVIE_ID),
                movieJson.getString(MOVIE_TITLE),
                releaseDate,
                movieJson.getString(MOVIE_POSTER),
                movieJson.getString(MOVIE_OVERVIEW),
                movieJson.getBoolean(MOVIE_ADULT),
                movieJson.getDouble(MOVIE_RATING)
        )
    }
}
