package ch.heuscher.ad2cause.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

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
        // Ad unit name: "Rewarded max Earn"
        private const val REWARDED_AD_UNIT_ID_INTERACTIVE = "ca-app-pub-5567609971256551/1555083848"

        // Non-Interactive Rewarded Ad Unit (Standard earnings - passive viewing)
        // Ad unit name: "Reward not interactive"
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
    private var currentCauseId: String? = null  // Track which cause the ad is for
    private var currentCauseName: String? = null
    private var currentTransactionId: String? = null // Unique ID for server-side verification
    private var verificationListener: ListenerRegistration? = null

    // Callback interfaces for ad lifecycle events
    var onAdLoaded: (() -> Unit)? = null
    var onAdFailedToLoad: ((LoadAdError) -> Unit)? = null
    var onAdClosed: (() -> Unit)? = null
    var onRewardEarned: ((Double) -> Unit)? = null
    var onVerificationStarted: (() -> Unit)? = null
    var onVerificationFailed: ((String) -> Unit)? = null

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
     * @param causeId The ID of the cause this ad will support (for tracking)
     * @param causeName The name of the cause (for tracking)
     */
    fun loadRewardedAd(
        type: AdType = AdType.NON_INTERACTIVE,
        causeId: String? = null,
        causeName: String? = null
    ) {
        if (isLoading || rewardedAd != null) {
            Log.d(TAG, "Ad is already loaded or currently loading")
            return
        }

        isLoading = true
        adType = type
        currentCauseId = causeId
        currentCauseName = causeName
        currentTransactionId = java.util.UUID.randomUUID().toString()

        val adUnitId = when (type) {
            AdType.INTERACTIVE -> REWARDED_AD_UNIT_ID_INTERACTIVE
            AdType.NON_INTERACTIVE -> REWARDED_AD_UNIT_ID_NON_INTERACTIVE
        }

        val adRequest = AdRequest.Builder().build()

        Log.d(TAG, "Loading ${type.name} rewarded ad with Unit ID: $adUnitId for cause: $causeName (ID: $causeId)")
        Log.i(TAG, "loadRewardedAd (INFO): Starting to load ${type.name} ad for cause=${causeName}")

        RewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    Log.d(TAG, "Rewarded ad (${type.name}) loaded successfully")
                    Log.i(TAG, "onAdLoaded (INFO): ${type.name} loaded for cause=$causeName")
                    rewardedAd = ad

                    // Set server-side verification options to track the cause
                    if (causeId != null || causeName != null) {
                        val customDataJson = org.json.JSONObject().apply {
                            put("cause_id", causeId ?: "unknown")
                            put("cause_name", causeName ?: "unknown")
                            put("transaction_id", currentTransactionId)
                        }
                        val ssv = ServerSideVerificationOptions.Builder()
                            .setCustomData(customDataJson.toString())
                            .build()
                        ad.setServerSideVerificationOptions(ssv)
                        Log.d(TAG, "Set SSV with cause data: $customDataJson")
                    }

                    isLoading = false
                    onAdLoaded?.invoke()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Log.e(TAG, "Failed to load rewarded ad: ${adError.message}")
                    Log.i(TAG, "onAdFailedToLoad (INFO): failed loading ad for cause=$causeName error=${adError.message}")
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
     * 
     * @param activity The activity context
     * @param causeId The ID of the cause to credit
     * @param causeName The name of the cause
     */
    fun showRewardedAd(activity: android.app.Activity, causeId: String, causeName: String) {
        if (rewardedAd == null) {
            Log.w(TAG, "Rewarded ad is not loaded. Call loadRewardedAd() first.")
            return
        }

        // 1. Generate a unique transaction_id (UUID)
        val transactionId = java.util.UUID.randomUUID().toString()
        currentTransactionId = transactionId

        // 2. Create a JSON string for custom_data
        val customDataJson = org.json.JSONObject().apply {
            put("cause_id", causeId)
            put("cause_name", causeName)
            put("transaction_id", transactionId)
        }
        
        val customDataString = customDataJson.toString()
        Log.d(TAG, "DEBUG: Generated Custom Data for SSV: $customDataString")

        // 3. Configure the RewardedAd with ServerSideVerificationOptions
        val ssvOptions = ServerSideVerificationOptions.Builder()
            .setCustomData(customDataString)
            .build()
        
        rewardedAd?.setServerSideVerificationOptions(ssvOptions)
        Log.d(TAG, "DEBUG: SSV Options set on RewardedAd instance. Transaction ID: $transactionId")

        rewardedAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed")
                Log.i(TAG, "onAdDismissedFullScreenContent (INFO): user closed ad")
                rewardedAd = null
                onAdClosed?.invoke()
                // Note: Caller (HomeFragment) will handle reloading if needed
            }

            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                Log.e(TAG, "Ad failed to show: ${adError.message}")
                rewardedAd = null
                onAdClosed?.invoke()
                // Note: Caller (HomeFragment) will handle reloading if needed
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed full screen content")
                Log.i(TAG, "onAdShowedFullScreenContent (INFO): ad is visible on screen")
            }
        }

        // 4. Show the ad
        Log.d(TAG, "DEBUG: Showing Rewarded Ad now...")
        rewardedAd?.let { ad ->
            ad.show(activity) { reward ->
                // Use the reward amount from AdMob (configured in AdMob console)
                val rewardAmount = reward.amount.toDouble()
                Log.d(TAG, "User earned reward callback received: ${rewardAmount}")
                
                // Verify & Reward:
                // Do not grant the reward immediately.
                // Show a "Verifying..." loading state.
                onVerificationStarted?.invoke()

                // Start verification
                verifyReward(transactionId, rewardAmount)
            }
        }
    }

    /**
     * Verify the reward using Firestore listener with timeout.
     */
    private fun verifyReward(transactionId: String, rewardAmount: Double) {
        // Remove any existing listener
        verificationListener?.remove()

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("ad_rewards").document(transactionId)

        Log.d(TAG, "Starting verification for transaction: $transactionId")

        // Add a timeout (e.g., 10 seconds)
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        val timeoutRunnable = Runnable {
            Log.w(TAG, "Verification timed out for $transactionId")
            if (verificationListener != null) {
                verificationListener?.remove()
                verificationListener = null
                onVerificationFailed?.invoke("Verification timed out. Please try again.")
            }
        }
        handler.postDelayed(timeoutRunnable, 10000) // 10 seconds

        // Start a Firestore Snapshot Listener
        verificationListener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val status = snapshot.getString("status")
                Log.d(TAG, "Verification status update: $status")

                // Wait for the document to exist AND the status field to be "VERIFIED"
                if (status == "VERIFIED") {
                    Log.d(TAG, "Ad verified successfully!")
                    
                    // Cancel timeout
                    handler.removeCallbacks(timeoutRunnable)
                    
                    // Stop listening
                    verificationListener?.remove()
                    verificationListener = null
                    
                    // Grant the reward to the user
                    onRewardEarned?.invoke(rewardAmount)
                }
            }
        }
    }

    /**
     * Listen for server-side verification of the ad reward.
     * @deprecated Use verifyReward internal logic instead
     */
    private fun listenForVerification(transactionId: String) {
        // Kept for backward compatibility if needed, but logic moved to verifyReward
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

    /**
     * Get the current cause ID being tracked for this ad.
     */
    fun getCurrentCauseId(): String? = currentCauseId

    /**
     * Get the current cause name being tracked for this ad.
     */
    fun getCurrentCauseName(): String? = currentCauseName

    /**
     * Get the current transaction ID for server-side verification.
     */
    fun getCurrentTransactionId(): String? = currentTransactionId
}
