package ch.heuscher.ad2cause.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Data model representing a User in Firestore.
 * Users sign in with Google to sync their data across devices.
 *
 * @property userId Unique user ID from Firebase Auth (same as document ID)
 * @property email User's email address from Google Sign-In
 * @property displayName User's display name from Google profile
 * @property photoUrl URL to user's Google profile photo
 * @property activeCauseId ID of the currently active cause for this user
 * @property totalEarned Total amount earned across all causes by this user
 * @property isAdmin Whether this user has admin privileges (can approve causes)
 * @property createdAt Timestamp when the user account was created
 * @property lastSignInAt Timestamp of the user's last sign-in
 */
data class User(
    @DocumentId
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val activeCauseId: String? = null,
    val totalEarned: Double = 0.0,
    val isAdmin: Boolean = false,
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val lastSignInAt: Date? = null
)
