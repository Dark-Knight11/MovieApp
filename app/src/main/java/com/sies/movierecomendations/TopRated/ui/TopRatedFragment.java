package com.sies.movierecomendations.TopRated.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.sies.movierecomendations.R;

import java.util.ArrayList;
import java.util.Objects;

public class TopRatedFragment extends Fragment {

    ImageView header;

    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_rated, container, false);

        header = view.findViewById(R.id.header);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tabLayout);

        fragments = new ArrayList<>();
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

        return view;
    }
}