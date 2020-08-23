package com.example.games4you;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.ebay_plugin.EbayDriver;
import com.example.games4you.logic.ebay_plugin.EbayListAdapter;



public class GamePageEbayFragment extends Fragment {
    private static final int NUMBER_OF_GAMES_IN_A_ROW = 2;
    private Game mGame;
    private LinearLayoutManager ebayLinearLayoutManager;
    private GamePageFragment.IProcess mProcess;
    private RecyclerView EbayTitleRecyclerView;
    private EbayDriver driver;

    private com.example.games4you.logic.ebay_plugin.EbayListAdapter EbayListAdapter;

/*
    private LinearLayoutManager offersLinearLayoutManager;
*/

    public GamePageEbayFragment(Game mGame) {
        this.mGame = mGame;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page_ebay, container, false);
        EbayTitleRecyclerView = view.findViewById(R.id.ebay_recyclerview);

        EbayTitleRecyclerView.setHasFixedSize(true);

      /*
        offersLinearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
*/
/*
        reviewsLinearLayoutManager = new LinearLayoutManager(this.getContext());
*/


        mProcess = new GamePageFragment.IProcess() {
            @Override
            public void updateAdapter() {

                EbayListAdapter.notifyDataSetChanged();

            }
        };

        String tag = mGame.getName();
        driver = new EbayDriver(tag,mProcess,mGame.getmConsole());

        try {
            driver.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EbayListAdapter = new EbayListAdapter(GamePageEbayFragment.this.getContext(),driver.getTitles(),getFragmentManager());
        EbayTitleRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_OF_GAMES_IN_A_ROW));
        EbayTitleRecyclerView.setAdapter(EbayListAdapter);


        return view;
    }
}