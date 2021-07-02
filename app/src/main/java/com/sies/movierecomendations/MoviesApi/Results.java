package com.sies.movierecomendations.MoviesApi;

public class Results {
    private final String title;
    private final String original_title;
    private final String overview;
    private final String release_date;
    private final String poster_path;
    private final String backdrop_path;
    private final String media_type;
    private final int id;
    private final float vote_average;

    private final String name;
    private final String original_name;
    private final String first_air_date;

    public Results(String title, String original_title, String name, String overview, String release_date, String poster_path, String backdrop_path, String media_type, int id, float vote_average, String original_name, String first_air_date) {
        this.title = title;
        this.original_title = original_title;
        this.name = name;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.media_type = media_type;
        this.id = id;
        this.vote_average = vote_average;
        this.original_name = original_name;
        this.first_air_date = first_air_date;
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

    public String getName() { return name; }

    public String getFirst_air_date() { return first_air_date; }

    public String getMedia_type() { return media_type; }

    public String getOriginal_title() { return original_title; }

    public String getOriginal_name() { return original_name; }
}
