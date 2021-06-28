package com.sies.movierecomendations.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.sies.movierecomendations.BuildConfig;
import com.sies.movierecomendations.MovieDbAPI;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.R;
import com.sies.movierecomendations.TopRated.topMovies;
import com.sies.movierecomendations.TopRated.topTv;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopRatedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopRatedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MoviesList res;
    String API_KEY = BuildConfig.API_KEY;
    RecyclerView topM;
    ImageView header;

    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragments;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    public TopRatedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopRatedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopRatedFragment newInstance(String param1, String param2) {
        TopRatedFragment fragment = new TopRatedFragment();
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
        View view = inflater.inflate(R.layout.fragment_top_rated, container, false);

        header = view.findViewById(R.id.header);
//        topM = view.findViewById(R.id.topMovies);

//        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
//        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

//        toolbar = view.findViewById(R.id.toolbar);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tabLayout);

//        fragments = new ArrayList<>();

        fragments.add(new topMovies());
        fragments.add(new topTv());

        FragmentPageAdapter pagerAdapter = new FragmentPageAdapter(getChildFragmentManager(), getContext(), fragments);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("movies");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("tv show");


        Glide
            .with(TopRatedFragment.this)
            .load("https://image.tmdb.org/t/p/original/dIWwZW7dJJtqC6CgWzYkNVKIUm8.jpg")
            .into(header);


//        topM.setLayoutManager(new LinearLayoutManager(getContext()));
//        getApi();
        return view;
    }

    public void getApi() {
        movieDbAPI.getMoviesTopRated(API_KEY).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(@NonNull Call<MoviesList> call, @NonNull Response<MoviesList> response) {
                res = response.body();
//                topM.setAdapter(new TopRatedAdapter(res));
            }

            @Override
            public void onFailure(@NonNull Call<MoviesList> call, @NonNull Throwable t) {
                Log.i("onFailure: ", t.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
    }
}