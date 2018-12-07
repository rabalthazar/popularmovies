package com.example.popularmovies.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.popularmovies.model.Movie

class MoviesLiveData(val application: Application): LiveData<List<Movie>>() {
    fun load(forceFetch: Boolean) {
        val loader = MoviesLoader(application, forceFetch, this)
        loader.execute()
    }

    fun setValueExternally(movies: List<Movie>)
    {
        value = movies
    }
}