package com.sies.movierecomendations.Search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sies.movierecomendations.databinding.SearchViewItemBinding
import com.sies.movierecomendations.network.Results

class SearchAdapter(private val clickListener: SearchItemClickListener): ListAdapter<Results, SearchAdapter.ViewHolder>(SearchItemDiffCallback()) {
    class ViewHolder(val binding: SearchViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(res: Results, clickListener: SearchItemClickListener) {
            binding.searchTerm = res
            binding.card.setOnClickListener { clickListener.onClick(res) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val results = getItem(position)
        holder.onBind(results, clickListener)
    }
}

class SearchItemClickListener(val clickListener: (res: Results) -> Unit) {
    fun onClick(res: Results) = clickListener(res)
}

class SearchItemDiffCallback: DiffUtil.ItemCallback<Results>() {
    override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
        return oldItem.equals(newItem)
    }

}
