package com.example.games4you;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameOffer;
import com.example.games4you.logic.GameOfferAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class GamePageUsersOffersDisplyFragment extends Fragment {
    private static final int NUMBER_OF_GAMES_IN_A_ROW =2;
    private Game mGame;
    private RecyclerView OffersRecyclerView;
    private FloatingActionButton addOfferButton;
    FirebaseFirestore db;

    public GamePageUsersOffersDisplyFragment(Game mGame) {
        this.mGame = mGame;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page_users_offers_disply, container, false);
        OffersRecyclerView = view.findViewById(R.id.offers_recyclerview);

        addOfferButton =  view.findViewById(R.id.fab);

        db= FirebaseFirestore.getInstance();



        db.collection(mGame.getmConsole())
                .document(mGame.getName())
                .collection("offers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<GameOffer> offers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                GameOffer offer = document.toObject(GameOffer.class);

                                offer.setmDescription((String)document.get("mDescription"));
                                offer.setmImageUrl((String)document.get("mImageUrl"));
                             //   offer.setmPrice(new Float((String) document.get("mPrice")));
                                offer.setmTitle((String)document.get("mTitle"));


                                offers.add(offer);

                            }
                            GameOfferAdapter mGameAdapter = new GameOfferAdapter(
                                    GamePageUsersOffersDisplyFragment.this.getContext(),offers,getFragmentManager());
                            OffersRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_OF_GAMES_IN_A_ROW));
                            OffersRecyclerView.setAdapter(mGameAdapter);
                        }
                    }
                });
        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                UserOfferForGameFragment userOfferForGameFragment = new UserOfferForGameFragment();
                userOfferForGameFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,userOfferForGameFragment)
                        .addToBackStack("add_offer").commit();
            }
        });
        return view;
    }
}