package com.sies.movierecomendations.GenreApi;

import java.util.List;

public class GenreList {
    private List<Genres> genres;

    public List<Genres> getGenres() { return genres; }

    public GenreList(List<Genres> genres) { this.genres = genres; }

}
