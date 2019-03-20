package com.example.popularmovies.model

import androidx.room.*
import com.example.popularmovies.model.List as ListModel


@Entity(
        tableName = "movie_list",
        foreignKeys = [
                ForeignKey(entity = ListModel::class, parentColumns = arrayOf("_id"), childColumns = arrayOf("list_id")),
                ForeignKey(entity = Movie::class, parentColumns = arrayOf("_id"), childColumns = arrayOf("movie_id"))
        ],
        indices = [Index(value = arrayOf("list_id")), Index(value = arrayOf("movie_id"))]
)
data class MovieList(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "_id")
        var id: Long = 0L,

        @ColumnInfo(name = "list_id")
        var listId: Long = 0L,

        @ColumnInfo(name = "movie_id")
        var movieId: Long = 0L,

        var order: Int = 0
)