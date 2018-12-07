package com.example.popularmovies.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.popularmovies.data.dao.ListDao
import com.example.popularmovies.data.dao.MovieDao
import com.example.popularmovies.data.dao.MovieListDao
import com.example.popularmovies.model.Movie
import com.example.popularmovies.model.MovieList
import com.example.popularmovies.model.List as ListModel

@Database(entities = [Movie::class, ListModel::class, MovieList::class], version = 3)
@TypeConverters(DateTypeConverter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun listDao(): ListDao
    abstract fun movieListDao(): MovieListDao
}