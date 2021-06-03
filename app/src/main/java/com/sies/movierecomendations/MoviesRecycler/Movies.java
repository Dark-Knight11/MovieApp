package com.sies.movierecomendations.MoviesRecycler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.MoviesApi.MovieDbAPI;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class Movies extends AppCompatActivity {
    MoviesList res;
    String API_KEY = BuildConfig.API_KEY;
    RecyclerView moviesRecyclerView;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);


        Intent intent = getIntent();
        int value = intent.getIntExtra("genreId", 12);
        getApi(value);

        moviesRecyclerView = findViewById(R.id.recyclerView);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(Movies.this));

    }

    public void getApi(int genreID) {
        movieDbAPI.getData(API_KEY, "popularity.desc", genreID).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                res = response.body();
                Toast.makeText(Movies.this, "No of movies: " + Integer.toString(res.getResults().size()), Toast.LENGTH_SHORT).show();
                moviesRecyclerView.setAdapter(new MovieListAdapter(res));
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }


}


