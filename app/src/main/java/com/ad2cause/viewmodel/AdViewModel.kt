package com.ad2cause.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * ViewModel for managing AdMob-related logic and state.
 * Handles rewarded ad interactions and reward callbacks.
 */
class AdViewModel(application: Application) : AndroidViewModel(application) {

    // Reward amount per ad
    companion object {
        const val REWARD_AMOUNT = 0.01 // $0.01 per ad
    }

    // Event when user earns reward
    private val _rewardEarnedEvent = MutableLiveData<Double>()
    val rewardEarnedEvent: LiveData<Double> = _rewardEarnedEvent

    // Ad loading state
    private val _isAdLoading = MutableLiveData(false)
    val isAdLoading: LiveData<Boolean> = _isAdLoading

    // Ad error event
    private val _adErrorEvent = MutableLiveData<String>()
    val adErrorEvent: LiveData<String> = _adErrorEvent

    /**
     * Called when a rewarded ad is completed successfully.
     * This triggers the reward event with the reward amount.
     */
    fun onRewardEarned() {
        _rewardEarnedEvent.value = REWARD_AMOUNT
    }

    /**
     * Called when starting to load an ad.
     */
    fun onAdLoadingStarted() {
        _isAdLoading.value = true
    }

    /**
     * Called when ad loading completes (success or failure).
     */
    fun onAdLoadingCompleted() {
        _isAdLoading.value = false
    }

    /**
     * Called when an ad error occurs.
     */
    fun onAdError(errorMessage: String) {
        _adErrorEvent.value = errorMessage
        _isAdLoading.value = false
    }

    /**
     * Resets the event values for reuse.
     */
    fun resetEvents() {
        _rewardEarnedEvent.value = null
        _adErrorEvent.value = null
    }
}
