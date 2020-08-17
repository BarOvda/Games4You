package com.example.games4you;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.Review;
import com.example.games4you.logic.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GamePageActivity extends Fragment {

    private Button btnPlayTrial;
    private Button btnPlayGamePlay;
    private Game mGame;
    private TextView description;
    private TextView name;
    private ImageView pic;
    private TextView addReview;
    private RecyclerView reviewRecycle;
    private ReviewAdapter reviewAdapter;
    private List<Review> mReviews;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_game_page, container, false);

        btnPlayTrial = view.findViewById(R.id.play_trial_btn);
        btnPlayGamePlay = view.findViewById(R.id.play_gameplay_btn);
        description = view.findViewById(R.id.game_description);
        name = view.findViewById(R.id.game_name);
        pic = view.findViewById(R.id.game_image);
        addReview = view.findViewById(R.id.add_review);

        reviewRecycle = view.findViewById(R.id.game_reviews_recycle);

        //============ get game from other activity\fragment ================
        mGame = (Game) getArguments().getSerializable("game");
        name.setText(mGame.getName());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        description.setText(mGame.getmDescription());
        //====================================================================

        btnPlayTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                TrailerFragment trailerFragment = new TrailerFragment();
                trailerFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, trailerFragment).commit();
            }
        });

        btnPlayGamePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                GamePlayFragment gamePlayFragment = new GamePlayFragment();
                gamePlayFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, gamePlayFragment).commit();
            }
        });

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, reviewFragment).commit();
            }
        });


        //============= Review Recycle View ==========================
//        reviewRecycle.setHasFixedSize(true);
//        reviewRecycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mReviews = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        String consoleType = "";

        if (mGame.getmConsoleType().equals("ps4"))
            consoleType = "ps4_games";
        else if (mGame.getmConsoleType().equals("xbox"))
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
                                review.setReview_title((String)document.get("review_title"));
                                review.setUser_name((String)document.get("user_name"));
                                review.setGame((String)document.get("game"));
                                review.setReview((String)document.get("review"));
                                review.setUser_email((String)document.get("user_email"));

//                                if(!document.get("rating").equals(null))
//                                    review.setRating((double)document.get("rating"));

                                mReviews.add(review);
                            }
                            reviewAdapter = new ReviewAdapter(Review.itemCallback, GamePageActivity.this.getContext(),mReviews);
                            reviewRecycle.setAdapter(reviewAdapter);
                        }
                    }
                });

        return view;
    }
}
