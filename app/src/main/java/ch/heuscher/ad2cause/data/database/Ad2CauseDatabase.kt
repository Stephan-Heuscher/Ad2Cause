package ch.heuscher.ad2cause.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.data.models.CauseStatus

/**
 * Room Database for the Ad2Cause application.
 * Manages all local data persistence.
 *
 * Database Schema:
 * - causes: Stores information about all causes (pre-defined and user-added)
 *
 * Version History:
 * - v1: Initial schema
 * - v2: Added Firestore sync fields (firestoreId, status, createdBy, timestamps)
 */
@Database(entities = [Cause::class], version = 2, exportSchema = false)
abstract class Ad2CauseDatabase : RoomDatabase() {

    /**
     * Abstract method to access the CauseDao.
     */
    abstract fun causeDao(): CauseDao

    companion object {
        @Volatile
        private var INSTANCE: Ad2CauseDatabase? = null

        /**
         * Migration from version 1 to version 2.
         * Adds Firestore sync fields to the causes table.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add new columns with default values
                database.execSQL("ALTER TABLE causes ADD COLUMN firestoreId TEXT DEFAULT NULL")
                database.execSQL("ALTER TABLE causes ADD COLUMN status TEXT NOT NULL DEFAULT '${CauseStatus.APPROVED.name}'")
                database.execSQL("ALTER TABLE causes ADD COLUMN createdBy TEXT DEFAULT NULL")
                database.execSQL("ALTER TABLE causes ADD COLUMN createdAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
                database.execSQL("ALTER TABLE causes ADD COLUMN approvedAt INTEGER DEFAULT NULL")
                database.execSQL("ALTER TABLE causes ADD COLUMN approvedBy TEXT DEFAULT NULL")
            }
        }

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
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
