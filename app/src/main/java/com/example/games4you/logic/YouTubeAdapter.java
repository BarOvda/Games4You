package com.example.games4you.logic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
        final GameOffer gameCurrent =mGames.get(position);
        holder.textViewName.setText((gameCurrent.getmTitle()));
        Picasso.get().load(gameCurrent.getmImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.priceView.setText(gameCurrent.getmPrice() + "$");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putSerializable("game",gameCurrent);
                UserOfferDisplayFragment fragment = new UserOfferDisplayFragment();
                fragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.fragment_container,fragment)
                        .addToBackStack("offer_page").commit();
            }
        });
    }




    @Override
    public int getItemCount() {
        return  mGames.size();    }

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
