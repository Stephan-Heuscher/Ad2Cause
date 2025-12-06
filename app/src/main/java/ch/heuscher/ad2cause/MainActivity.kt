package ch.heuscher.ad2cause

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ch.heuscher.ad2cause.databinding.ActivityMainBinding
// Causes are now managed in-memory via MainActivity (no DB inserts)
import android.util.Log
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var causeViewModel: CauseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply theme preference early so UI inflates with correct mode
        val prefs = getSharedPreferences("ad2cause_prefs", MODE_PRIVATE)
        val darkModeEnabled = prefs.getBoolean("pref_dark_mode", false)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            if (darkModeEnabled) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModels
        causeViewModel = ViewModelProvider(this)[CauseViewModel::class.java]

        // No database initialization for causes - they are provided in-memory below

        // Setup toolbar - hide the default title since we have a custom header
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setup navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navController = navHostFragment?.navController
            ?: error("NavHostFragment not found")

        // Setup bottom navigation with nav controller
        binding.bottomNavigation.setupWithNavController(navController)

        // Initialize sample data in-memory on first launch
        initializeSampleData()

        // Show onboarding the first time the user launches the app
        val seen = prefs.getBoolean("pref_onboarding_seen", false)
        if (!seen) {
            // navigate to onboarding fragment
            navController.navigate(R.id.onboarding_fragment)
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_privacy_policy -> {
                navController.navigate(R.id.privacy_policy_fragment)
                true
            }
            R.id.action_feedback -> {
                sendFeedbackEmail()
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            R.id.action_settings -> {
                navController.navigate(R.id.settings_fragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Send feedback via email
     */
    private fun sendFeedbackEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("stv.heuscher@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_email_body))
        }

        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Show about dialog
     */
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_name))
            .setMessage("${getString(R.string.app_slogan)}\n\nVersion 1.0\n\n© 2025 Stephan Heuscher\n\nAd2Cause helps you support open-source accessibility projects by watching ads. Every view contributes to making technology accessible for everyone.")
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Initialize the database with the 3 predefined causes on first launch.
     * Sets Safe Home Button as the default active cause.
     */
    private fun initializeSampleData() {
        lifecycleScope.launch {
            // Insert the 3 predefined causes (in-memory only)
            val causes = listOf(
                Cause(
                    id = 1,
                    name = getString(R.string.cause_ai_rescue_ring_name),
                    description = getString(R.string.cause_ai_rescue_ring_desc),
                    imageUrl = "file:///android_asset/Rescue_Ring_Icon.png",
                    isUserAdded = false,
                    totalEarned = 0.0
                ),
                Cause(
                    id = 2,
                    name = getString(R.string.cause_assistive_tap_name),
                    description = getString(R.string.cause_assistive_tap_desc),
                    imageUrl = "file:///android_asset/Assistive_Tap_Icon.png",
                    isUserAdded = false,
                    totalEarned = 0.0
                ),
                Cause(
                    id = 3,
                    name = getString(R.string.cause_safe_home_button_name),
                    description = getString(R.string.cause_safe_home_button_desc),
                    imageUrl = "file:///android_asset/Safe_Home_Button_Icon.png",
                    isUserAdded = false,
                    totalEarned = 0.0
                )
            )

            Log.i(TAG, "initializeSampleData: setting ${causes.size} in-memory causes")
            // Provide these causes to the ViewModel (in-memory only)
            causeViewModel.setCauses(causes)

            Log.i(TAG, "initializeSampleData: set causes into CauseViewModel, defaulting safe-home if present")
            // Set Safe Home Button as the default active cause if none defined
            val safeHomeButton = causes.find { it.name == getString(R.string.cause_safe_home_button_name) }
            safeHomeButton?.let { causeViewModel.setActiveCause(it) }
        }
    }
}
