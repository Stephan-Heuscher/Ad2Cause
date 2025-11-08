package ch.heuscher.ad2cause.data.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Repository for managing Firebase Authentication operations.
 * Handles Google Sign-In and user state management.
 */
class AuthRepository(private val context: Context) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _isSignedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn

    init {
        // Listen to auth state changes
        firebaseAuth.addAuthStateListener { auth ->
            _currentUser.value = auth.currentUser
            _isSignedIn.value = auth.currentUser != null
        }
    }

    /**
     * Get Google Sign-In client configured for this app
     */
    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    /**
     * Handle the result of Google Sign-In
     * @param task The Google Sign-In task result
     * @return Result with success or error message
     */
    suspend fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>): Result<FirebaseUser> {
        return try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {
                // Create or update user in Firestore
                createOrUpdateUserInFirestore(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Authentication failed: User is null"))
            }
        } catch (e: ApiException) {
            Result.failure(Exception("Google Sign-In failed: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Authentication error: ${e.message}"))
        }
    }

    /**
     * Sign out from Firebase and Google
     */
    suspend fun signOut() {
        try {
            firebaseAuth.signOut()
            getGoogleSignInClient().signOut().await()
            _currentUser.value = null
            _isSignedIn.value = false
        } catch (e: Exception) {
            // Even if Google sign out fails, Firebase is signed out
            throw Exception("Sign out error: ${e.message}")
        }
    }

    /**
     * Get current Firebase user ID
     */
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    /**
     * Create or update user document in Firestore
     */
    private suspend fun createOrUpdateUserInFirestore(firebaseUser: FirebaseUser) {
        try {
            val userId = firebaseUser.uid
            val userRef = firestore.collection("users").document(userId)

            // Check if user already exists
            val userDoc = userRef.get().await()

            if (userDoc.exists()) {
                // Update last sign-in time
                userRef.update(
                    mapOf(
                        "lastSignInAt" to Date(),
                        "email" to (firebaseUser.email ?: ""),
                        "displayName" to (firebaseUser.displayName ?: ""),
                        "photoUrl" to (firebaseUser.photoUrl?.toString() ?: "")
                    )
                ).await()
            } else {
                // Create new user document
                val user = User(
                    userId = userId,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    activeCauseId = null,
                    totalEarned = 0.0,
                    isAdmin = false,
                    createdAt = Date(),
                    lastSignInAt = Date()
                )
                userRef.set(user).await()
            }
        } catch (e: Exception) {
            throw Exception("Failed to create/update user in Firestore: ${e.message}")
        }
    }

    /**
     * Get user data from Firestore
     */
    suspend fun getUserFromFirestore(userId: String): Result<User> {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Failed to parse user data"))
                }
            } else {
                Result.failure(Exception("User document not found"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get user from Firestore: ${e.message}"))
        }
    }

    /**
     * Update user's active cause in Firestore
     */
    suspend fun updateActiveCause(userId: String, causeId: String?): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("activeCauseId", causeId)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update active cause: ${e.message}"))
        }
    }

    /**
     * Update user's total earned amount in Firestore
     */
    suspend fun updateTotalEarned(userId: String, amount: Double): Result<Unit> {
        return try {
            val userRef = firestore.collection("users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val currentTotal = snapshot.getDouble("totalEarned") ?: 0.0
                transaction.update(userRef, "totalEarned", currentTotal + amount)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update total earned: ${e.message}"))
        }
    }

    /**
     * Check if current user is an admin
     */
    suspend fun isCurrentUserAdmin(): Boolean {
        return try {
            val userId = getCurrentUserId() ?: return false
            val userDoc = firestore.collection("users").document(userId).get().await()
            userDoc.getBoolean("isAdmin") ?: false
        } catch (e: Exception) {
            false
        }
    }
}
