package ch.heuscher.ad2cause.ui.screens

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
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.databinding.FragmentCauseDetailBinding
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
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

        setupToolbar()
        loadCauseDetails()
        setupButton()
    }

    /**
     * Setup the toolbar with back button.
     */
    private fun setupToolbar() {
        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Load and display cause details.
     */
    private fun loadCauseDetails() {
        lifecycleScope.launch {
            causeViewModel.getCauseById(causeId).collect { cause ->
                cause?.let {
                    // Load hero image
                    binding.causeDetailImage.load(it.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_placeholder)
                        error(R.drawable.ic_placeholder)
                    }
                    
                    // Set cause name in CollapsingToolbar
                    binding.collapsingToolbar.title = it.name
                    binding.causeDetailName.text = it.name
                    binding.causeDetailDescription.text = it.description
                    
                    // Display earnings as number only (label is separate)
                    binding.causeDetailEarnings.text = String.format("%.0f", it.totalEarned)

                    // Update button and status state
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
                causeViewModel.getCauseById(causeId).collect { c ->
                    c?.let {
                        causeViewModel.setActiveCause(it)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.active_cause_set, it.name),
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate back to home
                        requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                            R.id.bottomNavigation
                        )?.selectedItemId = R.id.nav_home
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
                
                // Update status text
                binding.causeStatusText.text = if (isActive) {
                    getString(R.string.active)
                } else {
                    getString(R.string.inactive)
                }
                binding.causeStatusText.setTextColor(
                    if (isActive) resources.getColor(R.color.success, null)
                    else resources.getColor(R.color.text_secondary, null)
                )
                
                // Update button
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
