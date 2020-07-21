package com.example.games4you;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.games4you.logic.Game;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;


public class TrailerFragment extends Fragment {

    public static final String YOUTUBE_CRED = "AIzaSyBsgtxTZEPQvf1_M34bJCmwlL1ROcJGu7c";
    private Game mGame;
    private String trailer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_trailer, container, false);

        mGame = (Game)getArguments().getSerializable("game");
        trailer = mGame.getmTrailer();

        YouTubePlayerSupportFragmentX youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        FragmentManager manager = getFragmentManager();

        youTubePlayerFragment.initialize(YOUTUBE_CRED, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.loadVideo(trailer);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.youtube_trailer_fragment,  youTubePlayerFragment).commit();

        return view;
    }

}