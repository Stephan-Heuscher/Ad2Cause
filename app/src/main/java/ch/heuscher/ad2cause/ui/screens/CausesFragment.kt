package ch.heuscher.ad2cause.ui.screens

import android.content.Intent
import android.net.Uri
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
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

/**
 * Causes Screen Fragment
 * Displays a list of all causes with search functionality.
 * Users can request new causes via email.
 */
class CausesFragment : Fragment() {

    private lateinit var binding: FragmentCausesBinding
    private lateinit var causeViewModel: CauseViewModel
    private lateinit var causeAdapter: CauseAdapter

    private val searchQueryFlow = MutableStateFlow("")

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

        setupRecyclerView()
        setupSearch()
        setupFab()
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
     * Setup the FAB to request a new cause via email
     */
    private fun setupFab() {
        binding.addCauseFab.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("stv.heuscher@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "My Cause")
            }

            try {
                startActivity(emailIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "No email app found",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                // Search without category filtering
                if (query.isEmpty()) {
                    causeViewModel.allCauses.collect { causes ->
                        causeAdapter.submitList(causes)
                        binding.emptyStateText.visibility = if (causes.isEmpty()) View.VISIBLE else View.GONE
                    }
                } else {
                    causeViewModel.searchCauses(query).collect { causes ->
                        causeAdapter.submitList(causes)
                        binding.emptyStateText.visibility = if (causes.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
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
