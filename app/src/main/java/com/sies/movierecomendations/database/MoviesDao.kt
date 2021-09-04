package com.sies.movierecomendations.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.sies.movierecomendations.network.Results

@Dao
interface MoviesDao {

    @Insert(onConflict = REPLACE)
    fun insert(movie: Results)

    @Update
    fun update(movie: Results)

    @Delete
    fun delete(movie: Results)

    @Query("SELECT * FROM Results")
    fun read(): LiveData<List<Results>>

    @Query("DELETE FROM Results")
    fun deleteAll()
}