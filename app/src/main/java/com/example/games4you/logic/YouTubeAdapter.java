package com.example.games4you.logic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.R;
import com.example.games4you.UserOfferDisplayFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.YouTubeViewHolder> {

    private Context mContext;
    private List<YouTubeVideo> videos;
    FragmentManager manager;


    public YouTubeAdapter(Context mContext, List<YouTubeVideo> videos, FragmentManager manager) {
        this.videos = new ArrayList<>();
        this.mContext = mContext;
        this.videos = videos;
        this.manager = manager;

    }

    @NonNull
    @Override
    public YouTubeAdapter.YouTubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.you_tube_item, parent, false);
        return new YouTubeAdapter.YouTubeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeViewHolder holder, int position) {
        final YouTubeVideo gameCurrent =videos.get(position);
        holder.mWebView.setBackgroundColor(Color.TRANSPARENT);
        String videoStringGamePlay = "<html><body>Game Play<br><iframe width=\"300\" height=\"200\" src=\"https://www.youtube.com/embed/"
                + gameCurrent.getUrl() + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        holder.mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings ws1 = holder.mWebView.getSettings();
        ws1.setJavaScriptEnabled(true);
        holder.mWebView.loadData(videoStringGamePlay, "text/html", "utf-8");



    }

    @Override
    public int getItemCount() {
        return  videos.size();    }

    public class YouTubeViewHolder extends RecyclerView.ViewHolder {
        public WebView mWebView;
        public CardView parentLayout;

        public YouTubeViewHolder(@NonNull View itemView) {
            super(itemView);
            mWebView = itemView.findViewById(R.id.web_view_youtube);
            parentLayout = itemView.findViewById(R.id.youtube_item_parent_layout);
        }


    }
}
