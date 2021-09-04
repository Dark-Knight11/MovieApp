package com.sies.movierecomendations.TopRated.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.APIService
import com.sies.movierecomendations.network.MoviesList
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TopMoviesViewModel: ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI: APIService = retrofit.create(APIService::class.java)
    }

    private val _topRatedMovies = MutableLiveData<MoviesList>()
    val topRatedMovies: LiveData<MoviesList>
        get() = _topRatedMovies

    private val _topRatedTVShows = MutableLiveData<MoviesList>()
    val topRatedTVShows: LiveData<MoviesList>
        get() = _topRatedTVShows

    init {
        viewModelScope.launch {
            getApi()
        }
    }

    private suspend fun getApi() {
        val res = movieDbAPI.getMoviesTopRated(API_KEY)
        _topRatedMovies.value = res

        val response = movieDbAPI.getTvTopRated(API_KEY)
        _topRatedTVShows.value = response
    }
}
