package com.sies.movierecomendations.Search.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sies.movierecomendations.MovieDetails.ui.MovieDetails
import com.sies.movierecomendations.R
import com.sies.movierecomendations.Search.data.SearchViewModel
import com.sies.movierecomendations.databinding.ActivitySearchBinding
import java.util.*

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        val adapter = SearchAdapter(SearchItemClickListener {
            viewModel.passDetailsToAdapter(it)
        })

        binding.recyclerView.adapter = adapter
        viewModel.result.observe(this, { it?.let {
            adapter.submitList(it.results)
        }})

        viewModel.term.observe(this, {
            it?.let {
                val intent = Intent(this, MovieDetails::class.java)
                if (it.media_type == "tv"){
                    intent.putExtra("title", it.original_name)
                    intent.putExtra("release-date", it.first_air_date)
                }
                else {
                    intent.putExtra("title", it.title)
                    intent.putExtra("release-date", it.release_date)
                }
                intent.putExtra("backdrop-path", it.backdrop_path)
                intent.putExtra("rating", it.vote_average)
                intent.putExtra("overview", it.overview)
                intent.putExtra("id", it.id)
                startActivity(intent)
            }
        })

        binding.searchBar.requestFocus()
        binding.voiceSearch.setOnClickListener { voiceSearch() }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchBar.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) viewModel.search(s.toString())
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private var voiceActivityResultLauncher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            // There are no request codes
            val data = it.data
            val text: ArrayList<String>?
            if (data != null) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                binding.searchBar.setText(text?.get(0))
            }
        }
    }

    private fun voiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        if (intent.resolveActivity(packageManager) != null)
            voiceActivityResultLauncher.launch(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}