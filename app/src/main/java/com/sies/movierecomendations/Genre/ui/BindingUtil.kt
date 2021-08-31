package com.sies.movierecomendations.Genre.ui

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageURI")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = "https://image.tmdb.org/t/p/w500$it"
        Log.i("imageURL", imgUri)
        Glide.with(imgView).load(imgUri).into(imgView)
    }
}
@BindingAdapter("releaseDate")
fun bindTitle(txtView: TextView, date: String?) {
    date?.let {
        Log.i("date", it)
        val text = "Release Date: $it"
        txtView.text = text
        Log.i("date", text)
    }
}
