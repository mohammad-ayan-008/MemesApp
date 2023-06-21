package com.example.RetrofitApiCaller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Memes {

    String url;
    String title;

    public Memes(String url, String Title) {
        this.url = url;
        this.title = Title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }
}
