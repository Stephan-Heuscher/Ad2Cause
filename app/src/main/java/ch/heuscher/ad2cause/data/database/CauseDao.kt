package ch.heuscher.ad2cause.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ch.heuscher.ad2cause.data.models.Cause
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
     * Retrieve all causes synchronously (blocking).
     * Used for initialization checks.
     */
    @Query("SELECT * FROM causes")
    suspend fun getAllCausesSync(): List<Cause>

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

    /**
     * Get a cause by its Firestore ID.
     */
    @Query("SELECT * FROM causes WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getCauseByFirestoreId(firestoreId: String): Cause?

    /**
     * Get all approved causes.
     */
    @Query("SELECT * FROM causes WHERE status = 'APPROVED'")
    fun getApprovedCauses(): Flow<List<Cause>>

    /**
     * Get all pending causes.
     */
    @Query("SELECT * FROM causes WHERE status = 'PENDING'")
    fun getPendingCauses(): Flow<List<Cause>>

    /**
     * Delete all causes (used for sync).
     */
    @Query("DELETE FROM causes")
    suspend fun deleteAllCauses()

    /**
     * Insert or replace a cause (upsert).
     */
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun upsertCause(cause: Cause): Long
}
