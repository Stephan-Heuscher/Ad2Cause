package ch.heuscher.ad2cause

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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

        // Setup toolbar
        setSupportActionBar(binding.toolbar)

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
            else -> super.onOptionsItemSelected(item)
        }
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
                // Insert the 3 predefined causes
                val causes = listOf(
                    Cause(
                        name = "AI Rescue Ring",
                        description = "An AI-powered emergency assistance system that provides intelligent rescue coordination and safety monitoring",
                        category = "Technology",
                        imageUrl = "https://raw.githubusercontent.com/Stephan-Heuscher/AI-Rescue-Ring/main/icon.png",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Assistive Tap",
                        description = "Supporting the development of accessibility features and assistive technology for users with disabilities",
                        category = "Technology",
                        imageUrl = "https://raw.githubusercontent.com/Stephan-Heuscher/Assistive-Tap/main/icon.png",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Safe Home Button",
                        description = "A safety-focused home automation solution providing secure access control and emergency home features",
                        category = "Technology",
                        imageUrl = "https://raw.githubusercontent.com/Stephan-Heuscher/Safe-Home-Button/main/icon.png",
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
