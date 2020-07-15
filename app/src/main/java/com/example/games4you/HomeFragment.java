package com.example.games4you;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.example.games4you.logic.ebay_plugin.EbayDriver;
import com.google.android.gms.tasks.OnCompleteListener;


import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView mXboxOneRecycleView;
    private RecyclerView mPs4RecycleView;
    private GameAdapter mGameAdapter;
    private ProgressBar homeProggresBar;
    private TextView xboxText,psText;
    FirebaseFirestore db;
    //   private DatabaseReference mDatabaseReference;
    private List<Game> mPs4Games;
    private List<Game> mXboxOneGames;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        mXboxOneRecycleView = view.findViewById(R.id.xbox_one_recycler_view);
        mPs4RecycleView = view.findViewById(R.id.ps4_recycler_view);
        homeProggresBar = view.findViewById(R.id.home_proggressBar);
        xboxText = view.findViewById(R.id.xbox_text);
        psText = view.findViewById(R.id.ps_text);
        mXboxOneRecycleView.setHasFixedSize(true);
        mPs4RecycleView.setHasFixedSize(true);

        mPs4Games = new ArrayList<>();
        mXboxOneGames = new ArrayList<>();
        db= FirebaseFirestore.getInstance();

        LinearLayoutManager xboxoneLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager ps4LayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);

        mXboxOneRecycleView.setLayoutManager(xboxoneLayoutManager);
        mPs4RecycleView.setLayoutManager(ps4LayoutManager);

        EbayDriver driver = new EbayDriver();
        String tag = "Velo binding machine";
        try {
            driver.runDriver(java.net.URLEncoder.encode(tag, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        db.collection("xbox_one_games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            psText.setVisibility(View.VISIBLE);
                            xboxText.setVisibility(View.VISIBLE);
                            homeProggresBar.setVisibility(View.GONE);
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Game game = document.toObject(Game.class);
                                mXboxOneGames.add(game);

                            }
                            mGameAdapter = new GameAdapter(HomeFragment.this.getContext(),mXboxOneGames);
                            mXboxOneRecycleView.setAdapter(mGameAdapter);
                        } else {
                            psText.setVisibility(View.GONE);
                            xboxText.setVisibility(View.GONE);
                            homeProggresBar.setVisibility(View.VISIBLE);
                        }
                    }
                });


        db.collection("ps4_games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Game game = document.toObject(Game.class);
                                mPs4Games.add(game);

                            }
                            mGameAdapter = new GameAdapter(HomeFragment.this.getContext(),mPs4Games);
                            mPs4RecycleView.setAdapter(mGameAdapter);
                        } else {

                        }
                    }
                });

        return  view;

    }


    public void searchFilter(String txt) {
        mGameAdapter.getFilter().filter(txt);
    }
}


