package com.ad2cause.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ad2cause.R
import com.ad2cause.databinding.FragmentStatsBinding
import com.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * Stats Screen Fragment
 * Displays statistics about earnings (future implementation).
 * This is a placeholder for future features.
 */
class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var causeViewModel: CauseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        causeViewModel = ViewModelProvider(requireActivity()).get(CauseViewModel::class.java)

        observeData()
    }

    /**
     * Observe and display statistics.
     */
    private fun observeData() {
        lifecycleScope.launch {
            causeViewModel.allCauses.collect { causes ->
                val totalEarnings = causes.sumOf { it.totalEarned }
                binding.totalEarningsValue.text = String.format("$%.2f", totalEarnings)
            }
        }
    }
}
