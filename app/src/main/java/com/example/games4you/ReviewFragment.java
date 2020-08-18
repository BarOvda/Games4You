package com.example.games4you;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class ReviewFragment extends Fragment {
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private String mGameTitle;
    private String mGameConsoleType;
    private ImageView mGamePhoto;
    private RatingBar mRating;
    private TextView mHeader;
    private EditText mReviewTitle;
    private EditText mReview;
    private Button mSubmitButton;
    private Game mGame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);
        mGame = (Game) getArguments().getSerializable("game");

        mGamePhoto = view.findViewById(R.id.game_photo);
        mRating = (RatingBar) view.findViewById(R.id.user_rating);
        mHeader = view.findViewById(R.id.textView3);
        mReviewTitle = view.findViewById(R.id.user_review_title);
        mReview = view.findViewById(R.id.user_review);
        mSubmitButton = view.findViewById(R.id.submit_review);

        mGameTitle = mGame.getName();
        Picasso.get().load(mGame.getImageUrl()).into(mGamePhoto);
        mGameConsoleType = mGame.getmConsole();

        mRating.setNumStars(5);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = mRating.getRating();
                String userEmail = currentUser.getEmail();
                String userName = currentUser.getDisplayName();
                Review review = new Review(mGameTitle, userEmail, rating,
                        mReview.getText().toString(), mReviewTitle.getText().toString(), userName);
                setFragmentVisabilty();
                if (mGameConsoleType.equals("ps4")) {
                    db.collection("ps4_games")
                            .document(mGameTitle)
                            .collection("reviews")
                            .document(userEmail)
                            .set(review)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Your review was upload", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new HomeFragment(), "PS4_FRAGMENT").commit();
                                }
                            });
                } else if (mGameConsoleType.equals("xbox")) {
                    db.collection("xbox_one_games")
                            .document(mGameTitle)
                            .collection("reviews")
                            .document(userEmail)
                            .set(review)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Your review was upload", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new HomeFragment(), "XBOX_ONE_FRAGMENT").commit();
                                }
                            });
                }
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("game", mGame);
                    bundle.putString("console", mGame.getmConsole());
                    GamePageActivity gamePageActivity = new GamePageActivity();
                    gamePageActivity.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, gamePageActivity).commit();

                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void setFragmentVisabilty() {
        mGamePhoto.setVisibility(View.GONE);
        mGamePhoto.setFocusableInTouchMode(false);
        mRating.setVisibility(View.GONE);
        mRating.setFocusableInTouchMode(false);
        mHeader.setVisibility(View.GONE);
        mHeader.setFocusableInTouchMode(false);
        mReviewTitle.setVisibility(View.GONE);
        mReviewTitle.setFocusableInTouchMode(false);
        mReview.setVisibility(View.GONE);
        mReview.setFocusableInTouchMode(false);
        mSubmitButton.setVisibility(View.GONE);
        mSubmitButton.setFocusableInTouchMode(false);

    }

    public String getmGameConsoleType() {
        return mGameConsoleType;
    }

    public void setmGameConsoleType(String mGameConsoleType) {
        this.mGameConsoleType = mGameConsoleType;
    }

    public String getmGameTitle() {
        return mGameTitle;
    }

    public void setmGameTitle(String mGameTitle) {
        this.mGameTitle = mGameTitle;
    }
}