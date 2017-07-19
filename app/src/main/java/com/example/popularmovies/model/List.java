package com.example.popularmovies.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Holds a list of movies, given an determined order: most popular or top rated
 * Created by rafael on 23/06/16.
 */
public class List {
    /**
     * Movie list id
     */
    private Long id;

    /**
     * List order. Actually popular (@string/pref_order_popular) or top_rated (@string/pref_order_toprated)
     */
    private String selection;

    /**
     * Date the list was fetched
     */
    private Date dateFetched;

    /**
     * List of movies in the list
     */
    private final ArrayList<Movie> movies = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSelection() {
        return selection;
    }

    public List setSelection(String selection) {
        this.selection = selection;
        return this;
    }

    public Date getDateFetched() {
        return dateFetched;
    }

    public List setDateFetched(Date dateFetched) {
        this.dateFetched = dateFetched;
        return this;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public Movie getMovie(Integer idx) {
        return movies.size() < idx ?
                movies.get(idx) : null;
    }

    public List add(Movie movie) {
        movies.add(movie);
        return this;
    }

    public List remove(Movie movie) {
        movies.remove(movie);
        return this;
    }

    public List clear() {
        movies.clear();
        return this;
    }
}
