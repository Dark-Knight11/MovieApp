package com.sies.movierecomendations;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView movies;
    GenreList res;
    String API_KEY = BuildConfig.API_KEY;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = findViewById(R.id.movies);
        movies.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        movieDbAPI.getGenre(API_KEY).enqueue(new Callback<GenreList>() {
            @Override
            public void onResponse(Call<GenreList> call, Response<GenreList> response) {
                res = response.body();
                Log.i("onResponse: ", String.valueOf(response.code()));
                movies.setAdapter(new genreAdapter(res));
            }

            @Override
            public void onFailure(Call<GenreList> call, Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });

    }
}