package com.example.popularmovies.util

import android.os.Bundle
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
        val releaseDateTs = movie.releaseDate?.time
        if (releaseDateTs !== null) {
            movieBundle.putLong(MOVIE_RELEASE, releaseDateTs)
        }
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
        val releaseDateTs = if (movieBundle.containsKey(MOVIE_RELEASE)) movieBundle.getLong(MOVIE_RELEASE) else null
        return Movie(
                movieBundle.getLong(MOVIE_ID),
                movieBundle.getString(MOVIE_TITLE)!!,
                if (releaseDateTs !== null ) Date(releaseDateTs) else null,
                movieBundle.getString(MOVIE_POSTER)!!,
                movieBundle.getString(MOVIE_OVERVIEW)!!,
                movieBundle.getBoolean(MOVIE_ADULT),
                movieBundle.getDouble(MOVIE_RATING))
    }
}
