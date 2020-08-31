package com.example.games4you;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameOffer;
import com.example.games4you.logic.Review;
import com.example.games4you.logic.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class GamePageReviewDisplayFragment extends Fragment {

    FirebaseFirestore db;

    private ProgressBar gameRating;
    private TextView mTextRating;

    private Game mGame;
    private RecyclerView reviewsRecyclerView;
    private FloatingActionButton addReviewButton;
    private float totalRating;
    public GamePageReviewDisplayFragment(Game mGame) {
        this.mGame = mGame;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page_review_display, container, false);
        reviewsRecyclerView = view.findViewById(R.id.review_recyclerview);
        addReviewButton = view.findViewById(R.id.add_review_floating_btn);
        mTextRating = view.findViewById(R.id.myTextProgress);
        gameRating = view.findViewById(R.id.progressBar);
        db= FirebaseFirestore.getInstance();

        db.collection(mGame.getmConsole())
                .document(mGame.getName())
                .collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Review> reviews = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);

                                reviews.add(review);
                            }
                            totalRating = calculateTotalRating(reviews);
                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);

                            mTextRating.setText(df.format(getTotalRating())+"");
                            ObjectAnimator animation = ObjectAnimator.ofInt(gameRating, "progress", 0, (int)(totalRating*100));

                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.addListener(new Animator.AnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    if(  totalRating>4.0){
                                        gameRating.setProgressTintList(ColorStateList.valueOf(0XFF3CAC23));
                                    }else if(totalRating>3.0){
                                        gameRating.setProgressTintList(ColorStateList.valueOf(0XFFACAA23));
                                    }else{
                                        gameRating.setProgressTintList(ColorStateList.valueOf(0XFFAC2323));
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




                            ReviewAdapter reviewAdapter = new ReviewAdapter(GamePageReviewDisplayFragment.this.getContext(), reviews, getFragmentManager());
                            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            reviewsRecyclerView.setAdapter(reviewAdapter);
                        }
                    }
                });

                        addReviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("game", mGame);
                                ReviewFragment reviewFragment = new ReviewFragment();
                                reviewFragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,reviewFragment)
                                        .addToBackStack("add_review").commit();
                            }
                        });



        return view;
    }


    private float calculateTotalRating(List<Review> mReviews){
        float totalRating = 0;

        for (int i = 0; i< mReviews.size(); i++){
            totalRating += mReviews.get(i).getRating();
        }
        totalRating = totalRating / mReviews.size();

        return totalRating;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }
}