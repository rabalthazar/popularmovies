package com.example.popularmovies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Model class for a movie
 * Created by rafael on 23/05/16.
 */
@Entity(tableName = "movie")
data class Movie(
    /**
     * TMDB movie id
     */
    @PrimaryKey
    @ColumnInfo(name = "_id")
    var id: Long,

    /**
     * Movie title
     */
    var title: String,

    /**
     * Movie release date
     */
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    var releaseDate: Date?,

    /**
     * Poster path in TMDB
     */
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String,

    /**
     * Movie overview
     */
    var overview: String,

    /**
     * Adult rated?
     */
    var adult: Boolean,

    /**
     * User rating
     */
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    var voteAverage: Double
) : Serializable
