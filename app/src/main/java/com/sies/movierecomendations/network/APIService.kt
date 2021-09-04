package com.sies.movierecomendations.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("discover/movie")
    suspend fun getData(
        @Query("api_key") key: String,
        @Query("sort_by") sort: String,
        @Query("with_genres") genreID: Int
    ): MoviesList

    @GET("genre/movie/list")
    suspend fun getGenre(
        @Query("api_key") key: String
    ): GenreList

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") key: String
    ): MoviesList

    @GET("movie/top_rated")
    suspend fun getMoviesTopRated(
        @Query("api_key") key: String
    ): MoviesList

    @GET("tv/top_rated")
    suspend fun getTvTopRated(
        @Query("api_key") key: String
    ): MoviesList

    @GET("{media}/{movie_id}/videos")
    suspend fun getVideos(
        @Path("media") media: String,
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String
    ): VideosList?

    @GET("trending/movie/day")
    suspend fun getTrending(
        @Query("api_key") key: String
    ): MoviesList

    @GET("search/multi")
    suspend fun getSearchResult(
        @Query("api_key") key: String,
        @Query("query") query: String
    ): MoviesList
}