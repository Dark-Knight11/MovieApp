package com.sies.movierecomendations.PopularMovies;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.sies.movierecomendations.Fragments.GenreFragment;
import com.sies.movierecomendations.Fragments.ProfileFragment;
import com.sies.movierecomendations.R;

public class PopularMovies extends AppCompatActivity {

//    MoviesList res;
//    String API_KEY = BuildConfig.API_KEY;
//
//    RecyclerView popMovies;
//    ImageView header;


//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("https://api.themoviedb.org/3/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        View pop = findViewById(R.id.popular);
        View genre = findViewById(R.id.genre);
        View profile = findViewById(R.id.profile);

        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().add(R.id.fl_wrapper, new com.sies.movierecomendations.Fragments.PopularMovies(), "Home").commit();

        pop.setOnClickListener(v -> ft.beginTransaction().replace(R.id.fl_wrapper, new com.sies.movierecomendations.Fragments.PopularMovies(), "Home").commit());
        genre.setOnClickListener(v -> ft.beginTransaction().replace(R.id.fl_wrapper, new GenreFragment() ,"Genre").commit());
        profile.setOnClickListener(v -> ft.beginTransaction().replace(R.id.fl_wrapper, new ProfileFragment() ,"Profile").commit());

//        header = findViewById(R.id.header);
//        popMovies = findViewById(R.id.popMovies);
//
//        Glide.with(PopularMovies.this).load("https://image.tmdb.org/t/p/original/srYya1ZlI97Au4jUYAktDe3avyA.jpg").into(header);
//
//        popMovies.setLayoutManager(new GridLayoutManager(PopularMovies.this, 2));
//        getApi();

    }

//    public void getApi() {
//        movieDbAPI.getPopular(API_KEY).enqueue(new Callback<MoviesList>() {
//            @Override
//            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
//                res = response.body();
////                Log.i( "onResponse: ", String.valueOf(response.headers()));
////                Log.i( "onResponse: ", String.valueOf(response.message()));
////                Log.i( "onResponse: ", String.valueOf(response.raw()));
//                Toast.makeText(PopularMovies.this, "No of movies: " + Integer.toString(res.getResults().size()), Toast.LENGTH_SHORT).show();
//                popMovies.setAdapter(new PopularMoviesAdapter(res));
//            }
//
//            @Override
//            public void onFailure(Call<MoviesList> call, Throwable t) {
//                Log.i("onFailure: ", t.getMessage());
//            }
//        });
//    }
}