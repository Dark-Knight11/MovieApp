package com.sies.movierecomendations.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.MoviesApi.MovieDbAPI;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_popular_movies, container, false);

        header = view.findViewById(R.id.header);
        popMovies = view.findViewById(R.id.popMovies);

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
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                res = response.body();
//                Log.i( "onResponse: ", String.valueOf(response.headers()));
//                Log.i( "onResponse: ", String.valueOf(response.message()));
//                Log.i( "onResponse: ", String.valueOf(response.raw()));
                Toast.makeText(getActivity().getApplicationContext(), "No of movies: " + Integer.toString(res.getResults().size()), Toast.LENGTH_SHORT).show();
                popMovies.setAdapter(new PopularMoviesAdapter(res));
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }
}