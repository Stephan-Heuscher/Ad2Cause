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
                // Insert sample causes
                val sampleCauses = listOf(
                    Cause(
                        name = "Clean Water for Africa",
                        description = "Providing access to clean water and sanitation",
                        category = "Environment",
                        imageUrl = "https://via.placeholder.com/200?text=Water",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Animal Rescue Initiative",
                        description = "Rescuing and rehabilitating endangered animals",
                        category = "Animals",
                        imageUrl = "https://via.placeholder.com/200?text=Animals",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Education for Children",
                        description = "Providing quality education to underprivileged children",
                        category = "Education",
                        imageUrl = "https://via.placeholder.com/200?text=Education",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Renewable Energy Projects",
                        description = "Supporting sustainable and renewable energy initiatives",
                        category = "Environment",
                        imageUrl = "https://via.placeholder.com/200?text=Energy",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Medical Research",
                        description = "Funding breakthrough medical research and treatments",
                        category = "Health",
                        imageUrl = "https://via.placeholder.com/200?text=Medical",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Food Security Program",
                        description = "Ensuring food security and nutrition for vulnerable populations",
                        category = "Humanitarian",
                        imageUrl = "https://via.placeholder.com/200?text=Food",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Climate Action",
                        description = "Fighting climate change through sustainability projects",
                        category = "Environment",
                        imageUrl = "https://via.placeholder.com/200?text=Climate",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Poverty Alleviation",
                        description = "Helping communities break the cycle of poverty",
                        category = "Humanitarian",
                        imageUrl = "https://via.placeholder.com/200?text=Poverty",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Ocean Conservation",
                        description = "Protecting marine ecosystems and ocean biodiversity",
                        category = "Environment",
                        imageUrl = "https://via.placeholder.com/200?text=Ocean",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Mental Health Awareness",
                        description = "Supporting mental health initiatives and awareness programs",
                        category = "Health",
                        imageUrl = "https://via.placeholder.com/200?text=Mental",
                        isUserAdded = false,
                        totalEarned = 0.0
                    ),
                    Cause(
                        name = "Support Assistive Touch App",
                        description = "Supporting the development of accessibility features and assistive technology",
                        category = "Technology",
                        imageUrl = "https://via.placeholder.com/200?text=Assistive",
                        isUserAdded = false,
                        totalEarned = 0.0
                    )
                )

                for (cause in sampleCauses) {
                    repository.insertCause(cause)
                }
            }
        }
    }
}
