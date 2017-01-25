package com.mahokyin.showcollege.rest;

import android.graphics.Movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahokyin on 1/22/17.
 */

public class ImageJson {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnailUrl")
    @Expose
    private String url;
    //private String webUrl;

    public ImageJson(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
