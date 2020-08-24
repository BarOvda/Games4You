package com.example.games4you;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;


public class GamePageReviewDisplayFragment extends Fragment {

    FirebaseFirestore db;
    private Game mGame;
    private RecyclerView reviewsRecyclerView;
    private FloatingActionButton addReviewButton;

    public GamePageReviewDisplayFragment(Game mGame) {
        this.mGame = mGame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page_review_display, container, false);

        reviewsRecyclerView = view.findViewById(R.id.review_recyclerview);
        addReviewButton = view.findViewById(R.id.add_review_floating_btn);

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
}