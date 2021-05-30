package com.sies.movierecomendations.PopularMovies;

import android.widget.ImageView;
import android.widget.TextView;

public class PopularMoviesClass {
    private TextView movieName;
    private ImageView poster;

    public PopularMoviesClass(TextView movieName, ImageView poster) {
        this.movieName = movieName;
        this.poster = poster;
    }

    public TextView getMovieName() {
        return movieName;
    }

    public ImageView getPoster() {
        return poster;
    }
}
