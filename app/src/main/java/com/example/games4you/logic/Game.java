package com.example.games4you.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {
    private String mName;
    private String mImageUrl;

    public Game(){

    }

    public Game(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;

    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
