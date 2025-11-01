package com.ad2cause.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ad2cause.data.models.Cause
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Cause entity.
 * Provides methods for database operations on causes.
 */
@Dao
interface CauseDao {

    /**
     * Insert a new cause into the database.
     */
    @Insert
    suspend fun insertCause(cause: Cause): Long

    /**
     * Update an existing cause in the database.
     */
    @Update
    suspend fun updateCause(cause: Cause)

    /**
     * Delete a cause from the database.
     */
    @Delete
    suspend fun deleteCause(cause: Cause)

    /**
     * Retrieve all causes from the database.
     * Returns a Flow for reactive updates.
     */
    @Query("SELECT * FROM causes")
    fun getAllCauses(): Flow<List<Cause>>

    /**
     * Retrieve a specific cause by its ID.
     */
    @Query("SELECT * FROM causes WHERE id = :id")
    suspend fun getCauseById(id: Int): Cause?

    /**
     * Search for causes by name (case-insensitive).
     */
    @Query("SELECT * FROM causes WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchCausesByName(searchQuery: String): Flow<List<Cause>>

    /**
     * Get all user-added causes.
     */
    @Query("SELECT * FROM causes WHERE isUserAdded = 1")
    fun getUserAddedCauses(): Flow<List<Cause>>

    /**
     * Get all pre-defined causes.
     */
    @Query("SELECT * FROM causes WHERE isUserAdded = 0")
    fun getPredefinedCauses(): Flow<List<Cause>>
}
