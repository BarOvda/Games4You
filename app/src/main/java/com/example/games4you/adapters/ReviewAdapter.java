package com.example.games4you.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.R;
import com.example.games4you.logic.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private List<Review> mReviews;
    FragmentManager manager;

    public ReviewAdapter( Context mContext, List<Review> mReview, FragmentManager manager) {
        this.mContext = mContext;
        this.mReviews = mReview;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewAdapter.ReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.review_item, parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.title.setText(review.getReview_title());
        holder.userName.setText(review.getUser_name());
        holder.ratingBar.setRating((float) review.getRating());
        holder.content.setText(review.getReview());

    }

    @Override
    public int getItemCount() {
        return  mReviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public CardView parentLayout;
        public TextView title;
        public TextView userName;
        public RatingBar ratingBar;
        public TextView content;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentLayout = itemView.findViewById(R.id.review_parent_layout);
            this.title = itemView.findViewById(R.id.review_title);;
            this.userName = itemView.findViewById(R.id.review_user_name);;
            this.ratingBar = itemView.findViewById(R.id.review_rating_bar);;
            this.content = itemView.findViewById(R.id.review_content);;
        }
    }
}
