package com.example.yara.popularmovieapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Yara on 20-Dec-18.
 */
@Entity(tableName = "Movies")
public class MovieEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    int movie_id;
    String title,posterPath,releaseDate;
    double voteAverage;


    public MovieEntry(int id, int movie_id, String title, String posterPath, String releaseDate, double voteAverage) {
        this.id = id;
        this.movie_id = movie_id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }
    @Ignore
    public MovieEntry(int movie_id, String title, String posterPath, String releaseDate, double voteAverage) {
        this.movie_id = movie_id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

}
