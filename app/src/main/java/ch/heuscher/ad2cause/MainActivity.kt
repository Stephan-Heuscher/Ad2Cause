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
import ch.heuscher.ad2cause.data.database.Ad2CauseDatabase
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.data.repository.CauseRepository
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var causeViewModel: CauseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModels
        causeViewModel = ViewModelProvider(this)[CauseViewModel::class.java]

        // Initialize database
        val database = Ad2CauseDatabase.getDatabase(this)

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

        // Initialize sample data on first launch
        initializeSampleData(database)
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
    private fun initializeSampleData(database: Ad2CauseDatabase) {
        lifecycleScope.launch {
            val repository = CauseRepository(database.causeDao())

            // Check if database is empty
            val existingCauses = repository.getAllCausesSync()
            if (existingCauses.isEmpty()) {
                // Insert the 3 predefined causes with updated descriptions from GitHub
                val causes = listOf(
                    Cause(
                        name = "AI Rescue Ring",
                        description = "Your intelligent companion on Android - always visible, always ready to help. Tap the rescue ring whenever you need assistance, and a powerful AI (Gemini 2.5 Flash) will help you with any task on your device. Features context-aware help that sees what you see, voice or text chat, smart keyboard avoidance, and customizable appearance. Privacy-first: your API key stays on your device.",
                        imageUrl = "file:///android_asset/Rescue_Ring_Icon.png",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Assistive Tap",
                        description = "Your navigation helper – A floating dot that helps users reach Home or navigate with their thumb from anywhere on the screen. Features Safe-Home mode (all taps go Home) for maximum security, and Navi mode for advanced users (1x tap = Back, 2x = Switch app, 3x = Recent apps, long press = Home). Designed for accessibility with WCAG 2.1 Level AA compliance.",
                        imageUrl = "file:///android_asset/Assistive_Tap_Icon.png",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Safe Home Button",
                        description = "A floating accessibility button that always brings you safely back home with a simple tap. Ideal for users with motor limitations who have difficulty reaching the phone's navigation buttons. Features Safe-Home mode with square design (like Android navigation), protected repositioning to prevent accidental moves, keyboard avoidance, and clean architecture for reliability.",
                        imageUrl = "file:///android_asset/Safe_Home_Button_Icon.png",
                        isUserAdded = false,
                        totalEarned = 0.0
                    )
                )

                causes.forEach { cause ->
                    repository.insertCause(cause)
                }

                // Set Safe Home Button as the default active cause
                val allCauses = repository.getAllCausesSync()
                val safeHomeButton = allCauses.find { it.name == "Safe Home Button" }
                safeHomeButton?.let { causeViewModel.setActiveCause(it) }
            }
        }
    }
}
