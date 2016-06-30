package com.example.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity that holds the movie detail fragment
 * @see MovieDetailFragment
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
    }
}
