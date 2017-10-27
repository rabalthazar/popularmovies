package com.example.popularmovies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * Activity that holds the movie detail fragment
 * @see MovieDetailFragment
 */
class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
    }
}
