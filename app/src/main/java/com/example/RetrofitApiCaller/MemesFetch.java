package com.example.RetrofitApiCaller;

import java.util.List;

public class MemesFetch {

    private List<Memes> memes;

    public MemesFetch(List<Memes> memes) {
        this.memes = memes;
    }

    public List<Memes> getMemes() {
        return this.memes;
    }

    public void setMemes(List<Memes> memes) {
        this.memes = memes;
    }
}
