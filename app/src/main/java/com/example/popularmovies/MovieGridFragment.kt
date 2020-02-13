package com.example.popularmovies

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.popularmovies.data.MoviesViewModel
import com.example.popularmovies.model.Movie
import com.example.popularmovies.util.MovieArrayAdapter
import kotlinx.android.synthetic.main.fragment_movie_grid.*

/**
 * Displays a grid with a list of movie posters
 */
class MovieGridFragment : Fragment() {

    /**
     * The adapter that feeds the movie grid
     */
    private lateinit var mMoviesAdapter: MovieArrayAdapter

    private var mForceFetch: Boolean = false

    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        activity?.run {
            viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
            mMoviesAdapter = MovieArrayAdapter(this, R.id.moviesGrid)
            viewModel.data.observe(this, Observer<List<Movie>> {
                mMoviesAdapter.clear()
                mMoviesAdapter.addAll(it)
            })
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_movie_grid, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData(mForceFetch)
        // Get a reference to the GridView, and attach this adapter to it.
        moviesGrid.adapter = mMoviesAdapter
        moviesGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val bundle = Bundle()
            bundle.putInt("moviePos", position)
            view.findNavController().navigate(R.id.movieDetailAction, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movies_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                mForceFetch = true
                onSelectionChanged()
                true
            }
            R.id.action_settings -> {
                findNavController().navigate(R.id.settingsAction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onSelectionChanged() {
        viewModel.loadData(mForceFetch)
        mForceFetch = false
    }
}
