package com.sies.movierecomendations.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.MovieDbAPI;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.PopularMovies.PopularMoviesAdapter;
import com.sies.movierecomendations.R;
import com.sies.movierecomendations.SearchActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    MoviesList res;
    String API_KEY = BuildConfig.API_KEY;

    private final String[] images = {
            "https://image.tmdb.org/t/p/original/srYya1ZlI97Au4jUYAktDe3avyA.jpg",
            "https://image.tmdb.org/t/p/original/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
            "https://image.tmdb.org/t/p/original/inJjDhCjfhh3RtrJWBmmDqeuSYC.jpg",
            "https://image.tmdb.org/t/p/original/gGSm6ZmWtGazs2H1m0gOp7cx1ZZ.jpg",
            "https://image.tmdb.org/t/p/original/pcDc2WJAYGJTTvRSEIpRZwM3Ola.jpg",
            "https://image.tmdb.org/t/p/original/2lBOQK06tltt8SQaswgb8d657Mv.jpg"
    };

    CarouselView carouselView;
    RecyclerView popMovies, trendingMovies;
    ImageListener imageListener;
    Toolbar toolbar;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        popMovies = view.findViewById(R.id.popMovies);
        trendingMovies = view.findViewById(R.id.trendingMovies);
        toolbar = view.findViewById(R.id.toolbar);
        carouselView = view.findViewById(R.id.carousel);

        setUpBanner(images);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search)
                startActivity(new Intent(getContext(), SearchActivity.class));
            return super.onOptionsItemSelected(item);
        });

        popMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getPopularMovies();
        trendingMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getTrendingMovies();

        return  view;
    }

    public void getPopularMovies() {
        movieDbAPI.getPopular(API_KEY).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(@NonNull Call<MoviesList> call, @NonNull Response<MoviesList> response) {
                res = response.body();
                popMovies.setAdapter(new PopularMoviesAdapter(res));
            }

            @Override
            public void onFailure(@NonNull Call<MoviesList> call, @NonNull Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }

    public void getTrendingMovies() {
        movieDbAPI.getTrending(API_KEY).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(@NonNull Call<MoviesList> call, @NonNull Response<MoviesList> response) {
                res = response.body();
                trendingMovies.setAdapter(new PopularMoviesAdapter(res));
            }

            @Override
            public void onFailure(@NonNull Call<MoviesList> call, @NonNull Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }

    // Settings for carousel view
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        carouselView.setPageCount(images.length);
        carouselView.setImageListener(imageListener);
    }

    // Carousel View
    private void setUpBanner(String[] banner) {
        imageListener = (position, imageView) ->
                Glide.with(this)
                        .load(banner[position])
                        .into(imageView);
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(banner.length);
    }
}

