package com.sies.movierecomendations.Genre.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sies.movierecomendations.Genre.data.GenreViewModel
import com.sies.movierecomendations.databinding.ActivityGenreMoviesBinding

class GenreMovies : AppCompatActivity() {

    lateinit var binding: ActivityGenreMoviesBinding
    lateinit var viewModel: GenreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreMoviesBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        val value = intent.getIntExtra("genreId", 12)
        val name = intent.getStringExtra("genre")

        binding.toolbar.title = name

        viewModel = ViewModelProvider(this).get(GenreViewModel::class.java)
        viewModel.getMovies(value)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(this@GenreMovies)

        viewModel.genreMovie.observe(this, {
            binding.recyclerView.adapter = GenreMovieListAdapter(it)
        })

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}