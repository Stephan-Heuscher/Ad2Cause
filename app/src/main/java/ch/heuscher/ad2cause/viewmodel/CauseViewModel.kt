package ch.heuscher.ad2cause.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ch.heuscher.ad2cause.data.database.Ad2CauseDatabase
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.data.repository.CauseRepository
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

    private val repository: CauseRepository
    private val sharedPreferences = application.getSharedPreferences("ad2cause_prefs", Context.MODE_PRIVATE)

    // All causes
    val allCauses: Flow<List<Cause>>
    
    // Currently selected (active) cause
    private val _activeCause = MutableStateFlow<Cause?>(null)
    val activeCause: StateFlow<Cause?> = _activeCause.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Category filter
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Event for UI feedback
    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    init {
        val database = Ad2CauseDatabase.getDatabase(application)
        repository = CauseRepository(database.causeDao())
        allCauses = repository.getAllCauses()

        // Load the active cause from SharedPreferences
        loadActiveCause()

        // Initialize database with sample data if empty
        initializeSampleData()
    }

    /**
     * Initialize the database with sample causes on first launch.
     */
    private fun initializeSampleData() {
        viewModelScope.launch {
            // This will be handled in MainActivity
        }
    }

    /**
     * Load the active cause from SharedPreferences.
     */
    private fun loadActiveCause() {
        viewModelScope.launch {
            val activeCauseId = sharedPreferences.getInt("active_cause_id", -1)
            if (activeCauseId != -1) {
                val cause = repository.getCauseById(activeCauseId)
                _activeCause.value = cause
            }
        }
    }

    /**
     * Set a cause as the active cause.
     */
    fun setActiveCause(cause: Cause) {
        _activeCause.value = cause
        sharedPreferences.edit().putInt("active_cause_id", cause.id).apply()
        _uiEvent.value = UiEvent.CauseSelected(cause.name)
    }

    /**
     * Get a cause by ID.
     */
    fun getCauseById(id: Int): Flow<Cause?> {
        return kotlinx.coroutines.flow.flow {
            emit(repository.getCauseById(id))
        }
    }

    /**
     * Insert a new cause (typically user-added).
     */
    fun addNewCause(name: String, description: String, category: String) {
        viewModelScope.launch {
            val newCause = Cause(
                name = name,
                description = description,
                category = category,
                imageUrl = "https://via.placeholder.com/200?text=${name.take(3)}", // Placeholder image
                isUserAdded = true,
                totalEarned = 0.0
            )
            repository.insertCause(newCause)
            _uiEvent.value = UiEvent.CauseAdded(name)
        }
    }

    /**
     * Update the earnings for the active cause.
     */
    fun updateActiveCauseEarnings(amount: Double) {
        val cause = _activeCause.value ?: return
        viewModelScope.launch {
            repository.updateCauseEarnings(cause.id, amount)
            // Reload the active cause to reflect updated earnings
            loadActiveCause()
        }
    }

    /**
     * Search for causes by name.
     */
    fun searchCauses(query: String): Flow<List<Cause>> {
        _searchQuery.value = query
        return if (query.isEmpty()) {
            repository.getAllCauses()
        } else {
            repository.searchCausesByName(query)
        }
    }

    /**
     * Clear search query.
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Filter causes by category.
     */
    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
    }

    /**
     * Get filtered causes based on search query and category.
     */
    fun getFilteredCauses(searchQuery: String, category: String?): Flow<List<Cause>> {
        return kotlinx.coroutines.flow.flow {
            repository.getAllCauses().collect { allCauses ->
                var filtered = allCauses

                // Filter by search query
                if (searchQuery.isNotEmpty()) {
                    filtered = filtered.filter { cause ->
                        cause.name.contains(searchQuery, ignoreCase = true) ||
                        cause.description.contains(searchQuery, ignoreCase = true)
                    }
                }

                // Filter by category
                if (category != null && category != "All") {
                    filtered = filtered.filter { cause ->
                        cause.category.equals(category, ignoreCase = true)
                    }
                }

                emit(filtered)
            }
        }
    }

    /**
     * Delete a cause from the database.
     */
    fun deleteCause(cause: Cause) {
        viewModelScope.launch {
            repository.deleteCause(cause)
            _uiEvent.value = UiEvent.CauseDeleted(cause.name)
        }
    }

    /**
     * Sealed class for UI events.
     */
    sealed class UiEvent {
        data class CauseSelected(val causeName: String) : UiEvent()
        data class CauseAdded(val causeName: String) : UiEvent()
        data class CauseDeleted(val causeName: String) : UiEvent()
    }
}
