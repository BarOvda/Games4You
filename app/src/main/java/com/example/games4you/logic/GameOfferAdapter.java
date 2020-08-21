package com.example.games4you.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.R;
import com.example.games4you.UserOfferDisplayFragment;
import com.example.games4you.logic.ebay_plugin.EbayListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameOfferAdapter extends RecyclerView.Adapter<GameOfferAdapter.GameOfferViewHolder> {

    private Context mContext;
    private List<GameOffer> mGames;
    FragmentManager manager;


    public GameOfferAdapter(Context mContext, List<GameOffer> mGames, FragmentManager manager) {
        this.mGames = new ArrayList<>();
        this.mContext = mContext;
        this.mGames = mGames;
        this.manager = manager;

    }

    @NonNull
    @Override
    public GameOfferAdapter.GameOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.game_offer_item, parent, false);
        return new GameOfferAdapter.GameOfferViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull GameOfferViewHolder holder, int position) {
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

    public class GameOfferViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public TextView priceView;
        public CardView parentLayout;

        public GameOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_view_name_offer);
            imageView = itemView.findViewById(R.id.game_view_image_offer);
            priceView = itemView.findViewById(R.id.game_view_price_offer);
            parentLayout = itemView.findViewById(R.id.game_item_parent_layout_offer);
        }


    }
}
