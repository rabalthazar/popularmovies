package com.example.popularmovies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Holds a list of movies, given an determined order: most popular or top rated
 * Created by rafael on 23/06/16.
 */
@Entity(tableName = "list")
data class List(
        /**
         * Movie list id
         */
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "_id")
        var id: Long = 0L,

        /**
         * List order. Actually popular (@string/pref_order_popular) or top_rated (@string/pref_order_top_rated)
         */
        var selection: String = "",

        /**
         * Date the list was fetched
         */
        @ColumnInfo(name = "date_fetched")
        var dateFetched: Date = Date()
)