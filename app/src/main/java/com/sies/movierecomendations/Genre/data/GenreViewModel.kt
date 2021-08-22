package com.sies.movierecomendations.Genre.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.GenreList
import com.sies.movierecomendations.network.MovieDbAPI
import com.sies.movierecomendations.network.MoviesList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GenreViewModel: ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        val movieDbAPI = retrofit.create(MovieDbAPI::class.java)
    }

    private val _genreList = MutableLiveData<GenreList>()
    val genreList: LiveData<GenreList>
        get() = _genreList

    private val _genreMovie = MutableLiveData<MoviesList>()
    val genreMovie: LiveData<MoviesList>
        get() = _genreMovie

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getList()
            }
        }
        Log.i("GenreViewModel", "GenreViewModel was called")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GenreViewModel", "GenreViewModel was cleared")
    }

    private fun getList() {

        movieDbAPI.getGenre(API_KEY).enqueue(object : Callback<GenreList?> {
            override fun onResponse(call: Call<GenreList?>, response: Response<GenreList?>) {
                val res = response.body()
                _genreList.value = res
            }

            override fun onFailure(call: Call<GenreList?>, t: Throwable) {
                Log.i("onFailure: ", t.message!!)
            }
        })
    }

    fun getMovies(genreID: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i("ThreadCall", Thread.currentThread().name)
                movieDbAPI.getData(GenreMoviesViewModel.API_KEY, "popularity.desc", genreID)
                    .enqueue(object : Callback<MoviesList?> {
                        override fun onResponse(
                            call: Call<MoviesList?>,
                            response: Response<MoviesList?>
                        ) {
                            val res = response.body()
                            _genreMovie.value = res
                        }

                        override fun onFailure(call: Call<MoviesList?>, t: Throwable) {
                            Log.i("onFailure: ", t.message!!)
                        }
                    })
            }
        }
    }
}