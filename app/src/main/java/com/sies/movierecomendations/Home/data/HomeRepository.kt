package com.sies.movierecomendations.Home.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.sies.movierecomendations.BuildConfig
import com.sies.movierecomendations.database.MoviesDao
import com.sies.movierecomendations.network.APIService
import com.sies.movierecomendations.network.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val apiService: APIService,
    private val dao: MoviesDao
) {
    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            try {
                val res = apiService.getTrending(BuildConfig.API_KEY)
                for (movie in res.results)
                    dao.insert(movie)
            }
            catch (e: Exception) {
                Log.i("HomeRepository", "You are Offline")
            }
        }
    }
    fun getMovies(): LiveData<List<Results>> {
        Log.i("Repo", "I was called")
        return dao.read()
    }
}