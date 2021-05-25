package com.sies.movierecomendations;

public class Results {
    private String title;
    private String overview;
    private String release_date;
    private String poster_path;
    private String backdrop_path;


    public Results(String title, String overview, String release_date, String poster_path, String backdrop_path) {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
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
}
