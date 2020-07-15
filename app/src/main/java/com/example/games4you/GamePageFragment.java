package com.example.games4you;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.games4you.logic.Game;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import javax.xml.xpath.XPath;

public class GamePageFragment extends YouTubePlayerFragment {

    public static final String YOUTUBE_CRED = "AIzaSyBsgtxTZEPQvf1_M34bJCmwlL1ROcJGu7c";

    private YouTubePlayerView youTubePlayerViewTrial;
    private YouTubePlayerView youTubePlayerViewGamePlay;
    private YouTubePlayer.OnInitializedListener onInitializedListenerTrial;
    private YouTubePlayer.OnInitializedListener onInitializedListenerGamePlay;
    private Button btnPlayTrial;
    private Button btnPlayGamePlay;
    private Game mGame;
    private TextView description;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ps4, container, false);
        youTubePlayerViewTrial = view.findViewById(R.id.trial_youtube);
        youTubePlayerViewGamePlay = view.findViewById(R.id.gameplay_youtube);
        btnPlayTrial = view.findViewById(R.id.play_trial_btn);
        btnPlayGamePlay = view.findViewById(R.id.play_gameplay_btn);
        description = view.findViewById(R.id.game_description);

        //============ get game from other activity\fragment ================
        Bundle bundle = this.getArguments();
        mGame = (Game) bundle.getSerializable("game");
        assert mGame != null;
        description.setText(mGame.getmDescription());

        //====================================================================

        onInitializedListenerTrial = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(mGame.getmTrailer());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(view.getContext(), "Video Failed To Load", Toast.LENGTH_LONG);
            }
        };

        onInitializedListenerGamePlay = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadPlaylist(mGame.getmGamePlay());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(view.getContext(), "Video Failed To Load", Toast.LENGTH_LONG);
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

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
