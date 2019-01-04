package com.example.yara.popularmovieapp.MovieDetails.Trailers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yara on 01-Nov-18.
 */

public class TrailersResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailsResults> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailsResults> getResults() {
        return results;
    }

    public void setResults(List<TrailsResults> results) {
        this.results = results;
    }

}
