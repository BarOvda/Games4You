package com.example.games4you.logic;

import java.io.Serializable;

public class EbayTitle implements Serializable {
    private String itemUrl;
    private String currentPrice;
    private String itemId;
    private String title;
    private String galleryUrl;
    public EbayTitle(){};
    public EbayTitle(String itemUrl, String currentPrice, String itemId, String title, String galleryUrl) {
        this.itemUrl = itemUrl;
        this.currentPrice = currentPrice;
        this.itemId = itemId;
        this.title = title;
        this.galleryUrl = galleryUrl;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGalleryUrl() {
        return galleryUrl;
    }

    public void setGalleryUrl(String galleryUrl) {
        this.galleryUrl = galleryUrl;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
