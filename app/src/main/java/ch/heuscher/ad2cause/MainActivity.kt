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
import ch.heuscher.ad2cause.data.repository.FirebaseRepository
import ch.heuscher.ad2cause.viewmodel.AuthViewModel
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var causeViewModel: CauseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModels
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
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

        // Observe authentication state
        observeAuthState()

        // Sync data on launch
        syncDataOnLaunch()
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
            R.id.action_sign_in -> {
                navController.navigate(R.id.sign_in_fragment)
                true
            }
            R.id.action_sign_out -> {
                showSignOutConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Observe authentication state and show sign-in prompt if needed
     */
    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.isSignedIn.collect { isSignedIn ->
                if (!isSignedIn) {
                    // Show optional sign-in prompt
                    showSignInPrompt()
                }
                // Update menu visibility
                invalidateOptionsMenu()
            }
        }
    }

    /**
     * Show optional sign-in prompt
     */
    private fun showSignInPrompt() {
        // Show a subtle prompt that signing in enables cross-device sync
        // Users can still use the app without signing in
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.sign_in_required)
            .setMessage(R.string.sign_in_description)
            .setPositiveButton(R.string.sign_in) { _, _ ->
                navController.navigate(R.id.sign_in_fragment)
            }
            .setNegativeButton("Skip") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    /**
     * Show sign-out confirmation dialog
     */
    private fun showSignOutConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.sign_out)
            .setMessage(R.string.sign_out_confirm)
            .setPositiveButton(R.string.sign_out) { _, _ ->
                authViewModel.signOut()
                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Sync causes from Firestore on app launch
     */
    private fun syncDataOnLaunch() {
        lifecycleScope.launch {
            authViewModel.isSignedIn.collect { isSignedIn ->
                if (isSignedIn) {
                    causeViewModel.syncCausesFromCloud()
                }
            }
        }
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
                    name = "Support Assistive Tap App",
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
