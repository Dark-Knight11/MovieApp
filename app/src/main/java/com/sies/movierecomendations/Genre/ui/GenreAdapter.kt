package com.sies.movierecomendations.Genre.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sies.movierecomendations.databinding.GenreListCardBinding
import com.sies.movierecomendations.network.Genres

class GenreAdapter(private val clickListener: GenreClickListener) : ListAdapter<Genres, GenreAdapter.ViewHolder>(GenreDiffCallback()) {
    class ViewHolder(private var binding: GenreListCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(res: Genres, clickListener: GenreClickListener) {
            binding.genres = res
//            binding.clickListener = clickListener
            binding.linearLayout.setOnClickListener {
                clickListener.onClick(res)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GenreListCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = getItem(position)
        holder.bind(genre, clickListener)
    }
}

class GenreDiffCallback: DiffUtil.ItemCallback<Genres>() {

    override fun areItemsTheSame(oldItem: Genres, newItem: Genres): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Genres, newItem: Genres): Boolean {
        return oldItem.equals(newItem)
    }
}
class GenreClickListener(val clickListener: (genre: Genres) -> Unit) {
    fun onClick(genre: Genres) = clickListener(genre)
}
