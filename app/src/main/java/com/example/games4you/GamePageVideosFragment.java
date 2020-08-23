package com.example.games4you;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.YouTubeAPI.YoutubeSearchAPI;
import com.example.games4you.logic.ebay_plugin.EbayListAdapter;

import java.util.List;


public class GamePageVideosFragment extends Fragment {
    private Game mGame;
    private LinearLayoutManager ebayLinearLayoutManager;
    private GamePageFragment.IProcess mProcess;
    private RecyclerView EbayTitleRecyclerView;
    private GamePageFragment.IProcess mYouTubeTrailerProcess;
    private GamePageFragment.IProcess mYouTubeGamePlayProcess;
    private WebView mWebViewTrailer;
    private WebView mWebViewGamePlay;

    private YoutubeSearchAPI youtubeSearchAPIForGamplay;
    private YoutubeSearchAPI youtubeSearchAPI;
    private EbayListAdapter EbayListAdapter;

/*
    private LinearLayoutManager offersLinearLayoutManager;
*/

    public GamePageVideosFragment(Game mGame) {
        this.mGame = mGame;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page_videos, container, false);


       mWebViewTrailer=(WebView)view.findViewById(R.id.videoview_trailer);
        mWebViewGamePlay=(WebView)view.findViewById(R.id.videoview_gameplay);

        mWebViewTrailer.setWebChromeClient(new ChromeClient());
        mWebViewGamePlay.setWebChromeClient(new ChromeClient());
        mWebViewTrailer.setBackgroundColor(Color.TRANSPARENT);
        mWebViewGamePlay.setBackgroundColor(Color.TRANSPARENT);


        mYouTubeTrailerProcess = new GamePageFragment.IProcess() {
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


        mYouTubeGamePlayProcess = new GamePageFragment.IProcess() {
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





        return view;
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
}