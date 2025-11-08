package ch.heuscher.ad2cause.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import ch.heuscher.ad2cause.data.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

/**
 * Repository for managing Firestore and Firebase Storage operations.
 * Handles causes, earnings, and image uploads.
 */
class FirebaseRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    // Collections
    private val causesCollection = firestore.collection("causes")
    private val usersCollection = firestore.collection("users")

    /**
     * Upload cause image to Firebase Storage
     * @param imageUri Local URI of the image to upload
     * @return URL of the uploaded image
     */
    suspend fun uploadCauseImage(imageUri: Uri): Result<String> {
        return try {
            val imageId = UUID.randomUUID().toString()
            val storageRef = storage.reference.child("causes/$imageId.jpg")

            val uploadTask = storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(Exception("Failed to upload image: ${e.message}"))
        }
    }

    /**
     * Create a new cause in Firestore (pending approval)
     * @param cause The cause to create
     * @param userId ID of the user creating the cause
     * @return Firestore document ID of the created cause
     */
    suspend fun createCause(cause: FirestoreCause, userId: String): Result<String> {
        return try {
            val causeData = cause.copy(
                createdBy = userId,
                status = CauseStatus.PENDING.name,
                isUserAdded = true
            )

            val docRef = causesCollection.add(causeData).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create cause: ${e.message}"))
        }
    }

    /**
     * Get all approved causes from Firestore as a Flow
     */
    fun getApprovedCauses(): Flow<List<FirestoreCause>> = callbackFlow {
        val listener = causesCollection
            .whereEqualTo("status", CauseStatus.APPROVED.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val causes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<FirestoreCause>()?.copy(firestoreId = doc.id)
                } ?: emptyList()

                trySend(causes)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get all pending causes (for admins)
     */
    fun getPendingCauses(): Flow<List<FirestoreCause>> = callbackFlow {
        val listener = causesCollection
            .whereEqualTo("status", CauseStatus.PENDING.name)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val causes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<FirestoreCause>()?.copy(firestoreId = doc.id)
                } ?: emptyList()

                trySend(causes)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get causes created by a specific user
     */
    fun getUserCauses(userId: String): Flow<List<FirestoreCause>> = callbackFlow {
        val listener = causesCollection
            .whereEqualTo("createdBy", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val causes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<FirestoreCause>()?.copy(firestoreId = doc.id)
                } ?: emptyList()

                trySend(causes)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Approve a cause (admin only)
     * @param causeId Firestore document ID
     * @param adminUserId ID of the admin approving
     */
    suspend fun approveCause(causeId: String, adminUserId: String): Result<Unit> {
        return try {
            causesCollection.document(causeId).update(
                mapOf(
                    "status" to CauseStatus.APPROVED.name,
                    "approvedBy" to adminUserId,
                    "approvedAt" to Date()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to approve cause: ${e.message}"))
        }
    }

    /**
     * Reject a cause (admin only)
     * @param causeId Firestore document ID
     */
    suspend fun rejectCause(causeId: String): Result<Unit> {
        return try {
            causesCollection.document(causeId).update(
                "status", CauseStatus.REJECTED.name
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to reject cause: ${e.message}"))
        }
    }

    /**
     * Delete a cause (admin only)
     */
    suspend fun deleteCause(causeId: String): Result<Unit> {
        return try {
            causesCollection.document(causeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete cause: ${e.message}"))
        }
    }

    /**
     * Record an earning event
     * @param userId ID of the user who earned
     * @param causeId Firestore ID of the cause
     * @param causeName Name of the cause
     * @param amount Amount earned
     * @param adType Type of ad watched
     */
    suspend fun recordEarning(
        userId: String,
        causeId: String,
        causeName: String,
        amount: Double,
        adType: AdType
    ): Result<String> {
        return try {
            val earning = EarningHistory(
                userId = userId,
                causeId = causeId,
                causeName = causeName,
                amount = amount,
                adType = adType.name,
                timestamp = Date()
            )

            val docRef = usersCollection
                .document(userId)
                .collection("earnings")
                .add(earning)
                .await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to record earning: ${e.message}"))
        }
    }

    /**
     * Get user's earning history
     */
    fun getUserEarnings(userId: String): Flow<List<EarningHistory>> = callbackFlow {
        val listener = usersCollection
            .document(userId)
            .collection("earnings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val earnings = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<EarningHistory>()?.copy(earningId = doc.id)
                } ?: emptyList()

                trySend(earnings)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Get earnings for a specific cause (all users)
     */
    suspend fun getCauseEarnings(causeId: String): Result<Double> {
        return try {
            // This would require a collection group query or aggregation
            // For now, we'll return 0 and suggest using Cloud Functions for aggregation
            Result.success(0.0)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get cause earnings: ${e.message}"))
        }
    }

    /**
     * Get user's earning summary
     */
    suspend fun getUserEarningSummary(userId: String): Result<UserEarningSummary> {
        return try {
            val userDoc = usersCollection.document(userId).get().await()
            val totalEarned = userDoc.getDouble("totalEarned") ?: 0.0

            val earningsSnapshot = usersCollection
                .document(userId)
                .collection("earnings")
                .get()
                .await()

            val totalAdsWatched = earningsSnapshot.size()

            val summary = UserEarningSummary(
                userId = userId,
                totalEarned = totalEarned,
                totalAdsWatched = totalAdsWatched,
                activeCauseName = null, // Can be populated separately
                topCause = null // Would need to calculate from earnings
            )

            Result.success(summary)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get earning summary: ${e.message}"))
        }
    }

    /**
     * Sync approved causes to local database
     * Returns list of approved causes
     */
    suspend fun syncApprovedCauses(): Result<List<FirestoreCause>> {
        return try {
            val snapshot = causesCollection
                .whereEqualTo("status", CauseStatus.APPROVED.name)
                .get()
                .await()

            val causes = snapshot.documents.mapNotNull { doc ->
                doc.toObject<FirestoreCause>()?.copy(firestoreId = doc.id)
            }

            Result.success(causes)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to sync causes: ${e.message}"))
        }
    }

    /**
     * Get a specific cause by Firestore ID
     */
    suspend fun getCauseById(causeId: String): Result<FirestoreCause> {
        return try {
            val doc = causesCollection.document(causeId).get().await()
            val cause = doc.toObject<FirestoreCause>()?.copy(firestoreId = doc.id)
            if (cause != null) {
                Result.success(cause)
            } else {
                Result.failure(Exception("Cause not found"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get cause: ${e.message}"))
        }
    }
}
