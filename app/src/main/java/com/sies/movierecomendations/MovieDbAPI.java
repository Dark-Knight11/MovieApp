package com.sies.movierecomendations;

import com.sies.movierecomendations.GenreApi.GenreList;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.MoviesApi.VideosList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("movie/top_rated")
    Call<MoviesList> getMoviesTopRated(
            @Query("api_key") String key
    );

    @GET("tv/top_rated")
    Call<MoviesList> getTvTopRated(
            @Query("api_key") String key
    );

    @GET("{media}/{movie_id}/videos")
    Call<VideosList> getVideos(
            @Path("media") String media,
            @Path("movie_id") int movieId,
            @Query("api_key") String key
    );

    @GET("trending/movie/day")
    Call<MoviesList> getTrending(
            @Query("api_key") String key
    );

    @GET("search/multi")
    Call<MoviesList> getSearchResult(
            @Query("api_key") String key,
            @Query("query") String query
    );
}
