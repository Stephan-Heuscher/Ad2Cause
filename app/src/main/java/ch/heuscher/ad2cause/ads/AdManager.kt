package ch.heuscher.ad2cause.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * Manager class for handling Google AdMob rewarded ads.
 * Handles ad loading, displaying, and reward callbacks.
 *
 * To use this class:
 * 1. Replace the placeholder Ad Unit IDs with your actual IDs from Google AdMob console
 * 2. Initialize with context in your Application or Activity
 * 3. Call loadRewardedAd() to load an ad
 * 4. Call showRewardedAd() to display the ad
 *
 * IMPORTANT: Replace these placeholder IDs:
 * - APP_ID: Your AdMob App ID
 * - REWARDED_AD_UNIT_ID: Your Rewarded Ad Unit ID
 */
class AdManager(private val context: Context) {

    companion object {
        private const val TAG = "AdManager"

        // APP ID from AdMob Console
        const val APP_ID = "ca-app-pub-5567609971256551~4548078693"

        // Ad Unit IDs from AdMob Console
        // Interactive Rewarded Ad Unit (Higher earnings - users can interact)
        private const val REWARDED_AD_UNIT_ID_INTERACTIVE = "ca-app-pub-5567609971256551/1555083848"
        
        // Non-Interactive Rewarded Ad Unit (Standard earnings - passive viewing)
        private const val REWARDED_AD_UNIT_ID_NON_INTERACTIVE = "ca-app-pub-5567609971256551/1251831518"

        // Test IDs (for development/testing):
        // private const val REWARDED_AD_UNIT_ID_TEST = "ca-app-pub-3940256099942544/5224354917"
    }

    /**
     * Enum to define ad types with their characteristics
     */
    enum class AdType {
        INTERACTIVE,      // Users can interact - higher earnings
        NON_INTERACTIVE   // Passive viewing only - standard earnings
    }

    private var rewardedAd: RewardedAd? = null
    private var isLoading = false
    private var adType: AdType = AdType.NON_INTERACTIVE  // Default to non-interactive

    // Callback interfaces for ad lifecycle events
    var onAdLoaded: (() -> Unit)? = null
    var onAdFailedToLoad: ((LoadAdError) -> Unit)? = null
    var onAdClosed: (() -> Unit)? = null
    var onRewardEarned: ((Double) -> Unit)? = null

    /**
     * Initialize Mobile Ads SDK.
     * Should be called once when the app starts.
     */
    fun initializeMobileAds() {
        MobileAds.initialize(context)
        Log.d(TAG, "Mobile Ads SDK initialized")
    }

    /**
     * Load a rewarded ad of the specified type.
     * Can be called multiple times; new ads will replace previous ones.
     *
     * @param type AdType to load (INTERACTIVE for higher earnings, NON_INTERACTIVE for standard)
     */
    fun loadRewardedAd(type: AdType = AdType.NON_INTERACTIVE) {
        if (isLoading || rewardedAd != null) {
            Log.d(TAG, "Ad is already loaded or currently loading")
            return
        }

        isLoading = true
        adType = type
        val adUnitId = when (type) {
            AdType.INTERACTIVE -> REWARDED_AD_UNIT_ID_INTERACTIVE
            AdType.NON_INTERACTIVE -> REWARDED_AD_UNIT_ID_NON_INTERACTIVE
        }

        val adRequest = AdRequest.Builder().build()

        Log.d(TAG, "Loading ${type.name} rewarded ad with Unit ID: $adUnitId")

        RewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    Log.d(TAG, "Rewarded ad (${type.name}) loaded successfully")
                    rewardedAd = ad
                    isLoading = false
                    onAdLoaded?.invoke()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Log.e(TAG, "Failed to load rewarded ad: ${adError.message}")
                    rewardedAd = null
                    isLoading = false
                    onAdFailedToLoad?.invoke(adError)
                }
            }
        )
    }

    /**
     * Display the loaded rewarded ad.
     * Make sure to call loadRewardedAd() first and wait for the ad to load.
     */
    fun showRewardedAd(activity: android.app.Activity) {
        if (rewardedAd == null) {
            Log.w(TAG, "Rewarded ad is not loaded. Call loadRewardedAd() first.")
            return
        }

        rewardedAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed")
                rewardedAd = null
                onAdClosed?.invoke()
                // Optionally reload the ad for the next viewing
                loadRewardedAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                Log.e(TAG, "Ad failed to show: ${adError.message}")
                rewardedAd = null
                onAdClosed?.invoke()
                loadRewardedAd()
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed full screen content")
            }
        }

        // Set reward callback - called when user successfully completes the ad
        rewardedAd?.let { ad ->
            ad.show(activity) { reward ->
                Log.d(TAG, "User earned reward: ${reward.amount}")
                val rewardAmount = 0.01 // $0.01 per ad
                onRewardEarned?.invoke(rewardAmount)
            }
        }
    }

    /**
     * Get the current ad type being used.
     */
    fun getCurrentAdType(): AdType = adType

    /**
     * Set the ad type preference for future ads.
     * This will be used when loadRewardedAd() is called without parameters.
     */
    fun setAdType(type: AdType) {
        adType = type
        Log.d(TAG, "Ad type preference changed to: ${type.name}")
    }

    /**
     * Check if a rewarded ad is currently loaded and ready to show.
     */
    fun isAdReady(): Boolean = rewardedAd != null && !isLoading

    /**
     * Get the current loading state.
     */
    fun isAdLoading(): Boolean = isLoading
}
