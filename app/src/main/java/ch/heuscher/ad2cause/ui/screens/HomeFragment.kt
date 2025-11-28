package ch.heuscher.ad2cause.ui.screens

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.ads.AdManager
import ch.heuscher.ad2cause.ads.AdEscapeOverlayService
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
    private val handler = Handler(Looper.getMainLooper())
    
    // Multi-ad tracking
    private var adsToWatch = 0
    private var adsWatched = 0
    private var isMultiAdMode = false
    
    // Flag to track if user clicked to watch ad (prevent auto-show on preload)
    private var pendingAdShow = false

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
        setupEscapeOverlay()
        
        // Pre-load an ad
        preloadAd()
    }
    
    /**
     * Setup the escape overlay callback
     */
    private fun setupEscapeOverlay() {
        AdEscapeOverlayService.onEscapePressed = {
            // User pressed escape - cancel multi-ad mode and reset state
            isMultiAdMode = false
            pendingAdShow = false
            adsToWatch = 0
            adsWatched = 0
            
            // Try to go back to home
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.ad_cancelled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    /**
     * Show the escape overlay during ad playback
     */
    private fun showEscapeOverlay() {
        if (canDrawOverlay()) {
            val intent = Intent(requireContext(), AdEscapeOverlayService::class.java).apply {
                action = AdEscapeOverlayService.ACTION_SHOW
            }
            requireContext().startService(intent)
        }
    }
    
    /**
     * Hide the escape overlay
     */
    private fun hideEscapeOverlay() {
        val intent = Intent(requireContext(), AdEscapeOverlayService::class.java).apply {
            action = AdEscapeOverlayService.ACTION_HIDE
        }
        try {
            requireContext().startService(intent)
        } catch (e: Exception) {
            // Ignore if service not running
        }
    }
    
    /**
     * Check if overlay permission is granted
     */
    private fun canDrawOverlay(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(requireContext())
        } else {
            true
        }
    }
    
    /**
     * Request overlay permission if not granted
     */
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext())) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Required")
                .setMessage(getString(R.string.overlay_permission_required))
                .setPositiveButton("Grant") { _, _ ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${requireContext().packageName}")
                    )
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    
    /**
     * Preload an ad for faster display
     */
    private fun preloadAd() {
        val cause = causeViewModel.activeCause.value
        if (cause != null && !adManager.isAdReady() && !adManager.isAdLoading()) {
            adManager.loadRewardedAd(
                AdManager.AdType.NON_INTERACTIVE,
                cause.id.toString(),
                cause.name
            )
        }
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

        // Multi-ad option
        binding.multiAdCard.setOnClickListener {
            showMultiAdDialog()
        }
        binding.multiAdButton.setOnClickListener {
            showMultiAdDialog()
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
     * Show dialog to select number of ads to watch
     */
    private fun showMultiAdDialog() {
        val cause = causeViewModel.activeCause.value
        if (cause == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_cause_to_earn),
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigation
            )?.selectedItemId = R.id.nav_causes
            return
        }
        
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 2
            maxValue = 10
            value = 3
            wrapSelectorWheel = false
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.multi_ad_dialog_title))
            .setMessage(getString(R.string.multi_ad_dialog_message))
            .setView(numberPicker)
            .setPositiveButton(getString(R.string.watch)) { _, _ ->
                startMultiAdMode(numberPicker.value)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
    
    /**
     * Start watching multiple ads
     * Always uses INTERACTIVE ads for better engagement in multi-ad mode
     */
    private fun startMultiAdMode(count: Int) {
        adsToWatch = count
        adsWatched = 0
        isMultiAdMode = true
        
        Toast.makeText(
            requireContext(),
            getString(R.string.multi_ad_progress, 1, adsToWatch),
            Toast.LENGTH_SHORT
        ).show()
        
        // Always use INTERACTIVE ads for multi-ad mode
        watchAd(AdManager.AdType.INTERACTIVE)
    }
    
    /**
     * Continue with next ad in multi-ad mode
     */
    private fun continueMultiAdMode() {
        if (isMultiAdMode && adsWatched < adsToWatch) {
            Toast.makeText(
                requireContext(),
                getString(R.string.multi_ad_progress, adsWatched + 1, adsToWatch),
                Toast.LENGTH_SHORT
            ).show()
            
            // Immediately load and show next ad (no delay in multi-ad mode)
            // Always use INTERACTIVE ads for multi-ad mode for better engagement
            watchAd(AdManager.AdType.INTERACTIVE)
        } else if (isMultiAdMode) {
            // Completed all ads
            isMultiAdMode = false
            Toast.makeText(
                requireContext(),
                getString(R.string.multi_ad_complete, adsWatched),
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    /**
     * Handle watch ad button click
     */
    private fun handleWatchAdClick(adType: AdManager.AdType) {
        val cause = causeViewModel.activeCause.value
        if (cause == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_cause_to_earn),
                Toast.LENGTH_SHORT
            ).show()
            // Navigate to causes to select one
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigation
            )?.selectedItemId = R.id.nav_causes
            return
        }
        isMultiAdMode = false
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
                getString(R.string.select_cause_to_earn),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Show loading state
        showLoading(true)
        
        // Mark that user wants to see an ad
        pendingAdShow = true

        if (adManager.isAdReady()) {
            // Ad is already loaded, show it
            showLoading(false)
            pendingAdShow = false
            showEscapeOverlay()  // Show escape button during ad
            adManager.showRewardedAd(requireActivity())
        } else {
            // Load ad of specified type with cause information
            if (!adManager.isAdLoading()) {
                adManager.loadRewardedAd(
                    adType,
                    cause.id.toString(),
                    cause.name
                )
            }
        }
    }
    
    /**
     * Show/hide loading state
     */
    private fun showLoading(show: Boolean) {
        binding.loadingContainer.visibility = if (show) View.VISIBLE else View.GONE
        binding.watchVideoAdButton.isEnabled = !show
        binding.engageInteractiveAdButton.isEnabled = !show
        binding.multiAdButton.isEnabled = !show
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
                    
                    // Preload ad for the active cause
                    if (!adManager.isAdReady() && !adManager.isAdLoading()) {
                        adManager.loadRewardedAd(
                            AdManager.AdType.NON_INTERACTIVE,
                            cause.id.toString(),
                            cause.name
                        )
                    }
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
            showLoading(false)
            updateButtonStates()
            
            // Only auto-show if user explicitly requested it (button was clicked)
            if (pendingAdShow && adManager.isAdReady()) {
                pendingAdShow = false
                showEscapeOverlay()  // Show escape button during ad
                adManager.showRewardedAd(requireActivity())
            }
        }

        adManager.onRewardEarned = { rewardAmount ->
            val cause = causeViewModel.activeCause.value
            if (cause != null) {
                // Update local earnings in database
                causeViewModel.updateActiveCauseEarnings(rewardAmount)
                
                // Track for multi-ad mode
                if (isMultiAdMode) {
                    adsWatched++
                }

                // Show reward message after a short delay (ad dismissal)
                handler.postDelayed({
                    if (!isMultiAdMode) {
                        val message = getString(R.string.ad_watch_reward, rewardAmount, cause.name)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }, 500)
            }
        }

        adManager.onAdFailedToLoad = { adError ->
            // Mark ads as unavailable
            adsAvailable = false
            pendingAdShow = false
            showLoading(false)
            updateButtonStates()
            isMultiAdMode = false
            hideEscapeOverlay()  // Hide escape button on failure
            Toast.makeText(
                requireContext(),
                getString(R.string.ad_not_ready),
                Toast.LENGTH_SHORT
            ).show()
        }
        
        adManager.onAdClosed = {
            // Hide escape overlay
            hideEscapeOverlay()
            
            // Reset pending flag - user has seen an ad or closed it
            pendingAdShow = false
            
            // Check if we should continue multi-ad mode
            if (isMultiAdMode && adsWatched < adsToWatch) {
                // In multi-ad mode, immediately continue to next ad
                pendingAdShow = true
                continueMultiAdMode()
            } else {
                // End multi-ad mode if active
                isMultiAdMode = false
                
                // Preload next ad after 1 second (reduced from 2 seconds for faster experience)
                val cause = causeViewModel.activeCause.value
                if (cause != null) {
                    handler.postDelayed({
                        if (!adManager.isAdReady() && !adManager.isAdLoading()) {
                            adManager.loadRewardedAd(
                                AdManager.AdType.NON_INTERACTIVE,
                                cause.id.toString(),
                                cause.name
                            )
                        }
                    }, 1000)
                }
            }
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
        binding.multiAdButton.isEnabled = buttonsEnabled
        binding.watchVideoAdCard.isClickable = buttonsEnabled
        binding.interactiveAdCard.isClickable = buttonsEnabled
        binding.multiAdCard.isClickable = buttonsEnabled

        // Set alpha to make cards appear disabled when not available
        binding.watchVideoAdCard.alpha = if (buttonsEnabled) 1.0f else 0.6f
        binding.interactiveAdCard.alpha = if (buttonsEnabled) 1.0f else 0.6f
        binding.multiAdCard.alpha = if (buttonsEnabled) 1.0f else 0.6f
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
        handler.removeCallbacksAndMessages(null)
        hideEscapeOverlay()
        AdEscapeOverlayService.onEscapePressed = null
    }
}
