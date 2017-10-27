package com.example.popularmovies.util

import android.os.Bundle

import com.example.popularmovies.model.Movie

import org.json.JSONException
import org.json.JSONObject

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Helper Factory class to convert movies to and from the various formats used in the app
 * Created by rafael on 23/05/16.
 */
object MovieFactory {
    val MOVIE_ID = "id"
    val MOVIE_TITLE = "title"
    val MOVIE_OVERVIEW = "overview"
    val MOVIE_RELEASE = "release_date"
    val MOVIE_ADULT = "adult"
    val MOVIE_POSTER = "poster_path"
    val MOVIE_RATING = "vote_average"

    /**
     * Creates a Movie object from a JSON in the TMDB format
     * @param movieJson Movie JSON object
     * @return Movie object
     * @throws JSONException if JSON can't be parsed
     */
    @Throws(JSONException::class)
    fun fromTMDBJsonObject(movieJson: JSONObject): Movie {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var releaseDate: Date?
        try {
            releaseDate = format.parse(movieJson.getString(MOVIE_RELEASE))
        } catch (e: ParseException) {
            releaseDate = null
        }

        return Movie(
                movieJson.getLong(MOVIE_ID),
                movieJson.getString(MOVIE_TITLE),
                releaseDate!!,
                movieJson.getString(MOVIE_POSTER),
                movieJson.getString(MOVIE_OVERVIEW),
                movieJson.getBoolean(MOVIE_ADULT),
                movieJson.getDouble(MOVIE_RATING)
        )
    }

    /**
     * Creates a Bundle with a movie data that can be put into Intents
     * @param movie Movie object
     * @return Bundle object with Movie data
     */
    fun toBundle(movie: Movie): Bundle {
        val movieBundle = Bundle()
        movieBundle.putLong(MOVIE_ID, movie.id)
        movieBundle.putString(MOVIE_TITLE, movie.title)
        movieBundle.putString(MOVIE_OVERVIEW, movie.overview)
        movieBundle.putLong(MOVIE_RELEASE, movie.releaseDate.time)
        movieBundle.putBoolean(MOVIE_ADULT, movie.adult)
        movieBundle.putString(MOVIE_POSTER, movie.posterPath)
        movieBundle.putDouble(MOVIE_RATING, movie.voteAverage)
        return movieBundle
    }

    /**
     * Creates a Movie object from a movie Bundle
     * @param movieBundle The movie Bundle
     * @return A Movie object with the Bundle data
     */
    fun fromBundle(movieBundle: Bundle): Movie {
        return Movie(
                movieBundle.getLong(MOVIE_ID),
                movieBundle.getString(MOVIE_TITLE)!!,
                Date(movieBundle.getLong(MOVIE_RELEASE)),
                movieBundle.getString(MOVIE_POSTER)!!,
                movieBundle.getString(MOVIE_OVERVIEW)!!,
                movieBundle.getBoolean(MOVIE_ADULT),
                movieBundle.getDouble(MOVIE_RATING))
    }
}
