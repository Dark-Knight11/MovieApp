package com.sies.movierecomendations.di

import com.sies.movierecomendations.Home.data.HomeRepository
import com.sies.movierecomendations.database.MoviesDao
import com.sies.movierecomendations.network.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RespositoryModule {

    @Singleton
    @Provides
    fun provideHomeRepository( apiService: APIService, dao: MoviesDao): HomeRepository {
        return HomeRepository(apiService,dao)
    }

}