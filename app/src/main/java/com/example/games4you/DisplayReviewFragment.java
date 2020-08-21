package com.example.games4you;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.Review;
import com.example.games4you.adapters.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DisplayReviewFragment extends Fragment {

    private Game mGame;
    private TextView name;
    private ImageView pic;
    private RecyclerView reviewRecycle;
    private ReviewAdapter reviewAdapter;
    private List<Review> mReviews;
    private  LinearLayoutManager reviewsLinearLayoutManager;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_review, container, false);

        db = FirebaseFirestore.getInstance();
        name = view.findViewById(R.id.reviews_head);
        pic = view.findViewById(R.id.reviews_game_image);
        reviewRecycle = view.findViewById(R.id.display_review_recycle);
        reviewsLinearLayoutManager = new LinearLayoutManager(this.getContext());

        //============ get game from other activity\fragment ================
        mGame = (Game) getArguments().getSerializable("game");
        name.setText("Reviews for: " + mGame.getName());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        //====================================================================

        reviewRecycle.setHasFixedSize(true);
        mReviews = new ArrayList<>();
        String consoleType = "";
        String con = mGame.getmConsole();

        if (con.equals("ps4"))
            consoleType = "ps4_games";
        else if (con.equals("xbox"))
            consoleType = "xbox_one_games";

        db.collection(consoleType)
                .document(mGame.getName())
                .collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
//
                                Log.d("REVIEW TEXT", String.format("%s",review.getUser_name()));
                                mReviews.add(review);
                            }
                            reviewAdapter = new ReviewAdapter( DisplayReviewFragment.this.getContext(),mReviews, getFragmentManager());
                            reviewRecycle.setLayoutManager(reviewsLinearLayoutManager);
                            reviewRecycle.setAdapter(reviewAdapter);

                        }
                    }
                });


        // ================== On back Pressed =================//

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("game",mGame);
                    bundle.putString("console",mGame.getmConsole());
                    GamePageActivity gamePageActivity = new GamePageActivity();
                    gamePageActivity.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,gamePageActivity).commit();

                    return true;
                }
                return false;
            }
        });

        // ===================================================//

        return view;
    }
}