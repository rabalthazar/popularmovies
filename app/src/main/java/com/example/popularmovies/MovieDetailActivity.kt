package com.example.popularmovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
