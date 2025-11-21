package ch.heuscher.ad2cause.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Type of ad that generated the earning
 */
enum class AdType {
    INTERACTIVE,        // Higher-paying interactive ads
    NON_INTERACTIVE,   // Standard video ads
    ADS_18_PLUS        // 18+ ads (configurable, default hidden)
}

/**
 * Data model representing an individual earning event.
 * Each time a user watches an ad, an EarningHistory record is created.
 * This is stored in Firestore under users/{userId}/earnings/{earningId}
 *
 * @property earningId Unique identifier for this earning event (Firestore document ID)
 * @property userId ID of the user who earned this reward
 * @property causeId Firestore ID of the cause that received this earning
 * @property causeName Name of the cause (denormalized for quick display)
 * @property amount Amount earned (e.g., 0.01 for standard ad)
 * @property adType Type of ad watched (INTERACTIVE or NON_INTERACTIVE)
 * @property timestamp When the earning was recorded
 * @property deviceId Optional device identifier for analytics
 */
data class EarningHistory(
    @DocumentId
    val earningId: String = "",
    val userId: String = "",
    val causeId: String = "",
    val causeName: String = "",
    val amount: Double = 0.0,
    val adType: String = AdType.NON_INTERACTIVE.name,
    @ServerTimestamp
    val timestamp: Date? = null,
    val deviceId: String? = null
)

/**
 * Summary of earnings for a specific cause
 * Used for displaying statistics
 */
data class CauseEarningSummary(
    val causeId: String,
    val causeName: String,
    val totalEarned: Double,
    val earningCount: Int,
    val lastEarningDate: Date?
)

/**
 * Summary of earnings for a user
 * Used for displaying user statistics
 */
data class UserEarningSummary(
    val userId: String,
    val totalEarned: Double,
    val totalAdsWatched: Int,
    val activeCauseName: String?,
    val topCause: CauseEarningSummary?
)
