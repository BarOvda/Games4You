package com.example.games4you.logic;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameOffer implements Serializable {
    private String gameName;
    private String gameConsole;
    private String userEmail;
    private String mTitle;
    private String mImageUrl;
    private String mDescription;
    private float mPrice;
    private GeoPoint location;


    public GameOffer(){ }

    public GameOffer(String mTitle, String mImageUrl, String mDescription, float mPrice,GeoPoint location
            ,String gameName,String userEmail,String gameConsole) {
        this.mTitle = mTitle;
        this.mImageUrl = mImageUrl;
        this.mDescription = mDescription;
        this.mPrice = mPrice;
        this.location = location;
        this.userEmail = userEmail;
        this.gameName = gameName;
        this.gameConsole = gameConsole;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public float getmPrice() {
        return mPrice;
    }

    public void setmPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getGameConsole() {
        return gameConsole;
    }

    public void setGameConsole(String gameConsole) {
        this.gameConsole = gameConsole;
    }
}
