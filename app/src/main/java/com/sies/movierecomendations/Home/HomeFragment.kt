package com.sies.movierecomendations.Home

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
import com.sies.movierecomendations.PopularMovies.PopularMoviesAdapter
import com.sies.movierecomendations.R
import com.sies.movierecomendations.SearchActivity
import com.sies.movierecomendations.databinding.FragmentHomeBinding
import com.synnapps.carouselview.ImageListener

class HomeFragment: Fragment() {

    private val images = arrayOf(
        "https://image.tmdb.org/t/p/original/srYya1ZlI97Au4jUYAktDe3avyA.jpg",
        "https://image.tmdb.org/t/p/original/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
        "https://image.tmdb.org/t/p/original/inJjDhCjfhh3RtrJWBmmDqeuSYC.jpg",
        "https://image.tmdb.org/t/p/original/gGSm6ZmWtGazs2H1m0gOp7cx1ZZ.jpg",
        "https://image.tmdb.org/t/p/original/pcDc2WJAYGJTTvRSEIpRZwM3Ola.jpg",
        "https://image.tmdb.org/t/p/original/2lBOQK06tltt8SQaswgb8d657Mv.jpg"
    )
    var imageListener: ImageListener? = null
    lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("HomeFragment", "onCreateView was called")

        setUpBanner(images)

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.search)
                startActivity(Intent(context, SearchActivity::class.java))
            super.onOptionsItemSelected(item)
        }
        binding.popMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.popularMovies.observe(viewLifecycleOwner, {
            binding.popMovies.adapter = PopularMoviesAdapter(it)
        })

        binding.trendingMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.trendingMovies.observe(viewLifecycleOwner, {
            binding.trendingMovies.adapter = PopularMoviesAdapter(it)
        })

        return binding.root
    }

    // Settings for carousel view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.carousel.pageCount = images.size
        binding.carousel.setImageListener(imageListener)

        Log.i("HomeFragment", "onViewCreated was called")
    }

    // Carousel View
    private fun setUpBanner(banner: Array<String>) {
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