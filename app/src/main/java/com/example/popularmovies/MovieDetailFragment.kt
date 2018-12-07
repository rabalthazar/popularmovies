package com.example.popularmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.popularmovies.model.Movie
import com.example.popularmovies.util.ImageLoader
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import java.text.NumberFormat

/**
 * Shows a movie details as original title, release date, movie overview, etc.
 */
class MovieDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.intent?.getSerializableExtra("MOVIE_DATA")?.let {
            showMovieDetails(it as Movie)
        }
    }
    private fun showMovieDetails(movie: Movie) {
        movieTitle.text = movie.title
        movieOverview.text = movie.overview
        movieRating.text = NumberFormat.getNumberInstance().format(movie.voteAverage)
        context?.let {
            ImageLoader.loadFromPosterPath(it, movie.posterPath, moviePoster)
            val releaseDate = movie.releaseDate
            val dateFormat = android.text.format.DateFormat.getDateFormat(it)
            movieRelease.text = if (releaseDate != null) dateFormat.format(releaseDate) else it.getString(R.string.date_placeholder)
        }
    }
}
