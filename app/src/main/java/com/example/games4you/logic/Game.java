package com.example.games4you.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game implements Serializable {

    private String mName;
    private String mImageUrl;
    private List<Categories> mCategories;
    private String mDescription;
    private String mTrailer;
    private String mGamePlay;
    public Game(){
        this.mCategories = new ArrayList<>();
    }

    public Game(String mName, String mImageUrl, String mDescription, String mTrailer, String mGamePlay) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mCategories = new ArrayList<>();
        this.mDescription = mDescription;
        this.mTrailer = mTrailer;
        this.mGamePlay = mGamePlay;
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
    public List<Categories> getmCategories() {
        return mCategories;
    }

    public void setmCategories(List<Categories> mCategories) {
        this.mCategories = mCategories;
    }
    public void addCategory(Categories Category){
        this.mCategories.add(Category);
    }
    public void addCategories(List<Categories> Categories){
        this.mCategories.addAll(Categories);
    }

    public String getmDescription() { return mDescription; }

    public String getmTrailer() { return mTrailer; }

    public String getmGamePlay() { return mGamePlay; }
}
