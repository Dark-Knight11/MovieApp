package com.sies.movierecomendations.Genre.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sies.movierecomendations.databinding.GenreMoviesCardBinding
import com.sies.movierecomendations.network.Results

class MovieListAdapter(private val clickListener: MovieListClickListener): ListAdapter<Results, MovieListAdapter.ViewHolder>(MovieListDiffCallback()) {
    class ViewHolder(private var binding: GenreMoviesCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(res: Results, clickListener: MovieListClickListener) {
            binding.movies = res
//            binding.clickListener = clickListener
            binding.frame.setOnClickListener {
                clickListener.onClick(res)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GenreMoviesCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val results = getItem(position)
        holder.bind(results, clickListener)
    }

}

class MovieListClickListener(val clickListener: (res: Results) -> Unit) {
    fun onClick(res: Results) = clickListener(res)
}

class MovieListDiffCallback: DiffUtil.ItemCallback<Results>() {
    override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
        return oldItem.equals(newItem)
    }

}
