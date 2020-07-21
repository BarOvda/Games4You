package com.example.games4you;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.games4you.logic.Game;
import com.squareup.picasso.Picasso;

public class GamePageActivity extends Fragment {

    private Button btnPlayTrial;
    private Button btnPlayGamePlay;
    private Game mGame;
    private TextView description;
    private TextView name;
    private ImageView pic;
    private TextView addReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_game_page, container, false);

        btnPlayTrial = view.findViewById(R.id.play_trial_btn);
        btnPlayGamePlay = view.findViewById(R.id.play_gameplay_btn);
        description = view.findViewById(R.id.game_description);
        name = view.findViewById(R.id.game_name);
        pic = view.findViewById(R.id.game_image);
        addReview = view.findViewById(R.id.add_review);

        //============ get game from other activity\fragment ================
        mGame = (Game) getArguments().getSerializable("game");
        Log.e("GAME", "name: " + mGame.getName() + " des: " + mGame.getmDescription());
        name.setText(mGame.getName());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        description.setText(mGame.getmDescription());
        //====================================================================

        btnPlayTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                TrailerFragment trailerFragment = new TrailerFragment();
                trailerFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, trailerFragment).commit();
            }
        });

        btnPlayGamePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                GamePlayFragment gamePlayFragment = new GamePlayFragment();
                gamePlayFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, gamePlayFragment).commit();
            }
        });

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, reviewFragment).commit();
            }
        });

        return view;
    }
}
