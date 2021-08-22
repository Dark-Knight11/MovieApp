package com.sies.movierecomendations.Home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sies.movierecomendations.Home.data.HomeViewModel
import com.sies.movierecomendations.R
import com.sies.movierecomendations.Search.ui.SearchActivity
import com.sies.movierecomendations.databinding.FragmentHomeBinding
import com.synnapps.carouselview.ImageListener

class HomeFragment: Fragment() {

    var imageListener: ImageListener? = null
    lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("HomeFragment", "onCreateView was called")

        viewModel.images.observe(viewLifecycleOwner, {
            setUpBanner(it)
        })

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.search)
                startActivity(Intent(context, SearchActivity::class.java))
            super.onOptionsItemSelected(item)
        }
        binding.popMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.popularMovies.observe(viewLifecycleOwner, {
            binding.popMovies.adapter =
                PopularMoviesAdapter(it)
        })

        binding.trendingMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.trendingMovies.observe(viewLifecycleOwner, {
            binding.trendingMovies.adapter =
                PopularMoviesAdapter(it)
        })

        return binding.root
    }

    // Carousel View
    private fun setUpBanner(banner: List<String>) {
        imageListener = ImageListener { position: Int, imageView: ImageView? ->
            Glide.with(this)
                .load(banner[position])
                .into(imageView!!)
        }
        binding.carousel.setImageListener(imageListener)
        binding.carousel.pageCount = banner.size
    }

    override fun onStart() {
        super.onStart()
        Log.i("HomeFragment", "onStart was called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("HomeFragment", "onDestroyView was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("HomeFragment", "onDestroy was called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("HomeFragment", "onPause was called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("HomeFragment", "onResume was called")
    }
}