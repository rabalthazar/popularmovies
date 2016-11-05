package com.example.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.popularmovies.R;

public class Utilities {
    public static String getPreferredSelection(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_order_key),
                context.getString(R.string.pref_order_default));

    }
}
