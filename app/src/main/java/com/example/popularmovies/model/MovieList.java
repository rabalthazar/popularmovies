package com.example.popularmovies.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rafael on 23/06/16.
 */
public class MovieList {
    protected Long id;

    protected String order;

    protected Date dateFetched;

    protected ArrayList<Movie> movies = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public MovieList setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public MovieList setOrder(String order) {
        this.order = order;
        return this;
    }

    public Date getDateFetched() {
        return dateFetched;
    }

    public MovieList setDateFetched(Date dateFetched) {
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

    public MovieList add(Movie movie) {
        movies.add(movie);
        return this;
    }

    public MovieList remove(Movie movie) {
        movies.remove(movie);
        return this;
    }

    public MovieList remove(Integer idx) {
        movies.remove(idx);
        return this;
    }

    public MovieList clear() {
        movies.clear();
        return this;
    }
}
