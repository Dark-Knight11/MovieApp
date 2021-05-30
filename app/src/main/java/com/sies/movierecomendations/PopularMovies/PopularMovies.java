package com.sies.movierecomendations.PopularMovies;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.MovieDbAPI;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopularMovies extends AppCompatActivity {

    MoviesList res;
    String API_KEY = BuildConfig.API_KEY;

    RecyclerView popMovies;
    ImageView header;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        header = findViewById(R.id.header);
        popMovies = findViewById(R.id.popMovies);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

//        Toast.makeText(PopularMovies.this, Integer.toString(width), Toast.LENGTH_SHORT).show();
        int spacing = 40; // 50px

        Glide.with(PopularMovies.this).load("https://image.tmdb.org/t/p/w500/srYya1ZlI97Au4jUYAktDe3avyA.jpg").into(header);

        popMovies.setLayoutManager(new GridLayoutManager(PopularMovies.this, 2));
        popMovies.addItemDecoration(new GridItemDecoration(spacing));
        getApi(878);

    }

    public void getApi(int genreID) {
        movieDbAPI.getData(API_KEY, "popularity.desc", genreID).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                res = response.body();
                Toast.makeText(PopularMovies.this, "No of movies: " + Integer.toString(res.getResults().size()), Toast.LENGTH_SHORT).show();
                popMovies.setAdapter(new PopularMoviesAdapter(res));
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }
}