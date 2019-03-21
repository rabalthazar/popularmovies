package com.example.popularmovies

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_order_key)))
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val stringValue = newValue.toString()

        if (preference is ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            val prefIndex = preference.findIndexOfValue(stringValue)
            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.summary = stringValue
        }
        return true
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private fun bindPreferenceSummaryToValue(preference: Preference) {
        // Set the listener to watch for value changes.
        preference.onPreferenceChangeListener = this

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.context)
                        .getString(preference.key, "") as Any)
    }

}