package com.sies.movierecomendations.MovieDetails.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieDetailViewModelFactory(val media: String, val id: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(media, id) as T
    }
}