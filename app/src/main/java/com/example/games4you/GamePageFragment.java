package com.example.games4you;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameOffer;
import com.example.games4you.logic.GameOfferAdapter;
import com.example.games4you.logic.Review;
import com.example.games4you.logic.ReviewAdapter;
import com.example.games4you.logic.YouTubeAPI.YoutubeSearchAPI;
import com.example.games4you.logic.ebay_plugin.EbayDriver;
import com.example.games4you.logic.ebay_plugin.EbayListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GamePageFragment extends Fragment {

    private WebView mWebViewTrailer;
    private WebView mWebViewGamePlay;

    private IProcess mProcess;
    private IProcess mYouTubeTrailerProcess;
    private IProcess mYouTubeGamePlayProcess;

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
    private  LinearLayoutManager reviewsLinearLayoutManager;
    private RecyclerView reviewRecycle;
    private ReviewAdapter reviewAdapter;
    private List<Review> mReviews;
    private YouTube youtube;
    private List<SearchResult> trailersList;
    FirebaseFirestore db;
    private YoutubeSearchAPI youtubeSearchAPIForGamplay;
    private YoutubeSearchAPI youtubeSearchAPI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page, container, false);

        trailersList = new ArrayList<>();

        mWebViewTrailer=(WebView)view.findViewById(R.id.videoview_trailer);
        mWebViewGamePlay=(WebView)view.findViewById(R.id.videoview_gameplay);
    //    getVideoTrailerFromYouTube();

        //String trailerURL = trailersList.get(0).toString();
        //Log.e("trailer",trailerURL);

        mWebViewTrailer.setWebChromeClient(new ChromeClient());
        mWebViewGamePlay.setWebChromeClient(new ChromeClient());

        btnAddOffer = view.findViewById(R.id.add_offer_button);
        db= FirebaseFirestore.getInstance();

        description = view.findViewById(R.id.game_description);
        name = view.findViewById(R.id.game_name);
        pic = view.findViewById(R.id.game_image);
        addReview = view.findViewById(R.id.add_review_button);
        reviewRecycle = view.findViewById(R.id.review_recyclerview);
        EbayTitleRecyclerView = view.findViewById(R.id.ebay_recyclerview);
        OffersRecyclerView = view.findViewById(R.id.offers_recyclerview);
        EbayTitleRecyclerView.setHasFixedSize(true);
        OffersRecyclerView.setHasFixedSize(true);

        //============ get game from other activity\fragment ================
        mGame = (Game) getArguments().getSerializable("game");
        Log.e("GAME", "name: " + mGame.getName() + " des: " + mGame.getmDescription());
        name.setText(mGame.getName());
        Picasso.get().load(mGame.getImageUrl()).into(pic);
        description.setText(mGame.getmDescription());
        //==================================================================== Ebay
        ebayLinearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        offersLinearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        reviewsLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mProcess = new IProcess() {
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
        EbayListAdapter = new EbayListAdapter(GamePageFragment.this.getContext(),driver.getTitles(),getFragmentManager());
        EbayTitleRecyclerView.setLayoutManager(ebayLinearLayoutManager);
        EbayTitleRecyclerView.setAdapter(EbayListAdapter);


//----------------- YouTube

        mYouTubeTrailerProcess = new IProcess() {
            @Override
            public void updateAdapter() {

                List<String> trailersIDs = youtubeSearchAPI.getVideoId();
                Log.e("VideoId",trailersIDs.get(0));
                String videoStrTrailer = "<html><body>Trailer<br><iframe width=\"300\" height=\"200\" src=\"https://www.youtube.com/embed/"+trailersIDs.get(0)+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                WebSettings ws = mWebViewTrailer.getSettings();
                ws.setJavaScriptEnabled(true);
                mWebViewTrailer.loadData(videoStrTrailer, "text/html", "utf-8");
                mWebViewTrailer.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                mWebViewGamePlay.reload();
            }
        };
         youtubeSearchAPI = new YoutubeSearchAPI(mGame.getName() + " trailer",mYouTubeTrailerProcess);
        youtubeSearchAPI.execute();








        mYouTubeGamePlayProcess = new IProcess() {
            @Override
            public void updateAdapter() {


                List<String> gameplayssIDs = youtubeSearchAPIForGamplay.getVideoId();

                String videoStringGamePlay = "<html><body>Game Play<br><iframe width=\"300\" height=\"200\" src=\"https://www.youtube.com/embed/" + gameplayssIDs.get(0) + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

                mWebViewGamePlay.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                WebSettings ws1 = mWebViewGamePlay.getSettings();
                ws1.setJavaScriptEnabled(true);
                mWebViewGamePlay.loadData(videoStringGamePlay, "text/html", "utf-8");

            }
        };


        youtubeSearchAPIForGamplay = new YoutubeSearchAPI(mGame.getName() + " gameplay",mYouTubeGamePlayProcess);

        youtubeSearchAPIForGamplay.execute();





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
                                    GamePageFragment.this.getContext(),offers,getFragmentManager());
                            OffersRecyclerView.setLayoutManager(offersLinearLayoutManager);
                            OffersRecyclerView.setAdapter(mGameAdapter);
                        }
                    }
                });


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
                manager.beginTransaction().replace(R.id.fragment_container, reviewFragment).commit();
            }
        });


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

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            getActivity().setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getActivity().getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    public interface IProcess {

        void updateAdapter();
    }
}
