package com.example.games4you;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.games4you.logic.Game;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GamePageFragment extends Fragment implements Toolbar.OnMenuItemClickListener{


    private ImageView scrolledImage;

    private Game mGame;

    private ImageView pic;

    FirebaseFirestore db;

    private View view;

    private Toolbar gamePageToolBar;

    private GamePageVideosFragment gamePageVideosFragment;
    private Game_Page_Information_Fragment game_page_information_fragment;
    private GamePageEbayFragment gamePageEbayFragment;
    private GamePageUsersOffersDisplyFragment gamePageUsersOffersDisplyFragment;
    private GamePageReviewDisplayFragment gamePageReviewDisplayFragment;
    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_game_page, container, false);


        pic = view.findViewById(R.id.game_image_menu);
        scrolledImage = view.findViewById(R.id.profile_avatar);

        gamePageToolBar = view.findViewById(R.id.htab_toolbar);

        gamePageToolBar.getMenu().clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        gamePageToolBar.inflateMenu(R.menu.game_page_menu);
        gamePageToolBar.setOnMenuItemClickListener(this);


        db= FirebaseFirestore.getInstance();
        //============ get game from other activity\fragment ================
        mGame = (Game) getArguments().getSerializable("game");


        //=========Fragments creation=================
        game_page_information_fragment = new Game_Page_Information_Fragment(mGame);
        gamePageVideosFragment = new GamePageVideosFragment(mGame);
        gamePageEbayFragment = new GamePageEbayFragment(mGame);
        gamePageUsersOffersDisplyFragment = new GamePageUsersOffersDisplyFragment(mGame);
        gamePageReviewDisplayFragment = new GamePageReviewDisplayFragment(mGame);

        //=====================================
        Log.e("GAME", "name: " + mGame.getName() + " des: " + mGame.getmDescription());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        Picasso.get().load(mGame.getImageUrl()).into(scrolledImage);



        if (savedInstanceState == null) {
            Drawable xboxIcon = ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_xbox_icon_png);
            Drawable psIcon = ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ps4_icon);
           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_game_page,
                    game_page_information_fragment).commit();

            if(mGame.getmConsole().equals("ps4_games")){
            gamePageToolBar.getMenu().getItem(5).setIcon(psIcon);}
            else{
                gamePageToolBar.getMenu().getItem(5).setIcon(xboxIcon);}

            gamePageToolBar.getMenu().getItem(4).getIcon().setColorFilter(Color.rgb(255,51,51), PorterDuff.Mode.MULTIPLY);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();

    }


    public interface IProcess {

        void updateAdapter();
    }
/*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        gamePageToolBar.getMenu().getItem(3).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }*/

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(!item.getTitle().equals("console_icon")) {
            for (int i = 0; i < gamePageToolBar.getMenu().size(); i++) {
                gamePageToolBar.getMenu().getItem(i).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

            }
            item.getIcon().setColorFilter(Color.rgb(255,51,51), PorterDuff.Mode.MULTIPLY);
        }
        switch (item.getItemId()) {

            case R.id.videos:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_game_page,
                        gamePageVideosFragment).commit();
                return true;
            case R.id.info:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_game_page,
                        game_page_information_fragment).commit();
                return true;

            case R.id.offers:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_game_page,
                        gamePageEbayFragment).commit();
                return true;
            case R.id.users_offers:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_game_page,
                        gamePageUsersOffersDisplyFragment).commit();
                return true;
            case R.id.reviews:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_game_page,
                        gamePageReviewDisplayFragment).commit();

        }
        return false;
    }


}
