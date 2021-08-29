package com.sies.movierecomendations.Genre.data

import android.util.Log
import androidx.lifecycle.*
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.MovieDbAPI
import com.sies.movierecomendations.network.MoviesList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GenreMoviesViewModelFactory(private val genreID: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenreMoviesViewModel::class.java))
            return GenreMoviesViewModel(genreID) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GenreMoviesViewModel(val genreID: Int): ViewModel() {

    companion object {
        var API_KEY = BuildConfig.API_KEY
    }

    private val _genreMovie = MutableLiveData<MoviesList>()
    val genreMovie: LiveData<MoviesList>
        get() = _genreMovie

    init {
        viewModelScope.launch {
            getMovies()
        }
    }

    private fun getMovies() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI = retrofit.create(MovieDbAPI::class.java)

        movieDbAPI.getData(API_KEY, "popularity.desc", genreID)
            .enqueue(object : Callback<MoviesList?> {
                override fun onResponse(call: Call<MoviesList?>, response: Response<MoviesList?>) {
                    val res = response.body()
                    _genreMovie.value = res
                }

                override fun onFailure(call: Call<MoviesList?>, t: Throwable) {
                    Log.i("onFailure: ", t.message!!)
                }
            })
    }
}


