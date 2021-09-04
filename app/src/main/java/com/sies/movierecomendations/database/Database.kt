package com.sies.movierecomendations.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sies.movierecomendations.network.Results

@Database(entities = [Results::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract val dao: MoviesDao
}