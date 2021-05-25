package com.sies.movierecomendations;

import android.widget.RelativeLayout;

public class MovieModelClass {

    private int image;
    private String title;
    private String description;
    private String releaseDate;
    private RelativeLayout frame;

    MovieModelClass(int image, String title,String description, String releaseDate, RelativeLayout frame) {
        this.image=image;
        this.title=title;
        this.description=description;
        this.releaseDate=releaseDate;
        this.frame = frame;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() { return description; }

    public String getReleaseDate() {
        return releaseDate;
    }

    public RelativeLayout getFrame() { return frame; }
}
