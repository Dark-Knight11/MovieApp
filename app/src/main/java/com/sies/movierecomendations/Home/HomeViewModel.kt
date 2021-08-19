package com.sies.movierecomendations.Home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.MovieDbAPI
import com.sies.movierecomendations.MoviesApi.MoviesList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel: ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
    }

    private val _popularMovies = MutableLiveData<MoviesList>()
    val popularMovies: LiveData<MoviesList>
        get() = _popularMovies

    private val _trendingMovies = MutableLiveData<MoviesList>()
    val trendingMovies: LiveData<MoviesList>
        get() = _trendingMovies

    init {
        getData()
        Log.i("HomeFragment", "viewModel init was called")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeFragment", "viewModel onCleared was called")
    }

    private fun getData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI = retrofit.create(MovieDbAPI::class.java)
        movieDbAPI.getPopular(API_KEY).enqueue(object: Callback<MoviesList> {
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                val res = response.body()
                _popularMovies.value = res
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.i("onFailure: ", t.message!!)
            }
        })

        movieDbAPI.getTrending(API_KEY).enqueue(object: Callback<MoviesList> {
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                val res = response.body()
                _trendingMovies.value = res
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.i("onFailure: ", t.message!!)
            }
        })
    }
}