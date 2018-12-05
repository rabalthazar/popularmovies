package com.example.popularmovies

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.popularmovies.util.Utilities
import kotlinx.android.synthetic.main.activity_movie_grid.*

/**
 * Holds the movie grid fragment
 * @see MovieGridFragment
 */
class MovieGridActivity : AppCompatActivity() {
    private var mSelectionOrder: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_grid)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.movie_grid_fragment, MovieGridFragment(), MovieGridFragment.FRAGMENT_TAG)
                    .commit()
        }

        mSelectionOrder = Utilities.getPreferredSelection(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val selection = Utilities.getPreferredSelection(this)
        if (selection != mSelectionOrder) {
            val movieGridFragment = supportFragmentManager
                    .findFragmentByTag(MovieGridFragment.FRAGMENT_TAG) as MovieGridFragment
            movieGridFragment.onSelectionChanged()
            mSelectionOrder = selection
        }
    }
}
