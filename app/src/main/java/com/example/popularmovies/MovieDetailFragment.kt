package com.example.popularmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.popularmovies.data.MoviesViewModel
import com.example.popularmovies.model.Movie
import com.example.popularmovies.util.ImageLoader
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import java.text.NumberFormat

/**
 * Shows a movie details as original title, release date, movie overview, etc.
 */
class MovieDetailFragment : Fragment() {
    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

        activity?.run {
            viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moviePos = (arguments?.getInt("moviePos")) ?: return
        viewModel.data.observe(this, Observer<List<Movie>?> { movieList ->
            movieList?.get(moviePos)?.let { showMovieDetails(it) }
        })
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
