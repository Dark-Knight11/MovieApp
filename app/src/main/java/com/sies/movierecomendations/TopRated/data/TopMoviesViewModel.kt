package com.sies.movierecomendations.TopRated.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.MovieDbAPI
import com.sies.movierecomendations.network.MoviesList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TopMoviesViewModel: ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
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

    fun getApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI = retrofit.create(MovieDbAPI::class.java)

        viewModelScope.launch {
        movieDbAPI.getMoviesTopRated(API_KEY).enqueue(object : Callback<MoviesList?> {
            override fun onResponse(call: Call<MoviesList?>, response: Response<MoviesList?>) {
                val res = response.body()
                _topRatedMovies.value = res
            }

            override fun onFailure(call: Call<MoviesList?>, t: Throwable) {
                Log.i("onFailure: ", t.message!!)
            }
        })

        movieDbAPI.getTvTopRated(API_KEY).enqueue(object : Callback<MoviesList?> {
            override fun onResponse(call: Call<MoviesList?>, response: Response<MoviesList?>) {
                val res = response.body()
                _topRatedTVShows.value = res
            }

            override fun onFailure(call: Call<MoviesList?>, t: Throwable) {
                Log.i("onFailure: ", t.message!!)
            }
        })
            }
    }
}