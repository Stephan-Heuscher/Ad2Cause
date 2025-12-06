package ch.heuscher.ad2cause.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import ch.heuscher.ad2cause.R

/**
 * Simple Settings screen to toggle Dark Mode and re-open onboarding.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val PREF_DARK_MODE = "pref_dark_mode"
    private val PREF_ONBOARDING_SEEN = "pref_onboarding_seen"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val darkPref = findPreference<SwitchPreferenceCompat>(PREF_DARK_MODE)
        val showOnboardingPref = findPreference<Preference>("pref_show_onboarding")

        // Initialize dark mode switch based on stored pref
        val prefs = requireContext().getSharedPreferences("ad2cause_prefs", Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean(PREF_DARK_MODE, false)
        darkPref?.isChecked = enabled

        darkPref?.setOnPreferenceChangeListener { preference, newValue ->
            val enabled = newValue as? Boolean ?: false
            // Persist preference
            prefs.edit().putBoolean(PREF_DARK_MODE, enabled).apply()

            // Apply theme
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

            true
        }

        showOnboardingPref?.setOnPreferenceClickListener {
            // Reset onboarding flag and navigate to onboarding fragment
            prefs.edit().putBoolean(PREF_ONBOARDING_SEEN, false).apply()
            // Use navigation controller if available
            try {
                findNavController().navigate(R.id.onboarding_fragment)
            } catch (e: Exception) {
                // Fallback: use fragment transaction
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, OnboardingFragment())
                    .addToBackStack(null)
                    .commit()
            }

            true
        }
    }
}
