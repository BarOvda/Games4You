package com.example.games4you;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.example.games4you.logic.GameOffer;
import com.example.games4you.logic.GameOfferAdapter;
import com.example.games4you.logic.ebay_plugin.EbayDriver;
import com.example.games4you.logic.ebay_plugin.EbayListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GamePageActivity extends Fragment {

    private WebView mWebViewTrailer;
    private WebView mWebViewGamePlay;


    private Button btnAddOffer;



    private Game mGame;
    private TextView description;
    private TextView name;
    private ImageView pic;
    private TextView addReview;
    private RecyclerView EbayTitleRecyclerView;
    private RecyclerView OffersRecyclerView;

    private EbayListAdapter EbayListAdapter;
    private EbayDriver driver;
    private  LinearLayoutManager ebayLinearLayoutManager;
    private  LinearLayoutManager offersLinearLayoutManager;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_game_page, container, false);

        mWebViewTrailer=(WebView)view.findViewById(R.id.videoview_trailer);
        mWebViewGamePlay=(WebView)view.findViewById(R.id.videoview_gameplay);
        String videoStrTrailer = "<html><body>Trailer<br><iframe width=\"400\" height=\"200\" src=\"https://www.youtube.com/embed/47yJ2XCRLZs\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        mWebViewTrailer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings ws = mWebViewTrailer.getSettings();
        ws.setJavaScriptEnabled(true);
        mWebViewTrailer.loadData(videoStrTrailer, "text/html", "utf-8");


        String videoStringGamePlay = "<html><body>Game Play<br><iframe width=\"400\" height=\"200\" src=\"https://www.youtube.com/embed/47yJ2XCRLZs\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        mWebViewGamePlay.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings ws1 = mWebViewGamePlay.getSettings();
        ws1.setJavaScriptEnabled(true);
        mWebViewGamePlay.loadData(videoStringGamePlay, "text/html", "utf-8");





        btnAddOffer = view.findViewById(R.id.add_offer_button);
        db= FirebaseFirestore.getInstance();

        description = view.findViewById(R.id.game_description);
        name = view.findViewById(R.id.game_name);
        pic = view.findViewById(R.id.game_image);
//        addReview = view.findViewById(R.id.add_review);
        EbayTitleRecyclerView =  view.findViewById(R.id.ebay_recyclerview);
        OffersRecyclerView =   view.findViewById(R.id.offers_recyclerview);
        EbayTitleRecyclerView.setHasFixedSize(true);
        OffersRecyclerView.setHasFixedSize(true);

        //============ get game from other activity\fragment ================
        mGame = (Game) getArguments().getSerializable("game");
        Log.e("GAME", "name: " + mGame.getName() + " des: " + mGame.getmDescription());
        name.setText(mGame.getName());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        description.setText(mGame.getmDescription());
        //====================================================================



         ebayLinearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        offersLinearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        //ebay plugin Test
        driver = new EbayDriver();
        String tag = mGame.getName();
        try {
            driver.runDriver(java.net.URLEncoder.encode(tag, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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



        btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                UserOfferForGameFragment userOfferForGameFragment = new UserOfferForGameFragment();
                userOfferForGameFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,userOfferForGameFragment).commit();
            }
        });

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
                                /*offer.setmDescription((String)document.get("mDescription"));
                                offer.setmImageUrl((String)document.get("mImageUrl"));
                             //   offer.setmPrice(new Float((String) document.get("mPrice")));
                                offer.setmTitle((String)document.get("mTitle"));
*/
                                offers.add(offer);

                            }
                            GameOfferAdapter mGameAdapter = new GameOfferAdapter(
                                    GamePageActivity.this.getContext(),offers,getFragmentManager());
                            OffersRecyclerView.setLayoutManager(offersLinearLayoutManager);
                            OffersRecyclerView.setAdapter(mGameAdapter);
                        }
                    }
                });



     /*   addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", mGame);
                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, reviewFragment).commit();
            }
        });*/
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                    return true;
                }
                return false;
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        while(driver.getTitles().size()<1);

        EbayListAdapter = new EbayListAdapter(GamePageActivity.this.getContext(),driver.getTitles(),getFragmentManager());
        EbayTitleRecyclerView.setLayoutManager(ebayLinearLayoutManager);
        EbayTitleRecyclerView.setAdapter(EbayListAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();

        }

}
