package com.example.popularmovies.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.popularmovies.model.MovieList

@Dao
interface MovieListDao {
    @Query("SELECT * FROM movie_list")
    fun getAll(): List<MovieList>

    @Query("SELECT * FROM movie_list WHERE list_id = :listId ORDER BY \"order\"")
    fun findByListId(listId: Long): List<MovieList>

    @Insert
    fun insert(movieList: MovieList): Long

    @Delete
    fun delete(vararg movieLists: MovieList)
}