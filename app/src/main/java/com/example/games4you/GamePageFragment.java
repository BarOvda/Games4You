package com.example.games4you;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.Review;
import com.example.games4you.logic.ReviewAdapter;
import com.example.games4you.logic.ebay_plugin.EbayDriver;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GamePageFragment extends Fragment implements Toolbar.OnMenuItemClickListener{


    private IProcess mProcess;


    private Button btnAddOffer;

    private ImageView scrolledImage;
    private ImageView menuImage;
    private ImageView videosIcon;
    private ImageView infoIcon;

    private Game mGame;
    private TextView name;
    private ImageView pic;
    private TextView addReview;

    private EbayDriver driver;
    private  LinearLayoutManager ebayLinearLayoutManager;
    private  LinearLayoutManager reviewsLinearLayoutManager;
    private RecyclerView reviewRecycle;
    private ReviewAdapter reviewAdapter;
    private List<Review> mReviews;
    private YouTube youtube;
    private List<SearchResult> trailersList;
    FirebaseFirestore db;

    private View view;

    private Toolbar gamePageToolBar;

    private GamePageVideosFragment gamePageVideosFragment;
    private Game_Page_Information_Fragment game_page_information_fragment;
    private GamePageEbayFragment gamePageEbayFragment;
    private  GamePageUsersOffersDisplyFragment gamePageUsersOffersDisplyFragment;
    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_game_page, container, false);

        trailersList = new ArrayList<>();
/*
    //    getVideoTrailerFromYouTube();

        //String trailerURL = trailersList.get(0).toString();
        //Log.e("trailer",trailerURL);

        btnAddOffer = view.findViewById(R.id.add_offer_button);


        addReview = view.findViewById(R.id.add_review_button);
        reviewRecycle = view.findViewById(R.id.review_recyclerview);

*/
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
            gamePageToolBar.getMenu().getItem(4).setIcon(psIcon);}
            else{
                gamePageToolBar.getMenu().getItem(4).setIcon(xboxIcon);}

            gamePageToolBar.getMenu().getItem(3).getIcon().setColorFilter(Color.rgb(255,51,51), PorterDuff.Mode.MULTIPLY);
        }













        //==================================================================== Ebay
       //----------------- YouTube
/*

*/


//==============================================

/*

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
*/
/*









        //============= Review Recycle View ==========================
        reviewRecycle.setHasFixedSize(true);
        mReviews = new ArrayList<>();
        String consoleType = "";
        String con = mGame.getmConsole();


        db.collection(mGame.getmConsole())
                .document(mGame.getName())
                .collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
//
                                Log.d("REVIEW TEXT", String.format("%s",review.getUser_name()));
                                mReviews.add(review);
                            }
                            reviewAdapter = new ReviewAdapter( GamePageFragment.this.getContext(),mReviews, getFragmentManager());
                            reviewRecycle.setLayoutManager(reviewsLinearLayoutManager);
                            reviewRecycle.setAdapter(reviewAdapter);

                        }
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
                manager.beginTransaction().replace(R.id.fragment_container, reviewFragment)
                        .addToBackStack("add_review").commit();
            }
        });


*/

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

        }
        return false;
    }


}
