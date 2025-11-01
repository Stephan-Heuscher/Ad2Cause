package ch.heuscher.ad2cause

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ch.heuscher.ad2cause.databinding.ActivityMainBinding
import ch.heuscher.ad2cause.data.database.Ad2CauseDatabase

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
    }
}
