package com.ad2cause.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ad2cause.data.models.Cause

/**
 * Room Database for the Ad2Cause application.
 * Manages all local data persistence.
 *
 * Database Schema:
 * - causes: Stores information about all causes (pre-defined and user-added)
 */
@Database(entities = [Cause::class], version = 1, exportSchema = false)
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
                    .fallbackToDestructiveMigration() // For development; use proper migrations in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
