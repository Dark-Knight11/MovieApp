package com.sies.movierecomendations.Search.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.network.APIService
import com.sies.movierecomendations.network.MoviesList
import com.sies.movierecomendations.network.Results
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel: ViewModel() {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieDbAPI: APIService = retrofit.create(APIService::class.java)
    }

    private val _result = MutableLiveData<MoviesList>()
    val result: LiveData<MoviesList>
        get() = _result

    private val _term = MutableLiveData<Results>()
    val term: LiveData<Results>
        get() = _term

    fun passDetailsToAdapter(res: Results) {
        _term.value = res
    }

    fun search(query: String) {
        viewModelScope.launch {
            val res = movieDbAPI.getSearchResult(API_KEY, query)
            _result.value = res
        }
    }
}