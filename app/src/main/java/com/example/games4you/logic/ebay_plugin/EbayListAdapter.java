package com.example.games4you.logic.ebay_plugin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.example.games4you.R;
import com.example.games4you.logic.EbayTitle;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EbayListAdapter extends RecyclerView.Adapter<EbayListAdapter.GameViewHolder> {
    private Context mContext;
    private List<EbayTitle> mGames;
    FragmentManager manager;


    public EbayListAdapter(Context mContext, List<EbayTitle> mGames, FragmentManager manager) {
        this.mGames = new ArrayList<>();
        this.mContext = mContext;
        this.mGames = mGames;
        this.manager = manager;

    }

    @NonNull
    @Override
    public EbayListAdapter.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ebay_game_item, parent, false);
        return new EbayListAdapter.GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EbayListAdapter.GameViewHolder holder, final int position) {
        final EbayTitle gameCurrent =mGames.get(position);
        holder.textViewName.setText((gameCurrent.getTitle()));
        Picasso.get().load(gameCurrent.getGalleryUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.priceView.setText(gameCurrent.getCurrentPrice() + "$");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gameCurrent.getItemUrl()));
                mContext.startActivity(browserIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
return  mGames.size();    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public TextView priceView;
        public CardView parentLayout;

        public GameViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.game_view_name_ebay);
            imageView = itemView.findViewById(R.id.game_view_image_ebay);
            priceView = itemView.findViewById(R.id.game_view_price_ebay);
            parentLayout = itemView.findViewById(R.id.game_item_parent_layout_ebay);
        }


    }
      /*  @NonNull
    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.game_item,parent,false);
        return new GameAdapter.GameViewHolder(v);
    }*/
}