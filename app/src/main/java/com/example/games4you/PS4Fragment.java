package com.example.games4you;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PS4Fragment extends Fragment {
    private RecyclerView mRecycleView;
    private GameAdapter mGameAdapter;

    private DatabaseReference mDatabaseReference;
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
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager((new LinearLayoutManager(this.getContext())));//cheak
        mGames = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("games_images");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Game g = dataSnapshot.getValue(Game.class);

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Game game = postSnapshot.getValue(Game.class);
                    mGames.add(game);
                }
                mGames.add(new Game("God Of War","https://www.dominator.co.il/images/itempics/1129_large.jpg"));
                mGames.add(new Game("God Of War","https://www.dominator.co.il/images/itempics/1129_large.jpg"));
                mGames.add(new Game("God Of War","https://www.dominator.co.il/images/itempics/1129_large.jpg"));
                mGames.add(new Game("God Of War","https://www.dominator.co.il/images/itempics/1129_large.jpg"));
                mGameAdapter = new GameAdapter(PS4Fragment.this.getContext(),mGames);
                mRecycleView.setAdapter(mGameAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PS4Fragment.this.getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return  view;

    }


    public void searchFilter(String txt) {
        mGameAdapter.getFilter().filter(txt);
    }
}
