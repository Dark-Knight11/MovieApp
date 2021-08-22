package com.sies.movierecomendations.network;

public class Genres {
    private final int id;
    private final String name;

    public Genres(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }
}

