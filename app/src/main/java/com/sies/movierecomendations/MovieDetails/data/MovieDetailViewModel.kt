package com.sies.movierecomendations.MovieDetails.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailViewModel(): ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI: APIService = retrofit.create(APIService::class.java)

    }

    private val _ytLink = MutableLiveData<String>()
    val ytLink: LiveData<String>
        get() = _ytLink


    suspend fun getApi(media: String, id: Int) {
        try {
            val res = movieDbAPI.getVideos(media, id, API_KEY)
            if (res != null && res.results.size > 0) {
                val key = res.results[0].key
                _ytLink.value = key
            }
        }
        catch (e: Exception) {}
    }

}