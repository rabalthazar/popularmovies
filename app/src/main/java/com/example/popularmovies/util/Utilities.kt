package com.example.popularmovies.util

import android.content.Context
import android.preference.PreferenceManager

import com.example.popularmovies.R

object Utilities {
    fun getPreferredSelection(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_order_key), context.getString(R.string.pref_order_default))
    }
}
