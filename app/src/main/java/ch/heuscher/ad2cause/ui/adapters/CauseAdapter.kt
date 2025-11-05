package ch.heuscher.ad2cause.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.data.models.Cause
import ch.heuscher.ad2cause.databinding.ItemCauseCardBinding

/**
 * RecyclerView Adapter for displaying a list of causes.
 * Uses ListAdapter with DiffUtil for efficient list updates.
 */
class CauseAdapter(
    private val onCauseClick: (Cause) -> Unit,
    private val onSetActive: (Cause) -> Unit
) : ListAdapter<Cause, CauseAdapter.CauseViewHolder>(CauseDiffCallback()) {

    var activeCauseId: Int? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CauseViewHolder {
        val binding = ItemCauseCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CauseViewHolder(binding, onCauseClick, onSetActive)
    }

    override fun onBindViewHolder(holder: CauseViewHolder, position: Int) {
        holder.bind(getItem(position), activeCauseId)
    }

    /**
     * ViewHolder for individual cause items.
     */
    class CauseViewHolder(
        private val binding: ItemCauseCardBinding,
        private val onCauseClick: (Cause) -> Unit,
        private val onSetActive: (Cause) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cause: Cause, activeCauseId: Int?) {
            binding.apply {
                // Load image using Coil
                causeImageView.load(cause.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_placeholder)
                }

                causeName.text = cause.name
                causeCategory.text = cause.category

                val isActive = cause.id == activeCauseId

                // Show/hide active indicator
                activeIndicator.visibility = if (isActive) View.VISIBLE else View.GONE

                // Update button state
                setActiveButton.isEnabled = !isActive
                setActiveButton.text = if (isActive) "Active" else "Set Active"

                // Handle details button click
                viewDetailsButton.setOnClickListener {
                    onCauseClick(cause)
                }

                // Handle set active button click
                setActiveButton.setOnClickListener {
                    if (!isActive) {
                        onSetActive(cause)
                    }
                }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates.
     */
    private class CauseDiffCallback : DiffUtil.ItemCallback<Cause>() {
        override fun areItemsTheSame(oldItem: Cause, newItem: Cause): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cause, newItem: Cause): Boolean {
            return oldItem == newItem
        }
    }
}
