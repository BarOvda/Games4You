package com.example.games4you;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.games4you.logic.YouTubeAdapter;
import com.example.games4you.logic.YouTubeVideo;
import com.example.games4you.logic.ebay_plugin.EbayListAdapter;

import java.util.ArrayList;
import java.util.List;


public class GamePageVideosFragment extends Fragment {
    private Game mGame;
    private LinearLayoutManager ebayLinearLayoutManager;
    private GamePageFragment.IProcess mProcess;
    private RecyclerView GamePlayRecyclerView;
    private GamePageFragment.IProcess mYouTubeTrailerProcess;
    private GamePageFragment.IProcess mYouTubeGamePlayProcess;
    private WebView mWebViewTrailer;
    private List<YouTubeVideo> videos;
    private YoutubeSearchAPI youtubeSearchAPIForGamplay;
    private YoutubeSearchAPI youtubeSearchAPI;
    private EbayListAdapter GamePlayListAdapter;

/*
    private LinearLayoutManager offersLinearLayoutManager;
*/

    public GamePageVideosFragment(Game mGame) {
        this.mGame = mGame;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page_videos, container, false);
        videos = new ArrayList<>();

       mWebViewTrailer=(WebView)view.findViewById(R.id.videoview_trailer);
        GamePlayRecyclerView=view.findViewById(R.id.videos_recyclerview);

        mWebViewTrailer.setWebChromeClient(new ChromeClient());
        //mWebViewGamePlay.setWebChromeClient(new ChromeClient());
        mWebViewTrailer.setBackgroundColor(Color.TRANSPARENT);
        //mWebViewGamePlay.setBackgroundColor(Color.TRANSPARENT);

        final YouTubeAdapter adapter= new YouTubeAdapter(this.getContext(),videos,getFragmentManager());
        GamePlayRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        GamePlayRecyclerView.setAdapter(adapter);
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
            }
        };
        youtubeSearchAPI = new YoutubeSearchAPI(mGame.getName() + " trailer",mYouTubeTrailerProcess,1l);
        youtubeSearchAPI.execute();


        mYouTubeGamePlayProcess = new GamePageFragment.IProcess() {
            @Override
            public void updateAdapter() {
                List<String> gameplayssIDs= new ArrayList<>();
                 gameplayssIDs.addAll(youtubeSearchAPIForGamplay.getVideoId());
                for (String gamePlayUrl:gameplayssIDs
                     ) {
                    videos.add(new YouTubeVideo(gamePlayUrl));
                }
                adapter.notifyDataSetChanged();
            }
        };

        youtubeSearchAPIForGamplay = new YoutubeSearchAPI(mGame.getName() + " gameplay",mYouTubeGamePlayProcess,3l);

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