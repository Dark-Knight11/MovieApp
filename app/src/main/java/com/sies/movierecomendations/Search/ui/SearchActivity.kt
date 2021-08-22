package com.sies.movierecomendations.Search.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sies.movierecomendations.Search.data.SearchViewModel
import com.sies.movierecomendations.databinding.ActivitySearchBinding
import java.util.*

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

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

        viewModel.result.observe(this, {
            binding.recyclerView.adapter = SearchAdapter(it)
        })

        setContentView(binding.root)
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