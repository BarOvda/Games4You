package com.example.games4you;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.example.games4you.logic.Game;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

public class GamePageActivity extends YouTubeBaseActivity {

    public static final String YOUTUBE_CRED = "AIzaSyBsgtxTZEPQvf1_M34bJCmwlL1ROcJGu7c";

    private YouTubePlayerView youTubePlayerViewTrial;
    private YouTubePlayerView youTubePlayerViewGamePlay;
    private YouTubePlayer.OnInitializedListener onInitializedListenerTrial;
    private YouTubePlayer.OnInitializedListener onInitializedListenerGamePlay;
    private Button btnPlayTrial;
    private Button btnPlayGamePlay;
    private Game mGame;
    private TextView description;
    private TextView name;
    private ImageView pic;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        youTubePlayerViewTrial = findViewById(R.id.trial_youtube);
        youTubePlayerViewGamePlay =findViewById(R.id.gameplay_youtube);
        btnPlayTrial = findViewById(R.id.play_trial_btn);
        btnPlayGamePlay = findViewById(R.id.play_gameplay_btn);
        description = findViewById(R.id.game_description);
        name = findViewById(R.id.game_name);
        pic = findViewById(R.id.game_image);

        //============ get game from other activity\fragment ================
        mGame = (Game)getIntent().getSerializableExtra("game");
        Log.e("GAME","name: " + mGame.getName()+ " des: " + mGame.getmDescription()) ;
        name.setText(mGame.getName());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        description.setText(mGame.getmDescription());
        //====================================================================

        onInitializedListenerTrial = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.loadVideo(mGame.getmTrailer());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };

        onInitializedListenerGamePlay = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.loadPlaylist(mGame.getmGamePlay());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };

        btnPlayTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerViewTrial.initialize(YOUTUBE_CRED, onInitializedListenerTrial);
            }
        });

        btnPlayGamePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerViewGamePlay.initialize(YOUTUBE_CRED, onInitializedListenerGamePlay);
            }
        });

    }
}
