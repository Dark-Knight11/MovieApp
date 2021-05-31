package com.sies.movierecomendations.PopularMovies;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PopularMoviesClass {
    private TextView movieName;
    private ImageView poster;
    private ConstraintLayout card;

    public PopularMoviesClass(TextView movieName, ImageView poster, ConstraintLayout card) {
        this.movieName = movieName;
        this.poster = poster;
        this.card = card;
    }

    public TextView getMovieName() {
        return movieName;
    }

    public ImageView getPoster() {
        return poster;
    }

    public ConstraintLayout getCard() { return card; }
}
