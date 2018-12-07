package com.example.popularmovies.data

import android.content.Context
import androidx.room.Room

/**
 * A SQLite helper to create, update and instance the app's database
 * Created by rafael on 24/06/16.
 */
object MoviesDbHelper {
    const val DATABASE_NAME = "movies"

    var database: MoviesDatabase? = null

    fun getDatabase(context: Context): MoviesDatabase {
        var localDatabase = database
        if (localDatabase == null) {
            localDatabase = Room.databaseBuilder(context.applicationContext, MoviesDatabase::class.java, DATABASE_NAME).build()
            database = localDatabase
        }
        return localDatabase
    }
}
