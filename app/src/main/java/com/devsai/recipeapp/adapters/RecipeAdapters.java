package com.devsai.recipeapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devsai.recipeapp.R;
import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapters extends RecyclerView.Adapter {

    private final int CATEGORY_TYPE = 3;
    private final int RECIPE_TYPE = 1;
    private final int LOADING_TYPE = 2;
    private final int EXHAUSTED_TYPE = 4;

    private List<Recipe> mRecipes ;

    private RecipeListListener mRecipeListListener;

    public RecipeAdapters(RecipeListListener mRecipeListListener) {
        this.mRecipeListListener = mRecipeListListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case CATEGORY_TYPE:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_category,parent,false);
                return new CategoryViewHolder(view,mRecipeListListener);
            case RECIPE_TYPE:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe,parent,false);
                return new RecipeViewHolder(view,mRecipeListListener);

            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_loading,parent,false);
                return new LoadingviewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted,parent,false);
                return new SearchExhaustedViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe,parent,false);
                return new RecipeViewHolder(view,mRecipeListListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipes.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        }
        else if(mRecipes.get(position).getTitle().equals("LOADING...")) {

           return LOADING_TYPE;
       }

       else if(mRecipes.get(position).getTitle().equals("EXHAUSTED...") ) {
           return EXHAUSTED_TYPE;
       }
        else if(position == mRecipes.size()-1 && position != 0 && !mRecipes.get(position).getTitle().equals("EXHAUSTED...") ) {
            return LOADING_TYPE;
        }

       else {
           return RECIPE_TYPE;
       }
    }
    public void setQueryExhausted() {

        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        mRecipes.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    private void hideLoading() {
        if(isLoading()) {
            for(Recipe recipe : mRecipes) {

                if(recipe.getTitle().equals("LOADING...")) {
                    mRecipes.remove(recipe);
                    notifyDataSetChanged();
                }
            }
        }
    }
    public void displayLoading() {

        if(!isLoading()) {

            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadinglist = new ArrayList<>();
            loadinglist.add(recipe);
            mRecipes = loadinglist;
            notifyDataSetChanged();
        }

    }
    private boolean isLoading() {

       if(mRecipes != null) {
           if(mRecipes.size() > 0) {
               if(mRecipes.get(mRecipes.size()-1).getTitle().equals("LOADING...")) {
                   return true;
               }
           }
       }
       return false;

    }

    public void displaySearchCategory() {

        List<Recipe> categories = new ArrayList<>();

        for(int i = 0; i < Constants.CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();


            recipe.setTitle(Constants.CATEGORIES[i]);
            recipe.setImage_url(Constants.CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }

        mRecipes = categories;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

         if(itemViewType == RECIPE_TYPE) {

            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(((RecipeViewHolder)holder).itemView).setDefaultRequestOptions(requestOptions)
                    .load(mRecipes.get(position).getImage_url()).into(((RecipeViewHolder)holder).mRecipeImage);

            (((RecipeViewHolder)holder).mRecipeTitle).setText(mRecipes.get(position).getTitle());
            (((RecipeViewHolder)holder).mRecipePublisher).setText(mRecipes.get(position).getPublisher());
            (((RecipeViewHolder)holder).mRecipeSocialRank).setText(String.valueOf(Math.round(mRecipes.get(position).getSocial_rank())));

        }
         else if(itemViewType == CATEGORY_TYPE) {

             RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

             Uri path = Uri.parse("android.resource://com.devsai.recipeapp/drawable/"+mRecipes.get(position).getImage_url());

             Glide.with(((CategoryViewHolder)holder).itemView).setDefaultRequestOptions(requestOptions)
                     .load(path).into(((CategoryViewHolder)holder).mCategoryImage);

             ((CategoryViewHolder)holder).mCategoryTitle.setText(mRecipes.get(position).getTitle());
         }

    }

    @Override
    public int getItemCount() {
        if(mRecipes == null) {
            return 0;
        }
        else {
            return mRecipes.size();
        }
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {

        if(mRecipes != null) {
            if(mRecipes.size() > 0) {
                return mRecipes.get(position);
            }
        }
        return null;
    }
}
