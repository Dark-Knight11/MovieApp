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
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sies.movierecomendations.Home.data.HomeViewModel
import com.sies.movierecomendations.MovieDetails.ui.MovieDetails
import com.sies.movierecomendations.R
import com.sies.movierecomendations.Search.ui.SearchActivity
import com.sies.movierecomendations.databinding.FragmentHomeBinding
import com.synnapps.carouselview.ImageListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var imageListener: ImageListener
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("HomeFragment", "onCreateView was called")

        viewModel.images.observe(viewLifecycleOwner, { it?.let {
            setUpBanner(it)
        }})

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.search)
                startActivity(Intent(context, SearchActivity::class.java))
            super.onOptionsItemSelected(item)
        }

        val adapter = HomePageAdapter(MovieClickListener {
            viewModel.passDetailsToAdapter(it)
        })

        binding.popMovies.adapter = adapter
        viewModel.popularMovies.observe(viewLifecycleOwner, {
            adapter.submitList(it.results)
        })

//        binding.trendingMovies.adapter = adapter
        viewModel.getTrending().observe(viewLifecycleOwner, {
            binding.trendingMovies.adapter = PopularMoviesAdapter(it)
        })

        viewModel.movies.observe(viewLifecycleOwner, {
            it?.let {
                val intent = Intent(context, MovieDetails::class.java)
                intent.putExtra("backdrop-path", it.backdrop_path)
                intent.putExtra("title", it.title)
                intent.putExtra("release-date", it.release_date)
                intent.putExtra("rating", it.vote_average)
                intent.putExtra("overview", it.overview)
                intent.putExtra("id", it.id)
                startActivity(intent)
            }
        })

        return binding.root
    }

    // Carousel View
    private fun setUpBanner(banner: List<String>) {
        imageListener = ImageListener { position: Int, imageView: ImageView? ->
            imageView?.let {
                Glide.with(this).load(banner[position]).into(imageView)
            }
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