package com.example.games4you.logic;

import android.webkit.WebView;

public class YouTubeVideo {
    private String url;
    private WebView webView;

    public YouTubeVideo(String url, WebView webView) {
        this.url = url;
        this.webView = webView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}
