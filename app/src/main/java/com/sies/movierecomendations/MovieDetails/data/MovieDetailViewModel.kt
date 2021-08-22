package com.sies.movierecomendations.MovieDetails.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.MovieDbAPI
import com.sies.movierecomendations.network.VideosList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailViewModel(val media: String, val id: Int): ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI = retrofit.create(MovieDbAPI::class.java)
    }

    private val _ytLink = MutableLiveData<String>()
    val ytLink: LiveData<String>
        get() = _ytLink

    init {
        viewModelScope.launch {
            getApi()
        }
    }

    private suspend fun getApi() {
        withContext(Dispatchers.IO) {
            movieDbAPI.getVideos(media, id, API_KEY).enqueue(object : Callback<VideosList?> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<VideosList?>, response: Response<VideosList?>) {
                    val resv = response.body()
                    if (response.body() != null && resv?.getResults()?.size!! > 0) {
                        val key = resv.results[0].key
                        _ytLink.value = key
                    }
                }

                override fun onFailure(call: Call<VideosList?>, t: Throwable) {
                    Log.w("onFailure: ", t.message!!)
                }
            })
        }
    }

}