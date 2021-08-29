package com.sies.movierecomendations.Genre.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sies.movierecomendations.Genre.data.GenreViewModel
import com.sies.movierecomendations.R
import com.sies.movierecomendations.databinding.FragmentGenreBinding

class GenreFragment : Fragment() {

    lateinit var binding: FragmentGenreBinding
    lateinit var viewModel: GenreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre, container, false)

        viewModel = ViewModelProvider(this).get(GenreViewModel::class.java)
        binding.lifecycleOwner = this

        if (!networkWhere())
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "No Internet", Snackbar.LENGTH_SHORT).show()

        val adapter = GenreAdapter(GenreClickListener {
            viewModel.passDetails(it)
        })

        binding.movies.adapter = adapter
        viewModel.genreList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it.genres)
            }
        })

        viewModel.details.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(GenreFragmentDirections.actionGenreFragmentToGenreMovies(it.id, it.name))
                viewModel.navigationDone()
            }
        })

        return binding.root
    }

    private fun networkWhere(): Boolean {
        var HAVE_WIFI = false
        var have_MobileData = false
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.allNetworkInfo
        for (info in networkInfo) {
            if (info.typeName.equals("WIFI", ignoreCase = true))
                if (info.isConnected) HAVE_WIFI = true
            if (info.typeName.equals("MOBILE", ignoreCase = true))
                if (info.isConnected) have_MobileData = true
        }
        return have_MobileData || HAVE_WIFI
    }
}