package com.example.popularmoviespart1.database;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Extend the favorites database to store the movie poster,
// synopsis, user rating, and release date, and display them even when offline.
@Entity(tableName = "favorite")
public class FavoriteEntry {
    @PrimaryKey()
    private int id;
    private String title;
    private String posterPath;
    private String synopsis;
    private float voteAverage;
    private String releaseDate;


    public FavoriteEntry(int id, String title, String posterPath, String synopsis, float voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /* Attributes to https://www.geeksforgeeks.org/override-equalsobject-hashcode-method/*/

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj){
            return true;
        }
        if(obj == null || obj.getClass()!= this.getClass()){
            return false;
        }

        FavoriteEntry favoriteEntry = (FavoriteEntry) obj;
        return (favoriteEntry.id == this.id && favoriteEntry.title.equals(this.title));
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
