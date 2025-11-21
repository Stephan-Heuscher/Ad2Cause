package ch.heuscher.ad2cause.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manager class for handling app preferences using SharedPreferences.
 * Provides simple get/set methods for app settings.
 */
class PreferencesManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "Ad2CausePreferences"
        private const val KEY_ADS_18_PLUS_ENABLED = "ads_18_plus_enabled"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Check if 18+ ads are enabled.
     * Default: false (disabled/hidden)
     */
    fun isAds18PlusEnabled(): Boolean {
        return prefs.getBoolean(KEY_ADS_18_PLUS_ENABLED, false)
    }

    /**
     * Enable or disable 18+ ads.
     * @param enabled true to enable/show 18+ ads, false to disable/hide
     */
    fun setAds18PlusEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ADS_18_PLUS_ENABLED, enabled).apply()
    }
}
