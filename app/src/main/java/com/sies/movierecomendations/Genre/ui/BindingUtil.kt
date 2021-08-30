package com.sies.movierecomendations.Genre.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sies.movierecomendations.R

@BindingAdapter("imageURI")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = "https://image.tmdb.org/t/p/w500$it"
        Glide.with(imgView.context).load(imgUri).placeholder(R.drawable.common_full_open_on_phone).fitCenter().into(imgView)
    }
}
@BindingAdapter("releaseDate")
fun bindTitle(txtView: TextView, title: String?) {
    title?.let {
        val text = "Release Date: $it"
        txtView.text = text
    }
}
