package com.example.games4you.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> implements Filterable {

   private Context mContext;
    private List<Game> mGames;
    private List<Game> mGamesFull;

    public GameAdapter(Context mContext, List<Game> mGames) {
        this.mContext = mContext;
        this.mGames = mGames;
        this.mGamesFull = new ArrayList<>(mGames);
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

    @Override
    public Filter getFilter() {
        return gamesFilter;
    }
    private Filter gamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<Game> filteredGames = new ArrayList<>();
           if(constraint == null || constraint.length()==0){
                filteredGames.addAll(mGamesFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for(Game game:mGamesFull){
                   if(game.getName().toLowerCase().contains(filterPattern)){
                       filteredGames.add(game);
                   }
               }
           }
           FilterResults results = new FilterResults();
           results.values = filteredGames;
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mGames.clear();
            mGames.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


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
