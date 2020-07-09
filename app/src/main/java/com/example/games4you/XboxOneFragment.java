package com.example.games4you;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.google.android.gms.tasks.OnCompleteListener;


import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class XboxOneFragment extends Fragment {
    private RecyclerView mRecycleView;
    private GameAdapter mGameAdapter;


    FirebaseFirestore db;
    //   private DatabaseReference mDatabaseReference;
    private List<Game> mGames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_xbox_one,container,false);
        mRecycleView = view.findViewById(R.id.recycler_view);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager((new LinearLayoutManager(this.getContext())));//cheak
        mGames = new ArrayList<>();
        db= FirebaseFirestore.getInstance();


        db.collection("xbox_one_games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Game game = document.toObject(Game.class);
                                mGames.add(game);

                                Log.d("Data tag", document.getId() + " => " + document.getData());
                            }
                            mGameAdapter = new GameAdapter(XboxOneFragment.this.getContext(),mGames);
                            mRecycleView.setAdapter(mGameAdapter);
                        } else {
                            Log.d("TAG Error", "task.getException()");
                        }
                    }
                });

        return  view;

    }


    public void searchFilter(String txt) {
        mGameAdapter.getFilter().filter(txt);
    }

    public void filterGamesByCategory(List<String> mUserCategoriesSelection) {
        mGameAdapter.filterByCategories(mUserCategoriesSelection);

    }
}


