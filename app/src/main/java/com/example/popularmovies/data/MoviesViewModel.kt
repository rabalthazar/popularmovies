package com.example.popularmovies.data

import android.app.Application
import androidx.lifecycle.*
import com.example.popularmovies.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application): AndroidViewModel(application) {
    val data: MutableLiveData<List<Movie>> = MutableLiveData()

    fun loadData(forceFetch: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val loader = MoviesLoader(getApplication(), forceFetch)
            val movies = loader.doLoad()
            data.postValue(movies);
        }
    }
}