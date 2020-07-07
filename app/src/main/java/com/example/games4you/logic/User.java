package com.example.games4you.logic;

public class User {
    String email;
    String userName;
    String imageUrl;

    public User(){

    }

    public User(String email, String userName,String imageUrl) {
        this.email = email;
        this.userName = userName;
        this.imageUrl=imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
