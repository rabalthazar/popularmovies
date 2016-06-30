package com.example.popularmovies.model;

import java.util.Date;

/**
 * Model class for a movie
 * Created by rafael on 23/05/16.
 */
public class Movie {
    /**
     * TMDB movie id
     */
    protected Long id;

    /**
     * Movie title
     */
    protected String title;

    /**
     * Movie release date
     */
    protected Date releaseDate;

    /**
     * Poster path in TMDB
     */
    protected String posterPath;

    /**
     * Movie overview
     */
    protected String overview;

    /**
     * Adult rated?
     */
    protected Boolean adult;

    /**
     * User rating
     */
    protected Double voteAverage;

    public Long getId() {
        return id;
    }

    public Movie setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Boolean isAdult() {
        return adult;
    }

    public Movie setAdult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }
}
