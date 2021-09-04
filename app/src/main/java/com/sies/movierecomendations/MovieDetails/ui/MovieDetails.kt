package com.sies.movierecomendations.MovieDetails.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sies.movierecomendations.MovieDetails.data.MovieDetailViewModel
import com.sies.movierecomendations.R
import com.sies.movierecomendations.databinding.ActivityMovieDetailScreenBinding
import kotlinx.coroutines.launch

class MovieDetails: AppCompatActivity() {

    lateinit var binding: ActivityMovieDetailScreenBinding
    lateinit var viewModel: MovieDetailViewModel
    var media = "movie"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_screen)
        binding.lifecycleOwner = this

        val backdropPath = intent.getStringExtra("backdrop-path")
        val name = intent.getStringExtra("title")
        val releaseDate = intent.getStringExtra("release-date")
        val votes = intent.getFloatExtra("rating", 0f)
        val overview = intent.getStringExtra("overview")
        val id = intent.getIntExtra("id", 0)
        val flag = intent.getIntExtra("flag", 1)

        if (flag != 1) media = "tv"
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        lifecycleScope.launch {
            viewModel.getApi(media, id)
        }

        Glide.with(this@MovieDetails).load("https://image.tmdb.org/t/p/original$backdropPath")
            .into(binding.backdrop)
        binding.title.text = name
        binding.date.text = "Release Date: $releaseDate"
        binding.overview.text = overview
        binding.rating.text = "Rating: $votes"

        viewModel.ytLink.observe(this, { videoKey ->
            binding.youtube.text = "https://www.youtube.com/watch?v=$videoKey"
            binding.youtube.setOnClickListener {
                val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoKey"))
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent2.setPackage("com.google.android.youtube")
                startActivity(intent2)
            }
            binding.youtube.visibility = View.VISIBLE
        })

    }
}