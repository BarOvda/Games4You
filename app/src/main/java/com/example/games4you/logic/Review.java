package com.example.games4you.logic;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class Review {

    private String user_name;
    private String game;
    private String user_email;
    private double rating;
    private String review;
    private String review_title;

    public Review() {
    }

    public Review(String game, String user_email, double rating, String review, String review_title, String user_name) {
        this.game = game;
        this.user_email = user_email;
        this.rating = rating;
        this.review = review;
        this.review_title = review_title;
        this.user_name = user_name;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReview_title() {
        return review_title;
    }

    public void setReview_title(String review_title) {
        this.review_title = review_title;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public static DiffUtil.ItemCallback<Review> itemCallback = new DiffUtil.ItemCallback<Review>() {
        @Override
        public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.getUser_email().equals(newItem.user_email);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return false;
        }
    };
}
