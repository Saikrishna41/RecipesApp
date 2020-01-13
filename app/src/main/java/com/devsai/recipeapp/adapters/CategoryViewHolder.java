package com.devsai.recipeapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.devsai.recipeapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

     TextView mCategoryTitle;
     CircleImageView mCategoryImage;
     RecipeListListener mRecipeListListener;

    public CategoryViewHolder(@NonNull View itemView, RecipeListListener recipeListListener)  {
        super(itemView);
        mCategoryTitle = itemView.findViewById(R.id.categoryTitle);
        mCategoryImage = itemView.findViewById(R.id.category_image);
        this.mRecipeListListener = recipeListListener;

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        mRecipeListListener.categoryClicked(mCategoryTitle.getText().toString());
    }
}
