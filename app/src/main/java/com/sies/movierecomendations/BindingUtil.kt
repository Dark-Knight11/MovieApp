package com.sies.movierecomendations.Genre.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sies.movierecomendations.network.Results

@BindingAdapter("imageURI")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = "https://image.tmdb.org/t/p/w500$it"
        Glide.with(imgView.context).load(imgUri).into(imgView)
    }
}
@BindingAdapter("releaseDate")
fun bindTitle(txtView: TextView, title: String?) {
    title?.let {
        val text = "Release Date: $it"
        txtView.text = text
    }
}

@BindingAdapter("searchTerm")
fun bindText(textView: TextView, movie: Results?) {
    movie?.let {
        if (movie.media_type == "tv")
            textView.text = movie.name
        else
            textView.text = movie.title
    }
}
