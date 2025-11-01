package com.ad2cause.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ad2cause.R
import com.ad2cause.data.models.Cause
import com.ad2cause.databinding.ItemCauseCardBinding

/**
 * RecyclerView Adapter for displaying a list of causes.
 * Uses ListAdapter with DiffUtil for efficient list updates.
 */
class CauseAdapter(
    private val onCauseClick: (Cause) -> Unit
) : ListAdapter<Cause, CauseAdapter.CauseViewHolder>(CauseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CauseViewHolder {
        val binding = ItemCauseCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CauseViewHolder(binding, onCauseClick)
    }

    override fun onBindViewHolder(holder: CauseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for individual cause items.
     */
    class CauseViewHolder(
        private val binding: ItemCauseCardBinding,
        private val onCauseClick: (Cause) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cause: Cause) {
            binding.apply {
                // Load image using Coil
                causeImageView.load(cause.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_placeholder)
                }

                causeName.text = cause.name
                causeCategory.text = cause.category

                // Handle click
                root.setOnClickListener {
                    onCauseClick(cause)
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
