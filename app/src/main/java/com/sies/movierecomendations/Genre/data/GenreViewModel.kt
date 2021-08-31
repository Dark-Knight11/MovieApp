package com.sies.movierecomendations.Genre.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.*
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
        private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        val movieDbAPI: MovieDbAPI = retrofit.create(MovieDbAPI::class.java)
    }

    // navigation to genre movies event
    private val _navigatedToGenreMovies = MutableLiveData<Boolean>()
    val navigatedToGenreMovies: LiveData<Boolean>
        get() = _navigatedToGenreMovies

    // genre object passed to genre adapter
    private val _details = MutableLiveData<Genres>()
    val details: LiveData<Genres>
        get() = _details

    // movie details passed to movie list adapter
    private val _movieDetails = MutableLiveData<Results>()
    val movieDetails: LiveData<Results>
        get() = _movieDetails

    // navigation to movie details screen event
    private val _navigateToMovieDetails = MutableLiveData<Boolean>()
    val navigateToMovieDetails: LiveData<Boolean>
        get() = _navigateToMovieDetails

    // genre List data
    private val _genreList = MutableLiveData<GenreList>()
    val genreList: LiveData<GenreList>
        get() = _genreList

    // genre movies data
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

    fun passDetails(genre: Genres) { _details.value = genre }

    fun navigationDone() {
        _navigatedToGenreMovies.value = true
        _details.value = null
    }

    fun passMovieDetails(res: Results) { _movieDetails.value = res }

    fun navigationToMovieDetailsDone() {
        _navigateToMovieDetails.value = true
        _movieDetails.value = null
    }

    fun getMovies(genreID: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i("ThreadCall", Thread.currentThread().name)
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
    }
}