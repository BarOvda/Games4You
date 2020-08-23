package com.example.games4you.logic;

import android.webkit.WebView;

import com.example.games4you.logic.YouTubeAPI.YoutubeSearchAPI;

public class YouTubeVideo {
    private String url;


    public YouTubeVideo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
