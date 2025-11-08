package ch.heuscher.ad2cause.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.ads.AdManager
import ch.heuscher.ad2cause.data.models.AdType
import ch.heuscher.ad2cause.data.repository.FirebaseRepository
import ch.heuscher.ad2cause.databinding.FragmentHomeBinding
import ch.heuscher.ad2cause.viewmodel.AdViewModel
import ch.heuscher.ad2cause.viewmodel.AuthViewModel
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * Home/Dashboard Fragment
 * Displays the active cause, total earnings, and buttons to watch ads.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var causeViewModel: CauseViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adViewModel: AdViewModel
    private lateinit var adManager: AdManager
    private val firebaseRepository = FirebaseRepository()
    private var adsAvailable = true  // Track if ads can be loaded

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModels
        causeViewModel = ViewModelProvider(requireActivity())[CauseViewModel::class.java]
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        adViewModel = ViewModelProvider(requireActivity())[AdViewModel::class.java]

        // Initialize AdManager
        adManager = AdManager(requireContext())
        adManager.initializeMobileAds()

        setupUI()
        observeViewModel()
        setupAdCallbacks()
    }

    /**
     * Setup UI elements and their listeners.
     */
    private fun setupUI() {
        // Browse Causes Button (navigates to Causes tab)
        binding.browseCausesButton.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigation
            )?.selectedItemId = R.id.nav_causes
        }

        // Non-Interactive Ad Button (Standard earnings, passive viewing)
        binding.watchVideoAdButton.setOnClickListener {
            if (causeViewModel.activeCause.value == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_cause_selected),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            watchAd(AdManager.AdType.NON_INTERACTIVE)
        }

        // Interactive Ad Button (Higher earnings, users can interact)
        binding.engageInteractiveAdButton.setOnClickListener {
            if (causeViewModel.activeCause.value == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_cause_selected),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            watchAd(AdManager.AdType.INTERACTIVE)
        }

        // Load the first ad on startup (non-interactive as default)
        adManager.loadRewardedAd(AdManager.AdType.NON_INTERACTIVE)
    }

    /**
     * Watch ad of specified type
     */
    private fun watchAd(adType: AdManager.AdType) {
        if (adManager.isAdReady()) {
            // Ad is already loaded, show it
            adManager.showRewardedAd(requireActivity())
        } else {
            // Load ad of specified type
            if (!adManager.isAdLoading()) {
                adManager.loadRewardedAd(adType)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.ad_loading),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Observe ViewModel data for changes.
     */
    private fun observeViewModel() {
        // Observe active cause
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { cause ->
                if (cause != null) {
                    binding.activeCauseName.text = getString(R.string.supporting_cause, cause.name)
                    binding.totalEarningsText.text = String.format("%.0f points", cause.totalEarned)
                    binding.noCauseGuidance.visibility = View.GONE
                } else {
                    binding.activeCauseName.text = getString(R.string.no_cause_selected)
                    binding.totalEarningsText.text = "0 points"
                    binding.noCauseGuidance.visibility = View.VISIBLE
                }
            }
        }

        // Observe ad loading state
        adViewModel.isAdLoading.observe(viewLifecycleOwner) { isLoading ->
            updateButtonStates()

            // Show/hide loading indicator
            binding.adLoadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loadingText.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    /**
     * Setup AdMob manager callbacks.
     */
    private fun setupAdCallbacks() {
        adManager.onAdLoaded = {
            // Ads are available
            adsAvailable = true
            updateButtonStates()
        }

        adManager.onRewardEarned = { rewardAmount ->
            val cause = causeViewModel.activeCause.value
            if (cause != null) {
                // Update local earnings in database
                causeViewModel.updateActiveCauseEarnings(rewardAmount)

                // If user is signed in, also record in Firestore
                val userId = authViewModel.getCurrentUserId()
                if (userId != null && cause.firestoreId != null) {
                    lifecycleScope.launch {
                        try {
                            // Record earning in Firestore
                            firebaseRepository.recordEarning(
                                userId = userId,
                                causeId = cause.firestoreId!!,
                                causeName = cause.name,
                                amount = rewardAmount,
                                adType = AdType.NON_INTERACTIVE // You can track ad type if needed
                            )

                            // Update user's total earned in Firestore
                            authViewModel.updateTotalEarned(rewardAmount)
                        } catch (e: Exception) {
                            // Silently fail - local earnings are still saved
                            // Could optionally queue for retry later
                        }
                    }
                }

                // Show reward message
                val message = getString(R.string.ad_watch_reward, rewardAmount, cause.name)
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        adManager.onAdFailedToLoad = { adError ->
            // Mark ads as unavailable
            adsAvailable = false
            updateButtonStates()
            // Silently handle ad loading failures during initial setup
            // Only show error if user explicitly tried to watch an ad
            // This prevents error toasts when logging in or navigating to the fragment
        }
    }

    /**
     * Update button states based on ad availability
     */
    private fun updateButtonStates() {
        val isLoading = adViewModel.isAdLoading.value ?: false
        val buttonsEnabled = adsAvailable && !isLoading

        binding.watchVideoAdButton.isEnabled = buttonsEnabled
        binding.engageInteractiveAdButton.isEnabled = buttonsEnabled

        // Set alpha to make buttons appear gray when disabled
        binding.watchVideoAdButton.alpha = if (buttonsEnabled) 1.0f else 0.5f
        binding.engageInteractiveAdButton.alpha = if (buttonsEnabled) 1.0f else 0.5f
    }

    override fun onResume() {
        super.onResume()
        // Reload active cause data when returning to this fragment
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { cause ->
                if (cause != null) {
                    binding.activeCauseName.text = getString(R.string.supporting_cause, cause.name)
                    binding.totalEarningsText.text = String.format("%.0f points", cause.totalEarned)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
