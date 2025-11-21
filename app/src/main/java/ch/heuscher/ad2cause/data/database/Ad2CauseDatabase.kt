package ch.heuscher.ad2cause.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.heuscher.ad2cause.data.models.Cause

/**
 * Room Database for the Ad2Cause application.
 * Manages all local data persistence.
 *
 * Database Schema:
 * - causes: Stores information about all causes (pre-defined and user-added)
 *
 * Version History:
 * - v1: Initial schema
 * - v2: Added Firestore sync fields (removed in v3)
 * - v3: Removed Firebase dependencies, simplified schema
 * - v4: Removed category field
 * - v5: Bump version to force migration
 */
@Database(entities = [Cause::class], version = 5, exportSchema = false)
abstract class Ad2CauseDatabase : RoomDatabase() {

    /**
     * Abstract method to access the CauseDao.
     */
    abstract fun causeDao(): CauseDao

    companion object {
        @Volatile
        private var INSTANCE: Ad2CauseDatabase? = null

        /**
         * Singleton pattern to get database instance.
         * Uses double-checked locking for thread safety.
         */
        fun getDatabase(context: Context): Ad2CauseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Ad2CauseDatabase::class.java,
                    "ad2cause_database"
                )
                    .fallbackToDestructiveMigration() // Simplified: recreate on schema change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
