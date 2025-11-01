package ch.heuscher.ad2cause

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ch.heuscher.ad2cause.databinding.ActivityMainBinding
import ch.heuscher.ad2cause.data.database.Ad2CauseDatabase
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.data.repository.CauseRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database
        val database = Ad2CauseDatabase.getDatabase(this)

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

    /**
     * Initialize the database with sample causes on first launch.
     */
    private fun initializeSampleData(database: Ad2CauseDatabase) {
        lifecycleScope.launch {
            val repository = CauseRepository(database.causeDao())
            
            // Check if database is empty
            val existingCauses = repository.getAllCausesSync()
            if (existingCauses.isEmpty()) {
                // Insert sample cause
                val sampleCause = Cause(
                    name = "Support Assistive Touch App",
                    description = "Supporting the development of accessibility features and assistive technology",
                    category = "Technology",
                    imageUrl = "https://via.placeholder.com/200?text=Assistive",
                    isUserAdded = false,
                    totalEarned = 0.0
                )
                repository.insertCause(sampleCause)
            }
        }
    }
}
