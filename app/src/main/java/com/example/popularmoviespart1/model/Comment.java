package com.example.popularmoviespart1.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String comment;
    @SerializedName("url")
    private String url;


    public Comment(String id, String author, String comment, String url) {
        this.id = id;
        this.author = author;
        this.comment = comment;
        this.url = url;
    }

    private Comment(Parcel in) {
        id = in.readString();
        author = in.readString();
        comment = in.readString();
        url = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(comment);
        dest.writeString(url);
    }

    @NonNull
    @Override
    public String toString() {
        return (this.id + '\n' + this.author + '\n' + this.comment + '\n' + this.url + '\n');
    }
}
