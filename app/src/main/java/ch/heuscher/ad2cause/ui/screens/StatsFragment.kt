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
import coil.load
import coil.transform.CircleCropTransformation
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
            isNestedScrollingEnabled = false
        }
    }

    /**
     * Observe and display statistics.
     */
    private fun observeData() {
        lifecycleScope.launch {
            causeViewModel.allCauses.collect { causes ->
                val totalEarnings = causes.sumOf { it.totalEarned }
                binding.totalEarningsValue.text = String.format("%.0f", totalEarnings)
                
                // Calculate additional stats
                val causesWithEarnings = causes.filter { it.totalEarned > 0 }
                binding.causesSupportedValue.text = causesWithEarnings.size.toString()
                
                // Estimate ads watched (assuming 1 point per ad)
                binding.adsWatchedValue.text = String.format("%.0f", totalEarnings)

                // Update stats list with ranking data
                val sortedCauses = causes.sortedByDescending { it.totalEarned }
                statsAdapter.submitList(sortedCauses, totalEarnings)
                
                // Handle empty state
                val isEmpty = causes.isEmpty()
                binding.emptyStatsContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.causesStatsRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }
        }

        // Observe active cause
        lifecycleScope.launch {
            causeViewModel.activeCause.collect { activeCause ->
                if (activeCause != null) {
                    binding.activeCauseNameStat.text = activeCause.name
                    binding.activeCauseEarningsStat.text = String.format("%.0f points earned", activeCause.totalEarned)
                    binding.activeCauseCard.visibility = View.VISIBLE
                    
                    // Load active cause image
                    if (activeCause.imageUrl.isNotEmpty()) {
                        binding.activeCauseImage.load(activeCause.imageUrl) {
                            crossfade(true)
                            placeholder(R.drawable.ic_heart_filled)
                            error(R.drawable.ic_heart_filled)
                            transformations(CircleCropTransformation())
                        }
                    } else {
                        binding.activeCauseImage.setImageResource(R.drawable.ic_heart_filled)
                    }
                } else {
                    binding.activeCauseNameStat.text = getString(R.string.no_active_cause)
                    binding.activeCauseEarningsStat.text = getString(R.string.select_cause_to_earn)
                    binding.activeCauseImage.setImageResource(R.drawable.ic_heart_filled)
                }
            }
        }
    }

    /**
     * Enhanced adapter for cause statistics with ranking and progress
     */
    private class CauseStatsAdapter : RecyclerView.Adapter<CauseStatsAdapter.StatsViewHolder>() {

        private var causes: List<Cause> = emptyList()
        private var totalEarnings: Double = 0.0

        fun submitList(newCauses: List<Cause>, total: Double) {
            causes = newCauses
            totalEarnings = total
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
            holder.bind(causes[position], position + 1, totalEarnings)
        }

        override fun getItemCount() = causes.size

        class StatsViewHolder(private val binding: ItemCauseStatBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(cause: Cause, rank: Int, totalEarnings: Double) {
                binding.causeStatName.text = cause.name
                // Load cause icon in list instead of numeric rank
                if (cause.imageUrl.isNotEmpty()) {
                    binding.causeStatIcon.load(cause.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_heart_filled)
                        error(R.drawable.ic_heart_filled)
                        transformations(CircleCropTransformation())
                    }
                } else {
                    binding.causeStatIcon.setImageResource(R.drawable.ic_heart_filled)
                }
                binding.causeStatEarnings.text = String.format("%.0f pts", cause.totalEarned)
                
                // Calculate and display percentage
                val percentage = if (totalEarnings > 0) {
                    (cause.totalEarned / totalEarnings * 100).toInt()
                } else {
                    0
                }
                binding.causeStatPercentage.text = "$percentage%"
                
                // Update progress bar width based on percentage
                val params = binding.causeStatProgress.layoutParams
                val maxWidth = 100 // dp
                params.width = (maxWidth * percentage / 100).coerceIn(4, maxWidth) * 
                    binding.root.context.resources.displayMetrics.density.toInt()
                binding.causeStatProgress.layoutParams = params
            }
        }
    }
}
