package com.example.popularmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.popularmovies.data.MoviesViewModel
import com.example.popularmovies.databinding.FragmentMovieDetailBinding
import com.example.popularmovies.model.Movie
import com.example.popularmovies.util.ImageLoader
import java.text.NumberFormat

/**
 * Shows a movie details as original title, release date, movie overview, etc.
 */
class MovieDetailFragment : Fragment() {
    private lateinit var viewModel: MoviesViewModel

    private var _binding: FragmentMovieDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]

        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moviePos = (arguments?.getInt("moviePos")) ?: return
        viewModel.data.observe(viewLifecycleOwner) { movieList ->
            movieList?.get(moviePos)?.let { showMovieDetails(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMovieDetails(movie: Movie) {
        binding.movieTitle.text = movie.title
        binding.movieOverview.text = movie.overview
        binding.movieRating.text = NumberFormat.getNumberInstance().format(movie.voteAverage)
        context?.let {
            ImageLoader.loadFromPosterPath(movie.posterPath, binding.moviePoster)
            val releaseDate = movie.releaseDate
            val dateFormat = android.text.format.DateFormat.getDateFormat(it)
            binding.movieRelease.text = if (releaseDate != null) dateFormat.format(releaseDate) else it.getString(R.string.date_placeholder)
        }
    }
}
