package com.ad2cause.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ad2cause.R
import com.ad2cause.data.models.Cause
import com.ad2cause.databinding.FragmentCausesBinding
import com.ad2cause.ui.adapters.CauseAdapter
import com.ad2cause.viewmodel.CauseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        setupSearchBar()
        setupFab()
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
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /**
     * Setup search bar functionality.
     */
    private fun setupSearchBar() {
        binding.searchBar.setOnClickListener {
            // In a real app, this would open a search interface
            // For now, we'll keep it simple
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

        // Get input fields
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

            if (name.isEmpty() || category.isEmpty() || description.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
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
     * Observe data from ViewModel.
     */
    private fun observeData() {
        lifecycleScope.launch {
            causeViewModel.allCauses.collect { causes ->
                causeAdapter.submitList(causes)
                binding.emptyStateText.visibility = if (causes.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
