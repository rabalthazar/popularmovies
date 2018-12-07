package com.example.popularmovies.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MoviesViewModel(application: Application): AndroidViewModel(application) {
    val data = MoviesLiveData(application)

    fun loadData(forceFetch: Boolean) {
        data.load(forceFetch)
    }
}