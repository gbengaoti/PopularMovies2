package com.example.popularmoviespart1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponsePage {
    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Trailer> results;

    public TrailerResponsePage(Integer id, List<Trailer> results) {
        this.id = id;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
