package ch.heuscher.ad2cause.data.repository

import ch.heuscher.ad2cause.data.database.CauseDao
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.data.models.FirestoreCause
import ch.heuscher.ad2cause.data.models.toFirestoreCause
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing Cause-related data operations.
 * Acts as a single source of truth for cause data, abstracting database operations.
 * Now includes Firestore sync capabilities.
 */
class CauseRepository(
    private val causeDao: CauseDao,
    private val firebaseRepository: FirebaseRepository? = null
) {

    /**
     * Get all causes as a Flow for reactive updates.
     */
    fun getAllCauses(): Flow<List<Cause>> = causeDao.getAllCauses()

    /**
     * Get all causes synchronously (blocking) for initialization checks.
     */
    suspend fun getAllCausesSync(): List<Cause> = causeDao.getAllCausesSync()

    /**
     * Get a specific cause by its ID.
     */
    suspend fun getCauseById(id: Int): Cause? = causeDao.getCauseById(id)

    /**
     * Insert a new cause into the database.
     */
    suspend fun insertCause(cause: Cause): Long = causeDao.insertCause(cause)

    /**
     * Update an existing cause.
     */
    suspend fun updateCause(cause: Cause) = causeDao.updateCause(cause)

    /**
     * Delete a cause from the database.
     */
    suspend fun deleteCause(cause: Cause) = causeDao.deleteCause(cause)

    /**
     * Search for causes by name.
     */
    fun searchCausesByName(query: String): Flow<List<Cause>> =
        causeDao.searchCausesByName(query)

    /**
     * Get all user-added causes.
     */
    fun getUserAddedCauses(): Flow<List<Cause>> = causeDao.getUserAddedCauses()

    /**
     * Get all pre-defined causes.
     */
    fun getPredefinedCauses(): Flow<List<Cause>> = causeDao.getPredefinedCauses()

    /**
     * Update the earnings for a specific cause.
     */
    suspend fun updateCauseEarnings(causeId: Int, earnedAmount: Double) {
        val cause = causeDao.getCauseById(causeId)
        cause?.let {
            val updatedCause = it.copy(totalEarned = it.totalEarned + earnedAmount)
            causeDao.updateCause(updatedCause)
        }
    }

    // ========== Firestore Sync Methods ==========

    /**
     * Get a cause by its Firestore ID.
     */
    suspend fun getCauseByFirestoreId(firestoreId: String): Cause? =
        causeDao.getCauseByFirestoreId(firestoreId)

    /**
     * Get all approved causes.
     */
    fun getApprovedCauses(): Flow<List<Cause>> = causeDao.getApprovedCauses()

    /**
     * Get all pending causes.
     */
    fun getPendingCauses(): Flow<List<Cause>> = causeDao.getPendingCauses()

    /**
     * Sync approved causes from Firestore to local Room database.
     * This downloads all approved causes and updates the local cache.
     */
    suspend fun syncCausesFromFirestore(): Result<Unit> {
        return try {
            val firebaseRepo = firebaseRepository
                ?: return Result.failure(Exception("Firebase not initialized"))

            // Get approved causes from Firestore
            val result = firebaseRepo.syncApprovedCauses()
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }

            val firestoreCauses = result.getOrNull() ?: emptyList()

            // Sync to local database
            firestoreCauses.forEach { firestoreCause ->
                // Check if cause already exists locally
                val existingCause = causeDao.getCauseByFirestoreId(firestoreCause.firestoreId)

                if (existingCause != null) {
                    // Update existing cause, preserving local totalEarned
                    val updatedCause = firestoreCause.toRoomCause(
                        localId = existingCause.id,
                        totalEarned = existingCause.totalEarned
                    )
                    causeDao.updateCause(updatedCause)
                } else {
                    // Insert new cause
                    val newCause = firestoreCause.toRoomCause()
                    causeDao.insertCause(newCause)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to sync causes: ${e.message}"))
        }
    }

    /**
     * Create a new cause and upload to Firestore (will be pending approval).
     */
    suspend fun createUserCause(
        cause: Cause,
        userId: String,
        imageUrl: String
    ): Result<String> {
        return try {
            val firebaseRepo = firebaseRepository
                ?: return Result.failure(Exception("Firebase not initialized"))

            // Create Firestore cause
            val firestoreCause = cause.toFirestoreCause().copy(
                imageUrl = imageUrl,
                createdBy = userId
            )

            val result = firebaseRepo.createCause(firestoreCause, userId)
            if (result.isFailure) {
                return result
            }

            val firestoreId = result.getOrNull()!!

            // Also save locally with pending status
            val localCause = cause.copy(
                firestoreId = firestoreId,
                imageUrl = imageUrl,
                createdBy = userId
            )
            causeDao.insertCause(localCause)

            Result.success(firestoreId)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create cause: ${e.message}"))
        }
    }

    /**
     * Approve a cause (admin only).
     */
    suspend fun approveCause(causeId: String, adminUserId: String): Result<Unit> {
        return try {
            val firebaseRepo = firebaseRepository
                ?: return Result.failure(Exception("Firebase not initialized"))

            val result = firebaseRepo.approveCause(causeId, adminUserId)
            if (result.isFailure) {
                return result
            }

            // Update local database
            val localCause = causeDao.getCauseByFirestoreId(causeId)
            localCause?.let {
                val updatedCause = it.copy(
                    status = "APPROVED",
                    approvedBy = adminUserId,
                    approvedAt = System.currentTimeMillis()
                )
                causeDao.updateCause(updatedCause)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to approve cause: ${e.message}"))
        }
    }

    /**
     * Reject a cause (admin only).
     */
    suspend fun rejectCause(causeId: String): Result<Unit> {
        return try {
            val firebaseRepo = firebaseRepository
                ?: return Result.failure(Exception("Firebase not initialized"))

            val result = firebaseRepo.rejectCause(causeId)
            if (result.isFailure) {
                return result
            }

            // Update local database
            val localCause = causeDao.getCauseByFirestoreId(causeId)
            localCause?.let {
                val updatedCause = it.copy(status = "REJECTED")
                causeDao.updateCause(updatedCause)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to reject cause: ${e.message}"))
        }
    }

    /**
     * Listen to approved causes from Firestore in real-time.
     */
    fun observeFirestoreCauses(): Flow<List<FirestoreCause>>? {
        return firebaseRepository?.getApprovedCauses()
    }
}
