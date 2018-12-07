package com.example.popularmovies.data

import androidx.room.TypeConverter
import java.util.*


class DateTypeConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return if (value == null) null else value.getTime()
    }
}