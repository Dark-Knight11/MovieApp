package com.sies.movierecomendations.MoviesApi;

import java.util.List;

public class VideosList {
    int id;
    private final List<VideoResults> results;

    public List<VideoResults> getResults() {
        return results;
    }

    public VideosList(int id, List<VideoResults> results) {
        this.id = id;
        this.results = results;
    }
}
