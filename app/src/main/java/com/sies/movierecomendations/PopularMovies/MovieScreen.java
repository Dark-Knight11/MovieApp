package com.sies.movierecomendations.PopularMovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.R;

public class MovieScreen extends AppCompatActivity {

    ImageView backdrop, full;
    TextView title, desc, date, rating;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_screen);

        full = findViewById(R.id.full);
        backdrop = findViewById(R.id.backdrop);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.overview);
        date = findViewById(R.id.date);
        rating = findViewById(R.id.rating);


        Intent intent = getIntent();
        String backdropPath = intent.getStringExtra("backdrop-path");
        String name = intent.getStringExtra("title");
        String releaseDate = intent.getStringExtra("release-date");
        float votes = intent.getFloatExtra("rating", 0);
        String overview = intent.getStringExtra("overview");

        Glide.with(MovieScreen.this).load("https://image.tmdb.org/t/p/original" + backdropPath).into(backdrop);
        title.setText(name);
        date.setText("Release Date: " + releaseDate);
        desc.setText(overview);
        rating.setText("Rating: " + votes);

    }
}