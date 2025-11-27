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
import coil.load
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.ads.AdManager
import ch.heuscher.ad2cause.databinding.FragmentHomeBinding
import ch.heuscher.ad2cause.viewmodel.AdViewModel
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * Home/Dashboard Fragment
 * Displays the active cause, total earnings, and buttons to watch ads.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var causeViewModel: CauseViewModel
    private lateinit var adViewModel: AdViewModel
    private lateinit var adManager: AdManager
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
        
        // Cause info container click - navigate to causes
        binding.causeInfoContainer.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigation
            )?.selectedItemId = R.id.nav_causes
        }

        // Non-Interactive Ad Button (Standard earnings, passive viewing)
        // Both the card and button are clickable
        binding.watchVideoAdCard.setOnClickListener {
            handleWatchAdClick(AdManager.AdType.NON_INTERACTIVE)
        }
        binding.watchVideoAdButton.setOnClickListener {
            handleWatchAdClick(AdManager.AdType.NON_INTERACTIVE)
        }

        // Interactive Ad Button (Higher earnings, users can interact)
        binding.interactiveAdCard.setOnClickListener {
            handleWatchAdClick(AdManager.AdType.INTERACTIVE)
        }
        binding.engageInteractiveAdButton.setOnClickListener {
            handleWatchAdClick(AdManager.AdType.INTERACTIVE)
        }

        // Load the first ad on startup (non-interactive as default) with cause info
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { cause ->
                if (cause != null && !adManager.isAdReady() && !adManager.isAdLoading()) {
                    adManager.loadRewardedAd(
                        AdManager.AdType.NON_INTERACTIVE,
                        cause.id.toString(),
                        cause.name
                    )
                }
            }
        }
    }
    
    /**
     * Handle watch ad button click
     */
    private fun handleWatchAdClick(adType: AdManager.AdType) {
        if (causeViewModel.activeCause.value == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_cause_selected),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        watchAd(adType)
    }

    /**
     * Watch ad of specified type
     */
    private fun watchAd(adType: AdManager.AdType) {
        val cause = causeViewModel.activeCause.value
        if (cause == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_cause_selected),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (adManager.isAdReady()) {
            // Ad is already loaded, show it
            adManager.showRewardedAd(requireActivity())
        } else {
            // Load ad of specified type with cause information
            if (!adManager.isAdLoading()) {
                adManager.loadRewardedAd(
                    adType,
                    cause.id.toString(),
                    cause.name
                )
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
                    // Show cause info and hide empty state
                    binding.causeInfoContainer.visibility = View.VISIBLE
                    binding.noCauseGuidance.visibility = View.GONE

                    // Load cause icon
                    binding.activeCauseIcon.load(cause.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_placeholder)
                        error(R.drawable.ic_placeholder)
                    }

                    // Set cause name
                    binding.activeCauseName.text = cause.name
                    
                    // Set description if available
                    if (!cause.description.isNullOrEmpty()) {
                        binding.activeCauseDescription.text = cause.description
                        binding.activeCauseDescription.visibility = View.VISIBLE
                    } else {
                        binding.activeCauseDescription.visibility = View.GONE
                    }
                    
                    // Update points display - just the number
                    binding.totalEarningsText.text = String.format("%.0f", cause.totalEarned)
                } else {
                    // Hide cause info and show empty state
                    binding.causeInfoContainer.visibility = View.GONE
                    binding.noCauseGuidance.visibility = View.VISIBLE
                    binding.totalEarningsText.text = "0"
                }
            }
        }

        // Observe ad loading state
        adViewModel.isAdLoading.observe(viewLifecycleOwner) { isLoading ->
            updateButtonStates()

            // Show/hide loading indicator
            binding.loadingContainer.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        binding.watchVideoAdCard.isClickable = buttonsEnabled
        binding.interactiveAdCard.isClickable = buttonsEnabled

        // Set alpha to make cards appear disabled when not available
        binding.watchVideoAdCard.alpha = if (buttonsEnabled) 1.0f else 0.6f
        binding.interactiveAdCard.alpha = if (buttonsEnabled) 1.0f else 0.6f
    }

    override fun onResume() {
        super.onResume()
        // Reload active cause data when returning to this fragment
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { cause ->
                if (cause != null) {
                    // Load cause icon
                    binding.activeCauseIcon.load(cause.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_placeholder)
                        error(R.drawable.ic_placeholder)
                    }

                    // Set cause name and earnings
                    binding.activeCauseName.text = cause.name
                    binding.totalEarningsText.text = String.format("%.0f", cause.totalEarned)
                    
                    // Set description if available
                    if (!cause.description.isNullOrEmpty()) {
                        binding.activeCauseDescription.text = cause.description
                        binding.activeCauseDescription.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
