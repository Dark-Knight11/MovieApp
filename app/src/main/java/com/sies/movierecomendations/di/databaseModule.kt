package com.sies.movierecomendations.di

import android.content.Context
import androidx.room.Room
import com.sies.movierecomendations.database.Database
import com.sies.movierecomendations.database.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object databaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(
            appContext,
            Database::class.java,
            "moviesDatabase"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDao(database: Database): MoviesDao {
        return database.dao
    }
}