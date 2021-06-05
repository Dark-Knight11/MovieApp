package com.sies.movierecomendations.MoviesApi;

public class Results {
    private final String title;
    private final String overview;
    private final String release_date;
    private final String poster_path;
    private final String backdrop_path;
    private final int id;
    private final float vote_average;


    public Results(String title, String overview, String release_date, String poster_path, String backdrop_path, int id, float vote_average) {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() { return backdrop_path; }

    public int getId() { return id; }

    public float getVote_average() { return vote_average; }
}
