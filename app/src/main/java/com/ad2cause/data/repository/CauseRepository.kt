package com.ad2cause.data.repository

import com.ad2cause.data.database.CauseDao
import com.ad2cause.data.models.Cause
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing Cause-related data operations.
 * Acts as a single source of truth for cause data, abstracting database operations.
 */
class CauseRepository(private val causeDao: CauseDao) {

    /**
     * Get all causes as a Flow for reactive updates.
     */
    fun getAllCauses(): Flow<List<Cause>> = causeDao.getAllCauses()

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
}
