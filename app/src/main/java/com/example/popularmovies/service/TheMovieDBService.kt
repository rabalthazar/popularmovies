package com.example.popularmovies.service

import com.example.popularmovies.service.data.MoviesResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TheMovieDBService {
    @GET("movie/{selection}")
    fun getMovies(@Path("selection") selection: String, @Query("api_key") apiKey: String): Call<MoviesResult>
}