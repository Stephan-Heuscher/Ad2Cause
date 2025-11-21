package ch.heuscher.ad2cause.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data model representing a Cause.
 * This entity is stored in the Room database (local storage only).
 *
 * @property id Unique identifier for the cause in Room database (Primary Key)
 * @property name Name of the cause
 * @property description Detailed description of the cause
 * @property imageUrl URL for the cause's logo or representative image (required)
 * @property isUserAdded Boolean flag to distinguish pre-defined causes from user-added ones
 * @property totalEarned Total amount of money earned for this cause
 */
@Entity(tableName = "causes")
data class Cause(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val imageUrl: String,
    val isUserAdded: Boolean = false,
    val totalEarned: Double = 0.0
)
