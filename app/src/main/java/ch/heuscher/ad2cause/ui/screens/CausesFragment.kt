package ch.heuscher.ad2cause.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.databinding.FragmentCausesBinding
import ch.heuscher.ad2cause.ui.adapters.CauseAdapter
import ch.heuscher.ad2cause.viewmodel.AuthViewModel
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
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
    private lateinit var authViewModel: AuthViewModel
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

        causeViewModel = ViewModelProvider(requireActivity())[CauseViewModel::class.java]
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

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
        causeAdapter = CauseAdapter(
            onCauseClick = { cause ->
                // Navigate to cause detail screen
                val bundle = Bundle().apply {
                    putInt("cause_id", cause.id)
                }
                findNavController().navigate(R.id.action_causes_to_detail, bundle)
            },
            onSetActive = { cause ->
                // Set cause as active directly
                causeViewModel.setActiveCause(cause)
                Toast.makeText(
                    requireContext(),
                    "Active cause: ${cause.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        binding.causesRecyclerView.apply {
            adapter = causeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    /**
     * Setup the Floating Action Button to add new causes.
     */
    private fun setupFab() {
        binding.addCauseFab.setOnClickListener {
            findNavController().navigate(R.id.action_causes_to_request)
        }
    }


    /**
     * Setup search functionality
     */
    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQueryFlow.value = s?.toString() ?: ""
                binding.clearSearchButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearSearchButton.setOnClickListener {
            binding.searchEditText.text?.clear()
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
