package com.sies.movierecomendations.MoviesApi;

import java.util.List;

public class MoviesList {
    int page;
    private List<Results> results;

    public int getPage() {
        return page;
    }

    public List<Results> getResults() {
        return results;
    }

    public MoviesList(int page, List<Results> results) {
        this.page = page;
        this.results = results;
    }
}

