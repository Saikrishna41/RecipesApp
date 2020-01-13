package com.devsai.recipeapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.devsai.recipeapp.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public AppCompatImageView mRecipeImage;

    TextView mRecipeTitle;
    TextView mRecipePublisher;
    TextView mRecipeSocialRank;
    RecipeListListener mRecipeListListener;


    public RecipeViewHolder(@NonNull View itemView , RecipeListListener recipeListListener) {

        super(itemView);
        mRecipeImage = itemView.findViewById(R.id.image);
        mRecipeTitle = itemView.findViewById(R.id.title);
        mRecipePublisher = itemView.findViewById(R.id.publisher);
        mRecipeSocialRank = itemView.findViewById(R.id.social_rank);
        this.mRecipeListListener = recipeListListener;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mRecipeListListener.onRecipeItemCLicked(getAdapterPosition());
    }
}
