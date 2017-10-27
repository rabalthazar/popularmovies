package com.example.popularmovies


import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity

/**
 * Activity that holds the settings fragment
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // Adds the general preferences fragment to the view
        fragmentManager.beginTransaction()
                .replace(R.id.settings_container, GeneralPreferenceFragment())
                .commit()
    }

    /**
     * General preferences fragment inner class.
     * This class implements the OnPreferenceChangeListener interface to show the current preference
     * value as a preference summary
     */
    class GeneralPreferenceFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)

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
                            .getString(preference.key, ""))
        }

    }
}
