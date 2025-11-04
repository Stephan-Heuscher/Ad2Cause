package ch.heuscher.ad2cause.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.databinding.FragmentCausesBinding
import ch.heuscher.ad2cause.ui.adapters.CauseAdapter
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

/**
 * Causes Screen Fragment
 * Displays a list of all causes with search functionality.
 * Allows users to add new causes and navigate to cause details.
 */
class CausesFragment : Fragment() {

    private lateinit var binding: FragmentCausesBinding
    private lateinit var causeViewModel: CauseViewModel
    private lateinit var causeAdapter: CauseAdapter

    private val searchQueryFlow = MutableStateFlow("")
    private var selectedCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCausesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        causeViewModel = ViewModelProvider(requireActivity()).get(CauseViewModel::class.java)

        setupRecyclerView()
        setupFab()
        setupSearch()
        setupCategoryChips()
        observeData()
    }

    /**
     * Setup the RecyclerView for displaying causes.
     */
    private fun setupRecyclerView() {
        causeAdapter = CauseAdapter { cause ->
            // Navigate to cause detail screen
            val bundle = Bundle().apply {
                putInt("cause_id", cause.id)
            }
            findNavController().navigate(R.id.action_causes_to_detail, bundle)
        }

        binding.causesRecyclerView.apply {
            adapter = causeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    /**
     * Setup the Floating Action Button to add new causes.
     */
    private fun setupFab() {
        binding.addCauseFab.setOnClickListener {
            showAddCauseDialog()
        }
    }

    /**
     * Display a dialog to add a new cause.
     */
    private fun showAddCauseDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_cause, null)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        // Get input fields and layouts
        val nameInputLayout = dialogView.findViewById<com.google.android.material.textfield.TextInputLayout>(
            R.id.causeNameInputLayout
        )
        val categoryInputLayout = dialogView.findViewById<com.google.android.material.textfield.TextInputLayout>(
            R.id.causeCategoryInputLayout
        )
        val descriptionInputLayout = dialogView.findViewById<com.google.android.material.textfield.TextInputLayout>(
            R.id.causeDescriptionInputLayout
        )
        val nameInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.causeNameInput
        )
        val categoryInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.causeCategoryInput
        )
        val descriptionInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.causeDescriptionInput
        )
        val saveButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(
            R.id.saveCauseButton
        )
        val cancelButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(
            R.id.cancelButton
        )

        saveButton?.setOnClickListener {
            val name = nameInput?.text.toString().trim()
            val category = categoryInput?.text.toString().trim()
            val description = descriptionInput?.text.toString().trim()

            // Clear previous errors
            nameInputLayout?.error = null
            categoryInputLayout?.error = null
            descriptionInputLayout?.error = null

            // Validate inputs
            var hasError = false

            if (name.isEmpty()) {
                nameInputLayout?.error = "Name is required"
                hasError = true
            } else if (name.length < 3) {
                nameInputLayout?.error = "Name must be at least 3 characters"
                hasError = true
            }

            if (category.isEmpty()) {
                categoryInputLayout?.error = "Category is required"
                hasError = true
            }

            if (description.isEmpty()) {
                descriptionInputLayout?.error = "Description is required"
                hasError = true
            } else if (description.length < 10) {
                descriptionInputLayout?.error = "Description must be at least 10 characters"
                hasError = true
            }

            if (hasError) {
                return@setOnClickListener
            }

            causeViewModel.addNewCause(name, description, category)
            dialog.dismiss()
            Toast.makeText(
                requireContext(),
                getString(R.string.cause_added_success, name),
                Toast.LENGTH_SHORT
            ).show()
        }

        cancelButton?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Setup search functionality
     */
    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        val searchEditText = requireActivity().findViewById<EditText>(R.id.searchEditText)
        val clearButton = requireActivity().findViewById<ImageView>(R.id.clearSearchButton)

        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQueryFlow.value = s?.toString() ?: ""
                clearButton?.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton?.setOnClickListener {
            searchEditText?.text?.clear()
        }

        // Observe search query with debounce
        lifecycleScope.launch {
            searchQueryFlow.debounce(300).collect { query ->
                updateFilteredCauses(query, selectedCategory)
            }
        }
    }

    /**
     * Setup category filter chips
     */
    private fun setupCategoryChips() {
        binding.chipAll.setOnClickListener {
            selectedCategory = null
            updateFilteredCauses(searchQueryFlow.value, null)
        }

        binding.chipTechnology.setOnClickListener {
            selectedCategory = "Technology"
            updateFilteredCauses(searchQueryFlow.value, "Technology")
        }

        binding.chipHealth.setOnClickListener {
            selectedCategory = "Health"
            updateFilteredCauses(searchQueryFlow.value, "Health")
        }

        binding.chipEnvironment.setOnClickListener {
            selectedCategory = "Environment"
            updateFilteredCauses(searchQueryFlow.value, "Environment")
        }

        // Set "All" as default selected
        binding.chipAll.isChecked = true
    }

    /**
     * Update the filtered causes list
     */
    private fun updateFilteredCauses(searchQuery: String, category: String?) {
        lifecycleScope.launch {
            causeViewModel.getFilteredCauses(searchQuery, category).collect { causes ->
                causeAdapter.submitList(causes)
                binding.emptyStateText.visibility = if (causes.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * Observe data from ViewModel.
     */
    private fun observeData() {
        // Observe active cause to update adapter
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { activeCause ->
                causeAdapter.activeCauseId = activeCause?.id
            }
        }

        lifecycleScope.launch {
            causeViewModel.allCauses.collect { causes ->
                causeAdapter.submitList(causes)
                binding.emptyStateText.visibility = if (causes.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
