package ch.heuscher.ad2cause.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.databinding.FragmentStatsBinding
import ch.heuscher.ad2cause.databinding.ItemCauseStatBinding
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * Stats Screen Fragment
 * Displays statistics about earnings and breakdown per cause.
 */
class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var causeViewModel: CauseViewModel
    private lateinit var statsAdapter: CauseStatsAdapter

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

        setupRecyclerView()
        observeData()
    }

    /**
     * Setup the RecyclerView for cause stats.
     */
    private fun setupRecyclerView() {
        statsAdapter = CauseStatsAdapter()
        binding.causesStatsRecyclerView.apply {
            adapter = statsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
    }

    /**
     * Observe and display statistics.
     */
    private fun observeData() {
        lifecycleScope.launch {
            causeViewModel.allCauses.collect { causes ->
                val totalEarnings = causes.sumOf { it.totalEarned }
                binding.totalEarningsValue.text = String.format("$%.2f", totalEarnings)

                // Update stats list
                statsAdapter.submitList(causes.sortedByDescending { it.totalEarned })
                binding.emptyStatsText.visibility = if (causes.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Observe active cause
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { activeCause ->
                if (activeCause != null) {
                    binding.activeCauseNameStat.text = activeCause.name
                    binding.activeCauseEarningsStat.text = String.format("Earned: $%.2f", activeCause.totalEarned)
                } else {
                    binding.activeCauseNameStat.text = "No active cause"
                    binding.activeCauseEarningsStat.text = "Select a cause to start earning"
                }
            }
        }
    }

    /**
     * Simple adapter for cause statistics
     */
    private class CauseStatsAdapter : RecyclerView.Adapter<CauseStatsAdapter.StatsViewHolder>() {

        private var causes: List<Cause> = emptyList()

        fun submitList(newCauses: List<Cause>) {
            causes = newCauses
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
            val binding = ItemCauseStatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return StatsViewHolder(binding)
        }

        override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
            holder.bind(causes[position])
        }

        override fun getItemCount() = causes.size

        class StatsViewHolder(private val binding: ItemCauseStatBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(cause: Cause) {
                binding.causeStatName.text = cause.name
                binding.causeStatCategory.text = cause.category
                binding.causeStatEarnings.text = String.format("$%.2f", cause.totalEarned)
            }
        }
    }
}
