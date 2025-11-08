package ch.heuscher.ad2cause.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Status of a cause in the approval workflow
 */
enum class CauseStatus {
    PENDING,    // Waiting for admin approval
    APPROVED,   // Approved and visible to all users
    REJECTED    // Rejected by admin
}

/**
 * Data model representing a Cause.
 * This entity is stored in both Room database (local) and Firestore (cloud).
 *
 * @property id Unique identifier for the cause in Room database (Primary Key)
 * @property firestoreId Document ID in Firestore (null for local-only causes)
 * @property name Name of the cause
 * @property description Detailed description of the cause
 * @property category Category of the cause (e.g., "Environment", "Animals", "Education")
 * @property imageUrl URL for the cause's logo or representative image (required)
 * @property isUserAdded Boolean flag to distinguish pre-defined causes from user-added ones
 * @property totalEarned Total amount of money earned for this cause
 * @property status Approval status (PENDING/APPROVED/REJECTED)
 * @property createdBy User ID of the user who created this cause (null for pre-defined)
 * @property createdAt Timestamp when the cause was created
 * @property approvedAt Timestamp when the cause was approved (null if not approved)
 * @property approvedBy User ID of the admin who approved this cause (null if not approved)
 */
@Entity(tableName = "causes")
data class Cause(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firestoreId: String? = null,
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val isUserAdded: Boolean = false,
    val totalEarned: Double = 0.0,
    val status: String = CauseStatus.APPROVED.name, // Default to approved for pre-defined causes
    val createdBy: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null,
    val approvedBy: String? = null
)

/**
 * Firestore version of Cause (without Room annotations)
 * Used for serialization to/from Firestore
 */
data class FirestoreCause(
    @DocumentId
    val firestoreId: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isUserAdded: Boolean = false,
    val status: String = CauseStatus.PENDING.name,
    val createdBy: String? = null,
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val approvedAt: Date? = null,
    val approvedBy: String? = null
) {
    /**
     * Convert Firestore cause to Room cause for local storage
     */
    fun toRoomCause(localId: Int = 0, totalEarned: Double = 0.0): Cause {
        return Cause(
            id = localId,
            firestoreId = firestoreId,
            name = name,
            description = description,
            category = category,
            imageUrl = imageUrl,
            isUserAdded = isUserAdded,
            totalEarned = totalEarned,
            status = status,
            createdBy = createdBy,
            createdAt = createdAt?.time ?: System.currentTimeMillis(),
            approvedAt = approvedAt?.time,
            approvedBy = approvedBy
        )
    }
}

/**
 * Extension function to convert Room Cause to Firestore Cause
 */
fun Cause.toFirestoreCause(): FirestoreCause {
    return FirestoreCause(
        firestoreId = firestoreId ?: "",
        name = name,
        description = description,
        category = category,
        imageUrl = imageUrl,
        isUserAdded = isUserAdded,
        status = status,
        createdBy = createdBy,
        createdAt = Date(createdAt),
        approvedAt = approvedAt?.let { Date(it) },
        approvedBy = approvedBy
    )
}
