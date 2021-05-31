package com.sies.movierecomendations;

import com.sies.movierecomendations.GenreApi.GenreList;
import com.sies.movierecomendations.MoviesApi.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbAPI {
    @GET("discover/movie")
    Call<MoviesList> getData(
            @Query("api_key") String key,
            @Query("sort_by") String sort,
            @Query("with_genres") int genreID
    );

    @GET("genre/movie/list")
    Call<GenreList> getGenre(
            @Query("api_key") String key
    );

    @GET("movie/popular")
    Call<MoviesList> getPopular(
            @Query("api_key") String key
    );

}
