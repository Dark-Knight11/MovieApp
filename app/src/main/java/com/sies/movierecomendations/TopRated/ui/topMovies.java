package com.sies.movierecomendations.TopRated.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sies.movierecomendations.R;
import com.sies.movierecomendations.TopRated.data.TopMoviesViewModel;
import com.sies.movierecomendations.databinding.ChildFragmentTopMoviesAndTvBinding;

public class topMovies extends Fragment {

    ChildFragmentTopMoviesAndTvBinding binding;
    TopMoviesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.child_fragment_top_movies_and_tv, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        viewModel = new ViewModelProvider(this).get(TopMoviesViewModel.class);

        viewModel.getTopRatedMovies().observe(getViewLifecycleOwner(), topMovies ->
                binding.recyclerView.setAdapter(new TopRatedAdapter(topMovies, 1)));

        return binding.getRoot();
    }
}