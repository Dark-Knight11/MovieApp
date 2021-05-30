package com.sies.movierecomendations.GenreRecycler;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.GenreApi.GenreList;
import com.sies.movierecomendations.MovieDbAPI;
import com.sies.movierecomendations.PopularMovies.PopularMovies;
import com.sies.movierecomendations.Profile;
import com.sies.movierecomendations.R;

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

        if (!networkWhere()) {
            TextView _404 = findViewById(R.id.noInternet);
            _404.setText("404\nPlease Connect to Internet");
            _404.setVisibility(View.VISIBLE);

        }

        Button profile = findViewById(R.id.profile);
        Button pop = findViewById(R.id.pop);

        pop.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PopularMovies.class));
        });

        movies = findViewById(R.id.movies);
        movies.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        profile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Profile.class)));

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

    private boolean networkWhere() {

        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info: networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;

        }
        return have_MobileData || have_WIFI;
    }
}