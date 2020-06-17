package com.example.games4you.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

   private Context mContext;
    private List<Game> mGames;

    public GameAdapter(Context mContext, List<Game> mGames) {
        this.mContext = mContext;
        this.mGames = mGames;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.game_item,parent,false);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game gameCurrent =mGames.get(position);
        holder.textViewName.setText((gameCurrent.getName()));
        Picasso.get().load(gameCurrent.getImageUrl())
        .fit()
        .centerCrop()
        .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }


    public class GameViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewName;
            public ImageView imageView;

            public GameViewHolder(View itemView){
                super(itemView);
                textViewName= itemView.findViewById(R.id.game_view_name);
                imageView= itemView.findViewById(R.id.game_view_image);
        }
    }


}
