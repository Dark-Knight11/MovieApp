package com.sies.movierecomendations.Genre.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.sies.movierecomendations.Genre.data.GenreViewModel
import com.sies.movierecomendations.MovieDetails.ui.MovieDetails
import com.sies.movierecomendations.R
import com.sies.movierecomendations.databinding.ActivityGenreMoviesBinding

class GenreMovies : AppCompatActivity() {

    lateinit var binding: ActivityGenreMoviesBinding
    lateinit var viewModel: GenreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_genre_movies)
        binding.lifecycleOwner = this

        val genreMoviesArgs by navArgs<GenreMoviesArgs>()

        binding.toolbar.title = genreMoviesArgs.name

        viewModel = ViewModelProvider(this).get(GenreViewModel::class.java)
        viewModel.getMovies(genreMoviesArgs.id)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = MovieListAdapter(MovieListClickListener { viewModel.passMovieDetails(it) })
        binding.recyclerView.adapter = adapter

        viewModel.genreMovie.observe(this, { it?.let { adapter.submitList(it.results) }})

        viewModel.movieDetails.observe(this, {
            it?.let {
                val intent = Intent(this, MovieDetails::class.java)
                intent.putExtra("backdrop-path", it.backdrop_path)
                intent.putExtra("title", it.title)
                intent.putExtra("release-date", it.release_date)
                intent.putExtra("rating", it.vote_average)
                intent.putExtra("overview", it.overview)
                intent.putExtra("id", it.id)
                startActivity(intent)
                viewModel.navigationToMovieDetailsDone()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}