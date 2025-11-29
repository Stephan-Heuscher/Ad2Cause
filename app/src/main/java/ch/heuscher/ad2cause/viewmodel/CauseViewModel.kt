package ch.heuscher.ad2cause.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ch.heuscher.ad2cause.data.models.Cause
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Cause-related data and logic.
 * Handles communication between the UI and data layer.
 */
class CauseViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("ad2cause_prefs", Context.MODE_PRIVATE)

    // All causes (in-memory)
    private val _allCauses = kotlinx.coroutines.flow.MutableStateFlow<List<Cause>>(emptyList())
    val allCauses: kotlinx.coroutines.flow.StateFlow<List<Cause>> = _allCauses
    
    // Currently selected (active) cause
    private val _activeCause = MutableStateFlow<Cause?>(null)
    val activeCause: StateFlow<Cause?> = _activeCause.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Event for UI feedback
    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    init {
        // No DB repository used: causes are provided from MainActivity at runtime.
        // Load the active cause from SharedPreferences (will be matched to in-memory causes once they're set)
        loadActiveCause()
    }

    /**
     * Initialize the database with sample causes on first launch.
     */
    private fun initializeSampleData() {
        // no-op: initial causes are supplied by MainActivity via setCauses()
    }

    /**
     * Load the active cause from SharedPreferences.
     */
    private fun loadActiveCause() {
        // Find existing active cause id from prefs and try to match to in-memory causes
        val activeCauseId = sharedPreferences.getInt("active_cause_id", -1)
        if (activeCauseId != -1) {
            // If causes were already set, try to find it
            val cause = _allCauses.value.find { it.id == activeCauseId }
            _activeCause.value = cause
        }
    }

    /**
     * Set a cause as the active cause.
     */
    fun setActiveCause(cause: Cause) {
        _activeCause.value = cause
        // store active cause id in preferences (no DB persistence)
        sharedPreferences.edit().putInt("active_cause_id", cause.id).apply()
        _uiEvent.value = UiEvent.CauseSelected(cause.name)
    }

    /**
     * Get a cause by ID.
     */
    fun getCauseById(id: Int): kotlinx.coroutines.flow.Flow<Cause?> {
        return _allCauses.map { list -> list.find { it.id == id } }
    }

    /**
     * Insert a new cause (typically user-added).
     */
    fun addNewCause(name: String, description: String) {
        // Adding causes is disabled when causes are managed in-memory by MainActivity
        _uiEvent.value = UiEvent.OperationNotAllowed("Adding new causes is disabled")
    }

    /**
     * Update the earnings for the active cause.
     */
    fun updateActiveCauseEarnings(amount: Double) {
        val cause = _activeCause.value ?: return
        // Update in-memory cause earnings
        val updated = cause.copy(totalEarned = cause.totalEarned + amount)
        _activeCause.value = updated
        // update list
        _allCauses.value = _allCauses.value.map { if (it.id == updated.id) updated else it }
    }

    /**
     * Search for causes by name.
     */
    fun searchCauses(query: String): kotlinx.coroutines.flow.Flow<List<Cause>> {
        _searchQuery.value = query
        val snapshot = _allCauses.value
        return kotlinx.coroutines.flow.flow {
            if (query.isEmpty()) emit(snapshot)
            else emit(snapshot.filter { it.name.contains(query, ignoreCase = true) })
        }
    }

    /**
     * Clear search query.
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Delete a cause from the database.
     */
    fun deleteCause(cause: Cause) {
        // Deleting causes is disabled when causes are managed in-memory by MainActivity
        _uiEvent.value = UiEvent.OperationNotAllowed("Deleting causes is disabled")
    }

    /**
     * Sealed class for UI events.
     */
    sealed class UiEvent {
        data class CauseSelected(val causeName: String) : UiEvent()
        data class CauseAdded(val causeName: String) : UiEvent()
        data class CauseDeleted(val causeName: String) : UiEvent()
        data class OperationNotAllowed(val message: String) : UiEvent()
    }

    /**
     * Set causes in-memory (should be called from MainActivity at app start).
     */
    fun setCauses(causes: List<Cause>) {
        _allCauses.value = causes

        // If an active cause id was previously saved, try to match it now
        val activeId = sharedPreferences.getInt("active_cause_id", -1)
        if (activeId != -1) {
            _activeCause.value = _allCauses.value.find { it.id == activeId }
        } else if (_allCauses.value.isNotEmpty() && _activeCause.value == null) {
            // default: first cause
            _activeCause.value = _allCauses.value.first()
            sharedPreferences.edit().putInt("active_cause_id", _activeCause.value!!.id).apply()
        }
    }
}
