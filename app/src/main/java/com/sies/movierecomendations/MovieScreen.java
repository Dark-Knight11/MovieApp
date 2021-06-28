package com.sies.movierecomendations;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.MoviesApi.VideosList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieScreen extends AppCompatActivity {

    ImageView backdrop;
    TextView title, desc, date, rating, youtube;

    VideosList resv;
    String API_KEY = BuildConfig.API_KEY;
    int id, flag;
    String key, media="movie";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_screen);

        backdrop = findViewById(R.id.backdrop);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.overview);
        date = findViewById(R.id.date);
        rating = findViewById(R.id.rating);
        youtube = findViewById(R.id.youtube);

        Intent intent = getIntent();
        String backdropPath = intent.getStringExtra("backdrop-path");
        String name = intent.getStringExtra("title");
        String releaseDate = intent.getStringExtra("release-date");
        float votes = intent.getFloatExtra("rating", 0);
        String overview = intent.getStringExtra("overview");
        id = intent.getIntExtra("id", 0);
        flag = intent.getIntExtra("flag", 1);

        if (flag!=1) media = "tv";
        getApi(id);

        Glide.with(MovieScreen.this).load("https://image.tmdb.org/t/p/original" + backdropPath).into(backdrop);
        title.setText(name);
        date.setText("Release Date: " + releaseDate);
        desc.setText(overview);
        rating.setText("Rating: " + votes);

    }

    public void getApi(int id) {
        movieDbAPI.getVideos(media, id, API_KEY).enqueue(new Callback<VideosList>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<VideosList> call, @NonNull Response<VideosList> response) {
                resv = response.body();
                Log.i("onResponse: ", String.valueOf(response.body()));
                if (resv.getResults().size()>0){
                    key = resv.getResults().get(0).getKey();
                    youtube.setText("https://www.youtube.com/watch?v=" + key);
                    youtube.setOnClickListener(v -> {
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+key));
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent2.setPackage("com.google.android.youtube");
                        startActivity(intent2);
                    });
                }
                else
                    youtube.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<VideosList> call, @NonNull Throwable t) {
                Log.w("onFailure: ", t.getMessage());
            }
        });
    }
}