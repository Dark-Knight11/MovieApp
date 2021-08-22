package com.sies.movierecomendations.Search.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
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

class SearchViewModel: ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private val movieDbAPI: MovieDbAPI = retrofit.create(MovieDbAPI::class.java)
    }

    private val _result = MutableLiveData<MoviesList>()
    val result: LiveData<MoviesList>
        get() = _result

    fun search(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieDbAPI.getSearchResult(API_KEY, query).enqueue(object : Callback<MoviesList?> {
                    override fun onResponse(
                        call: Call<MoviesList?>,
                        response: Response<MoviesList?>
                    ) {
                        val res = response.body()
                        _result.value = res
                    }

                    override fun onFailure(call: Call<MoviesList?>, t: Throwable) {
                        Log.i("onFailure: ", t.message!!)
                    }
                })
            }
        }
    }
}