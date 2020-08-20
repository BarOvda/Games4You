package com.example.games4you.logic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.GamePageFragment;
import com.example.games4you.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> implements Filterable {

   private Context mContext;
    private List<Game> mGames;
    private List<Game> mGamesFull;
    FragmentManager manager;

    public GameAdapter(Context mContext, List<Game> mGames,FragmentManager manager) {
        this.mContext = mContext;
        this.mGames = mGames;
        this.mGamesFull = new ArrayList<>(mGames);
        this.manager = manager;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.game_item,parent,false);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GameViewHolder holder, final int position) {
        Game gameCurrent =mGames.get(position);
        holder.textViewName.setText((gameCurrent.getName()));
        Picasso.get().load(gameCurrent.getImageUrl())
        .fit()
        .centerCrop()
        .into(holder.imageView);


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("game",mGames.get(position));
                bundle.putString("console",mGames.get(position).getmConsole());
                GamePageFragment gamePageActivity = new GamePageFragment();
                gamePageActivity.setArguments(bundle);
                manager.beginTransaction().replace(R.id.fragment_container,gamePageActivity).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }
    public void filterByCategories(List<String> categories){
        if(categories.isEmpty()) {
            mGames.clear();
            mGames.addAll(mGamesFull);
            notifyDataSetChanged();
        }
        for(String category:categories){
            getFilter().filter("category:"+category);
        }
    }
    @Override
    public Filter getFilter() {
        return gamesFilter;
    }

    private Filter gamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<Game> filteredGames = new ArrayList<>();
           boolean categoriesFlag = false;
            if(constraint.length()>8&&constraint.toString().contains("category:")){
                constraint = constraint.subSequence(9,constraint.length());
                categoriesFlag = true;
            }
           if(constraint == null || constraint.length()==0){
                filteredGames.addAll(mGamesFull);
           }else{

               String filterPattern = constraint.toString().toLowerCase().trim();
               for(Game game:mGamesFull){
                   if(categoriesFlag==false){
                   if(game.getName().toLowerCase().contains(filterPattern)){
                       filteredGames.add(game);
                   }
                   }else{
                       for(Categories category:game.getmCategories()){
                           if(category.toString().toLowerCase().equals(filterPattern)){
                               filteredGames.add(game);
                           }
                       }

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
            public CardView parentLayout;

            public GameViewHolder(View itemView){
                super(itemView);
                textViewName= itemView.findViewById(R.id.game_view_name);
                imageView= itemView.findViewById(R.id.game_view_image);
                parentLayout = itemView.findViewById(R.id.game_item_parent_layout);
        }
    }


}
