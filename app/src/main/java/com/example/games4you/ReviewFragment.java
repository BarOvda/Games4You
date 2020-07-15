package com.example.games4you;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.example.games4you.logic.Review;
import com.example.games4you.logic.ebay_plugin.EbayDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {
  private String mGameName;
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);


        mGamePhoto = view.findViewById(R.id.game_photo);
        mRating = (RatingBar)  view.findViewById(R.id.user_rating);
        mHeader = view.findViewById(R.id.textView3);
        mReviewTitle = view.findViewById(R.id.user_review_title);
        mReview = view.findViewById(R.id.user_review);
        mSubmitButton = view.findViewById(R.id.submit_review);

        //for testing
        mGameTitle = "The Last Of Us 2";


        mRating.setNumStars(5);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = (double)mRating.getRating();
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                Review review = new Review(mGameTitle,userEmail,rating,
                        mReview.getText().toString(),mReviewTitle.getText().toString());
                setFragmentVisabilty();
                if(mGameConsoleType.equals("ps4")){
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
                                    new HomeFragment(),"XBOX_ONE_FRAGMENT").commit();
                        }
                    });}
                else if(mGameConsoleType.equals("XboxOne")){
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
                                    new HomeFragment(),"XBOX_ONE_FRAGMENT").commit();
                        }
                    });}

            }
        })
        ;

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