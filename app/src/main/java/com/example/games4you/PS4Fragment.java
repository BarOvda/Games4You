package com.example.games4you;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class PS4Fragment extends Fragment {
    private RecyclerView mRecycleView;
    private GameAdapter mGameAdapter;
    private ProgressBar progressBar;

    private SearchView searchView;
    private ImageView filterView;
    boolean[] checkedCategories;
    String[] listCategories;
    String[] listCategoriesToDisplay;
    List<String> mUserCategoriesSelection;
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


        searchView = view.findViewById(R.id.search_button_toolbar);
        filterView = view.findViewById(R.id.search_button_filter);
        mUserCategoriesSelection = new ArrayList<>();
        listCategories = new String[Categories.values().length];
        listCategoriesToDisplay = new String[Categories.values().length];
        getAllCategories();

        checkedCategories = new boolean[listCategories.length];
        filterByCategories();


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
                                game.setmDescription((String)document.get("description"));
                                game.setmTrailer((String)document.get("trailer"));
                                game.setmGamePlay((String)document.get("gameplay"));
                                game.setmConsole((String)document.get("console"));
                                if(i==0)
                                    game.addCategory(Categories.HORROR);
                                game.setmConsole("ps4_games");
                                mGames.add(game);
                                i++;

                            }
                            mGameAdapter = new GameAdapter(PS4Fragment.this.getContext(),mGames, getFragmentManager());
                            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_OF_GAMES_IN_A_ROW));

                            mRecycleView.setAdapter(mGameAdapter);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    searchFilter(newText);
                return true;
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

    public void filterByCategories(){

        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("filter","pressed");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Games Filter");
                mBuilder.setMultiChoiceItems(listCategoriesToDisplay, checkedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        //     if(isChecked){
                        if(!mUserCategoriesSelection.contains(listCategories[position])){
                            mUserCategoriesSelection.add(listCategories[position]);
                        }else{
                            mUserCategoriesSelection.remove(listCategories[position]);
                        }
                    }
                    //}
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            filterGamesByCategory(mUserCategoriesSelection);


                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkedCategories.length;i++)
                            checkedCategories[i]=false;
                        mUserCategoriesSelection.clear();

                            filterGamesByCategory(mUserCategoriesSelection);


                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
    private void getAllCategories() {
        int i=0;
        for (Categories category : Categories.values()) {
            listCategories[i] = category.toString();
            listCategoriesToDisplay[i] =onlyAlphabet(category.toString());
            i++;
        }
    }
    private  String stripAccents(String s) {
        String s1 = Normalizer.normalize(s, Normalizer.Form.NFD);
        s1 = s1.replaceAll("[\\p{InCombiningDiacriticalMarks}]", " ");

        return s1;
    }
    private String onlyAlphabet (String string){
        return stripAccents(string)
                // deletes anything not letter or space
                .replaceAll("[^A-Za-z\\s]", " ")
                // converts chains of blanks in single spaces
                .replaceAll("\\s{2,}", " ")
                // gets lower case
                .toLowerCase();
    }
    public void resetFilterChoices(){
        for(int i=0;i<checkedCategories.length;i++)
            checkedCategories[i]=false;
        mUserCategoriesSelection.clear();
    }
}


