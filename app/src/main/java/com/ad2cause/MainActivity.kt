package com.ad2cause

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ad2cause.data.database.Ad2CauseDatabase
import com.ad2cause.data.models.Cause
import com.ad2cause.databinding.ActivityMainBinding
import com.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * MainActivity
 * Entry point of the Ad2Cause application.
 * Handles initialization of database with sample data, setup of navigation,
 * and management of the overall app lifecycle.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var causeViewModel: CauseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        causeViewModel = ViewModelProvider(this).get(CauseViewModel::class.java)

        // Setup navigation
        setupNavigation()

        // Initialize database with sample data on first launch
        initializeSampleData()
    }

    /**
     * Setup bottom navigation with NavController.
     */
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Connect bottom navigation to nav controller
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }

    /**
     * Initialize database with sample causes on first app launch.
     */
    private fun initializeSampleData() {
        lifecycleScope.launch {
            val database = Ad2CauseDatabase.getDatabase(applicationContext)
            val causeDao = database.causeDao()

            // Check if database is empty
            lifecycleScope.launch {
                causeDao.getAllCauses().collect { causes ->
                    if (causes.isEmpty()) {
                        // Insert sample causes
                        val sampleCauses = listOf(
                            Cause(
                                name = "Clean Water for Africa",
                                description = "Providing clean drinking water to communities in Africa",
                                category = "Environment",
                                imageUrl = "https://via.placeholder.com/200?text=Clean+Water",
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
                                description = "Promoting sustainable and renewable energy solutions",
                                category = "Environment",
                                imageUrl = "https://via.placeholder.com/200?text=Energy",
                                isUserAdded = false,
                                totalEarned = 0.0
                            ),
                            Cause(
                                name = "Medical Research",
                                description = "Funding innovative medical research and treatments",
                                category = "Health",
                                imageUrl = "https://via.placeholder.com/200?text=Medical",
                                isUserAdded = false,
                                totalEarned = 0.0
                            ),
                            Cause(
                                name = "Food Security Program",
                                description = "Combating hunger and improving food security globally",
                                category = "Humanitarian",
                                imageUrl = "https://via.placeholder.com/200?text=Food",
                                isUserAdded = false,
                                totalEarned = 0.0
                            ),
                            Cause(
                                name = "Climate Action",
                                description = "Fighting climate change through community initiatives",
                                category = "Environment",
                                imageUrl = "https://via.placeholder.com/200?text=Climate",
                                isUserAdded = false,
                                totalEarned = 0.0
                            ),
                            Cause(
                                name = "Poverty Alleviation",
                                description = "Helping families escape poverty with economic opportunities",
                                category = "Humanitarian",
                                imageUrl = "https://via.placeholder.com/200?text=Poverty",
                                isUserAdded = false,
                                totalEarned = 0.0
                            ),
                            Cause(
                                name = "Ocean Conservation",
                                description = "Protecting marine ecosystems and ocean habitats",
                                category = "Environment",
                                imageUrl = "https://via.placeholder.com/200?text=Ocean",
                                isUserAdded = false,
                                totalEarned = 0.0
                            ),
                            Cause(
                                name = "Mental Health Awareness",
                                description = "Raising awareness and supporting mental health initiatives",
                                category = "Health",
                                imageUrl = "https://via.placeholder.com/200?text=Mental+Health",
                                isUserAdded = false,
                                totalEarned = 0.0
                            )
                        )

                        // Insert all sample causes
                        for (cause in sampleCauses) {
                            causeDao.insertCause(cause)
                        }
                    }
                }
            }
        }
    }
}
