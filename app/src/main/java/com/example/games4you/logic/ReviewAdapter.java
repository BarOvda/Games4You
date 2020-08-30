package com.example.games4you.logic;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.games4you.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private List<Review> mReviews;
    FragmentManager manager;
    Review review;
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
    public void onBindViewHolder(@NonNull final ReviewViewHolder holder, int position) {
         review = mReviews.get(position);
        holder.title.setText(review.getReview_title());

        holder.ratingText.setText(review.getRating()+"");
        ObjectAnimator animation = ObjectAnimator.ofInt(holder.ratingBar, "progress", 0, (int)review.getRating()*100); // see this max value coming back here, we animate towards that value

        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationStart(Animator animation) {
                if(  review.getRating()>4.0){
                    holder.ratingBar.setProgressTintList(ColorStateList.valueOf(0XFF3CAC23));
                }else if( review.getRating()>3.0){
                    holder.ratingBar.setProgressTintList(ColorStateList.valueOf(0XFFACAA23));
                }else{
                    holder.ratingBar.setProgressTintList(ColorStateList.valueOf(0XFFAC2323));
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        animation.start();
        holder.content.setText(review.getReview());

    }

    @Override
    public int getItemCount() {
        return  mReviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public CardView parentLayout;
        public TextView title;
        public ProgressBar ratingBar;
        public TextView ratingText;
        public TextView content;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentLayout = itemView.findViewById(R.id.review_parent_layout);
            this.title = itemView.findViewById(R.id.review_title);
            this.ratingBar = itemView.findViewById(R.id.review_rating_bar);
            this.ratingText = itemView.findViewById(R.id.myTextProgress);
            this.content = itemView.findViewById(R.id.review_content);
        }
    }
}
