package com.example.popularmovies.data.dao

import androidx.room.*
import com.example.popularmovies.model.List as ListModel

@Dao
interface ListDao {
    @Query("SELECT * FROM list")
    fun getAll(): List<ListModel>

    @Query("SELECT * FROM list WHERE selection = :selection")
    fun findBySelection(selection: String): ListModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: ListModel): Long

    @Delete
    fun delete(list: ListModel)
}