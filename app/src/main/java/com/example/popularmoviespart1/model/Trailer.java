package com.example.popularmoviespart1.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Trailer implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String isoOne;
    @SerializedName("iso_3166_1")
    private String isoTwo;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name; // youtube or vimeo
    @SerializedName("site")
    private String site; // youtube or vimeo
    @SerializedName("size")
    private Integer size; // youtube or vimeo
    @SerializedName("type")
    private String type; //trailer or feauturette

    public Trailer(String id, String isoOne, String isoTwo, String key, String name,
                   String site, Integer size, String type) {
        this.id = id;
        this.isoOne = isoOne;
        this.isoTwo = isoTwo;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }


    protected Trailer(Parcel in) {
        id = in.readString();
        isoOne = in.readString();
        isoTwo = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        type = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return (this.key + '\n' + '\n' + this.type + '\n' + this.site);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsoOne() {
        return isoOne;
    }

    public void setIsoOne(String isoOne) {
        this.isoOne = isoOne;
    }

    public String getIsoTwo() {
        return isoTwo;
    }

    public void setIsoTwo(String isoTwo) {
        this.isoTwo = isoTwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(isoOne);
        dest.writeString(isoTwo);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        if (size == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(size);
        }
        dest.writeString(type);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
