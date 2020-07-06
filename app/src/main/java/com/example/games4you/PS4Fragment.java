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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PS4Fragment extends Fragment {
    private RecyclerView mRecycleView;
    private GameAdapter mGameAdapter;
    private StorageReference  mStorageRef ;

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


        View view = inflater.inflate(R.layout.fragment_ps4,container,false);
        mRecycleView = view.findViewById(R.id.recycler_view);

        mStorageRef = FirebaseStorage.getInstance().getReference("games_images");

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager((new LinearLayoutManager(this.getContext())));//cheak
        mGames = new ArrayList<>();
        db= FirebaseFirestore.getInstance();
        //mDatabaseReference = FirebaseDatabase.getInstance().getReference("games_images");

        db.collection("games_images")
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
                            mGameAdapter = new GameAdapter(PS4Fragment.this.getContext(),mGames);
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
}


