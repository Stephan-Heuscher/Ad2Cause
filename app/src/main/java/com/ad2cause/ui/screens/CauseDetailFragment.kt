package com.ad2cause.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.ad2cause.R
import com.ad2cause.databinding.FragmentCauseDetailBinding
import com.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * Cause Detail Screen Fragment
 * Displays full details of a specific cause and allows setting it as active.
 */
class CauseDetailFragment : Fragment() {

    private lateinit var binding: FragmentCauseDetailBinding
    private lateinit var causeViewModel: CauseViewModel
    private var causeId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCauseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        causeViewModel = ViewModelProvider(requireActivity()).get(CauseViewModel::class.java)

        // Get cause ID from arguments
        causeId = arguments?.getInt("cause_id") ?: -1
        if (causeId == -1) {
            findNavController().popBackStack()
            return
        }

        loadCauseDetails()
        setupButton()
    }

    /**
     * Load and display cause details.
     */
    private fun loadCauseDetails() {
        lifecycleScope.launch {
            causeViewModel.getCauseById(causeId).collect { cause ->
                cause?.let {
                    binding.causeDetailImage.load(it.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_placeholder)
                        error(R.drawable.ic_placeholder)
                    }
                    binding.causeDetailName.text = it.name
                    binding.causeDetailCategory.text = it.category
                    binding.causeDetailDescription.text = it.description
                    binding.causeDetailEarnings.text = getString(R.string.total_earned, it.totalEarned)

                    // Update button state
                    updateButtonState(it.id)
                }
            }
        }
    }

    /**
     * Setup the button click listener.
     */
    private fun setupButton() {
        binding.setActiveCauseButton.setOnClickListener {
            lifecycleScope.launch {
                val cause = causeViewModel.getCauseById(causeId).collect { c ->
                    c?.let {
                        causeViewModel.setActiveCause(it)
                        Toast.makeText(
                            requireContext(),
                            "Active cause set to: ${it.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate back to home
                        findNavController().popBackStack(R.id.nav_home, false)
                    }
                }
            }
        }
    }

    /**
     * Update button state based on whether this is the active cause.
     */
    private fun updateButtonState(causeId: Int) {
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { activeCause ->
                val isActive = activeCause?.id == causeId
                binding.setActiveCauseButton.apply {
                    isEnabled = !isActive
                    text = if (isActive) {
                        getString(R.string.currently_active)
                    } else {
                        getString(R.string.set_as_active_cause)
                    }
                }
            }
        }
    }
}
