package com.sies.movierecomendations.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.MovieDbAPI;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.PopularMovies.PopularMoviesAdapter;
import com.sies.movierecomendations.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularMovies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularMovies extends Fragment {

    MoviesList res;
    String API_KEY = BuildConfig.API_KEY;

    RecyclerView popMovies;
    ImageView header;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    public PopularMovies() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularMovies.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularMovies newInstance(String param1, String param2) {
        PopularMovies fragment = new PopularMovies();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_popular_movies, container, false);

        header = view.findViewById(R.id.header);
        popMovies = view.findViewById(R.id.popMovies);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        Glide
            .with(PopularMovies.this)
            .load("https://image.tmdb.org/t/p/original/srYya1ZlI97Au4jUYAktDe3avyA.jpg")
            .into(header);

        popMovies.setLayoutManager(new GridLayoutManager(getContext(), 2));
        getApi();

        return  view;
    }

    public void getApi() {
        movieDbAPI.getPopular(API_KEY).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(@NonNull Call<MoviesList> call, @NonNull Response<MoviesList> response) {
                res = response.body();
                Toast.makeText(getContext(), "No of movies: " + res.getResults().size(), Toast.LENGTH_SHORT).show();
                popMovies.setAdapter(new PopularMoviesAdapter(res));
            }

            @Override
            public void onFailure(@NonNull Call<MoviesList> call, @NonNull Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }


}