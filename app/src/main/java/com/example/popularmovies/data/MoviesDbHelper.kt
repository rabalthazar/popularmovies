package com.example.popularmovies.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * A SQLite helper to create, update and instance the app's database
 * Created by rafael on 24/06/16.
 */
object MoviesDbHelper {
    private const val DATABASE_NAME = "movies"

    private var database: MoviesDatabase? = null

    fun getDatabase(context: Context): MoviesDatabase {
        var localDatabase = database
        if (localDatabase == null) {
            localDatabase = Room.databaseBuilder(context.applicationContext, MoviesDatabase::class.java, DATABASE_NAME)
                    .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
                    .build()
            database = localDatabase
        }
        return localDatabase
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE movie_list ADD COLUMN \"order\" INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE INDEX index_movie_list_movie_id ON movie_list(movie_id)")
        db.execSQL("CREATE INDEX index_movie_list_list_id ON movie_list(list_id)")
    }
}