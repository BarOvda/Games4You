package com.example.games4you.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.games4you.R;

import java.util.List;

public class ReviewAdapter extends ListAdapter<Review, ReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    private List<Review> mReviews;

    public ReviewAdapter(@NonNull DiffUtil.ItemCallback<Review> diffCallback, Context mContext, List<Review> mReview) {
        super(diffCallback);
        this.mContext = mContext;
        this.mReviews = mReview;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.title.setText(review.getReview_title());
        holder.userName.setText(review.getUser_name());
        holder.ratingBar.setRating((float) review.getRating());
        holder.content.setText(review.getReview());

    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public CardView parentLayout;
        public TextView title;
        public TextView userName;
        public RatingBar ratingBar;
        public ReadMoreTextView content;

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
