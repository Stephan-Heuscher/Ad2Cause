package ch.heuscher.ad2cause.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import ch.heuscher.ad2cause.data.models.User
import ch.heuscher.ad2cause.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing authentication state and operations.
 * Handles Google Sign-In, sign-out, and user state.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    // Current Firebase user
    val currentUser: StateFlow<FirebaseUser?> = authRepository.currentUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Sign-in state
    val isSignedIn: StateFlow<Boolean> = authRepository.isSignedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Firestore user data
    private val _firestoreUser = MutableLiveData<User?>()
    val firestoreUser: LiveData<User?> = _firestoreUser

    // Sign-in result event
    private val _signInResult = MutableLiveData<Result<FirebaseUser>>()
    val signInResult: LiveData<Result<FirebaseUser>> = _signInResult

    // Sign-out result event
    private val _signOutResult = MutableLiveData<Result<Unit>>()
    val signOutResult: LiveData<Result<Unit>> = _signOutResult

    // Loading state
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Admin status
    private val _isAdmin = MutableLiveData(false)
    val isAdmin: LiveData<Boolean> = _isAdmin

    init {
        // Load user data if already signed in
        if (authRepository.getCurrentUserId() != null) {
            loadFirestoreUser()
            checkAdminStatus()
        }
    }

    /**
     * Get Google Sign-In client for use in UI
     */
    fun getGoogleSignInClient() = authRepository.getGoogleSignInClient()

    /**
     * Handle Google Sign-In result from Activity
     */
    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = authRepository.handleGoogleSignInResult(task)
            _signInResult.value = result

            if (result.isSuccess) {
                loadFirestoreUser()
                checkAdminStatus()
            }

            _isLoading.value = false
        }
    }

    /**
     * Sign in with email and password
     */
    fun signInWithEmailPassword(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = authRepository.signInWithEmailPassword(email, password)
            _signInResult.value = result

            if (result.isSuccess) {
                loadFirestoreUser()
                checkAdminStatus()
            }

            _isLoading.value = false
        }
    }

    /**
     * Create account with email and password
     */
    fun createAccountWithEmailPassword(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = authRepository.createAccountWithEmailPassword(email, password)
            _signInResult.value = result

            if (result.isSuccess) {
                loadFirestoreUser()
                checkAdminStatus()
            }

            _isLoading.value = false
        }
    }

    /**
     * Sign out from Firebase and Google
     */
    fun signOut() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _firestoreUser.value = null
                _isAdmin.value = false
                _signOutResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _signOutResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load user data from Firestore
     */
    private fun loadFirestoreUser() {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            val result = authRepository.getUserFromFirestore(userId)
            if (result.isSuccess) {
                _firestoreUser.value = result.getOrNull()
            }
        }
    }

    /**
     * Reload user data from Firestore
     */
    fun reloadUserData() {
        loadFirestoreUser()
        checkAdminStatus()
    }

    /**
     * Update active cause for current user
     */
    fun updateActiveCause(causeId: String?) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            authRepository.updateActiveCause(userId, causeId)
            loadFirestoreUser() // Reload to get updated data
        }
    }

    /**
     * Update total earned for current user
     */
    fun updateTotalEarned(amount: Double) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            authRepository.updateTotalEarned(userId, amount)
            loadFirestoreUser() // Reload to get updated data
        }
    }

    /**
     * Check if current user is an admin
     */
    private fun checkAdminStatus() {
        viewModelScope.launch {
            val isAdminUser = authRepository.isCurrentUserAdmin()
            _isAdmin.value = isAdminUser
        }
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? = authRepository.getCurrentUserId()

    /**
     * Reset event values
     */
    fun resetEvents() {
        _signInResult.value = null
        _signOutResult.value = null
    }
}
