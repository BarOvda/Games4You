package com.example.games4you;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.games4you.logic.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Game_Page_Information_Fragment extends Fragment {
    private Game mGame;
    private TextView description;

    public Game_Page_Information_Fragment(Game mGame) {
        this.mGame = mGame;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_game__page__information_, container, false);
        description = view.findViewById(R.id.game_description);
        System.err.println("DATA:" + mGame.getmDescription());
        description.setText(mGame.getmDescription());

        return view;
    }
}