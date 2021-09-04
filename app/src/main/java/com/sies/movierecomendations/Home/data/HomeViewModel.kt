package com.sies.movierecomendations.Home.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.APIService
import com.sies.movierecomendations.network.MoviesList
import com.sies.movierecomendations.network.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {

    @Inject lateinit var movieDbAPI: APIService
    companion object {
        const val API_KEY = BuildConfig.API_KEY
    }

    private val _images = MutableLiveData<List<String>>()
    val images: LiveData<List<String>>
        get() = _images


    private val _popularMovies = MutableLiveData<MoviesList>()
    val popularMovies: LiveData<MoviesList>
        get() = _popularMovies


    private val _movies = MutableLiveData<Results>()
    val movies: LiveData<Results>
        get() = _movies

    init {
        viewModelScope.launch {
            repository.refreshMovies()
            getData()
        }
        Log.i("HomeFragment", "viewModel init was called")
    }

    fun getTrending(): LiveData<List<Results>> = repository.getMovies()

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeFragment", "viewModel onCleared was called")
    }

    private suspend fun getData() {
        // Popular Movies Call
        withContext(Dispatchers.IO) {
            try {
                val res = movieDbAPI.getPopular(API_KEY)
                withContext(Dispatchers.Main) {
                    _popularMovies.value = res
                    val array: MutableList<String> = mutableListOf()
                    for (i in 0..5)
                        array.add("https://image.tmdb.org/t/p/original" + res.results[i].backdrop_path)
                    _images.value = array
                }
            }
            catch (e: Exception) {
                Log.e("HomeViewModel", "api call failed" )
            }

        }
    }

    fun passDetailsToAdapter(res: Results) { _movies.value = res }
}