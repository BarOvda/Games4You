package com.example.games4you.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {

    private String mName;
    private String mImageUrl;
    private List<Categories> mCategories;
    public Game(){

    }

    public Game(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mCategories = new ArrayList<>();
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
}
