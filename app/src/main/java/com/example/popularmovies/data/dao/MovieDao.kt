package com.example.popularmovies.data.dao

import androidx.room.*
import com.example.popularmovies.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE _id = :movieId")
    fun loadById(movieId: Long): Movie?

    @Query("SELECT * FROM movie WHERE _id IN (:movieIds)")
    fun loadByIds(movieIds: LongArray): List<Movie>

    @Query("SELECT * FROM movie m WHERE m._id NOT IN (SELECT ml.movie_id FROM movie_list ml WHERE ml.movie_id = m._id)")
    fun findOrphans(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Long

    @Delete
    fun delete(movie: Movie)
}
