package com.example.games4you;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Categories;
import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class PS4Fragment extends Fragment {
    private RecyclerView mRecycleView;
    private GameAdapter mGameAdapter;
    private ProgressBar progressBar;
    private final static int NUMBER_OF_GAMES_IN_A_ROW = 2;

    FirebaseFirestore db;
    private List<Game> mGames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ps4,container,false);
        mRecycleView = view.findViewById(R.id.recycler_view);
        progressBar =  view.findViewById(R.id.progressBar);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager((new LinearLayoutManager(this.getContext())));//cheak
        mGames = new ArrayList<>();
        db= FirebaseFirestore.getInstance();
        //mDatabaseReference = FirebaseDatabase.getInstance().getReference("games_images");

        db.collection("ps4_games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Game game = document.toObject(Game.class);
                                if(i==0)
                                    game.addCategory(Categories.HORROR);
                                mGames.add(game);
                                i++;

                            }
                            mGameAdapter = new GameAdapter(PS4Fragment.this.getContext(),mGames);
                            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_OF_GAMES_IN_A_ROW));

                            mRecycleView.setAdapter(mGameAdapter);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });

        return  view;

    }


    public void searchFilter(String txt) {
        mGameAdapter.getFilter().filter(txt);
    }

    public void filterGamesByCategory(List<String> categories){
       mGameAdapter.filterByCategories(categories);
    }
}


