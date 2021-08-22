//package com.sies.movierecomendations.archived;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.GridLayoutManager;
//
//import com.sies.movierecomendations.Genre.data.GenreViewModel;
//import com.sies.movierecomendations.Genre.ui.GenreAdapter;
//import com.sies.movierecomendations.R;
//import com.sies.movierecomendations.databinding.FragmentGenreBinding;
//
//public class GenreFragment extends Fragment {
//
//    FragmentGenreBinding binding;
//    GenreViewModel viewModel;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre, container,false);
//
//        viewModel = new ViewModelProvider(this).get(GenreViewModel.class);
//
//        if (!networkWhere()) {
//            binding.noInternet.setText("404\nPlease Connect to Internet");
//            binding.noInternet.setVisibility(View.VISIBLE);
//        }
//
//        binding.movies.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        viewModel.getGenreList().observe(getViewLifecycleOwner(), genreList ->
//                binding.movies.setAdapter(new GenreAdapter(genreList))
//
//        return binding.getRoot();
//    }
//
//    private boolean networkWhere() {
//
//        boolean have_WIFI = false;
//        boolean have_MobileData = false;
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
//
//        for(NetworkInfo info: networkInfos) {
//            if (info.getTypeName().equalsIgnoreCase("WIFI"))
//                if (info.isConnected())
//                    have_WIFI = true;
//            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
//                if (info.isConnected())
//                    have_MobileData = true;
//
//        }
//        return have_MobileData || have_WIFI;
//    }
//}