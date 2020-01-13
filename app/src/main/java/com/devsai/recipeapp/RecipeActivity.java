package com.devsai.recipeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.viewmodels.RecipeByIdViewModel;

public class RecipeActivity extends BaseActivity {

    private TextView mTitle;
    private TextView mPublisher;
    private TextView mSocialRank;
    private AppCompatImageView mImage;
    private LinearLayout mRecipeIngredients;
    private ScrollView mScrollView;
    private static final String TAG = "RecipeActivity";
    private RecipeByIdViewModel mRecipeByIdViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        showProgressBar(true);
        mTitle = findViewById(R.id.recipe_title);
        mPublisher = findViewById(R.id.recipe_publisher);
        mSocialRank = findViewById(R.id.recipe_socialrank);
        mImage = findViewById(R.id.recipe_image);
        mScrollView = findViewById(R.id.parent);
        mRecipeIngredients = findViewById(R.id.ingredients_container);
        mRecipeByIdViewModel = ViewModelProviders.of(this).get(RecipeByIdViewModel.class);
        getIncomingIntent();
         onSubscribeObservers();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("recipes")) {
            Recipe recipe = getIntent().getParcelableExtra("recipes");
            Log.d(TAG, "getIncomingIntent: "+recipe.getTitle());
            searchRecipeById(recipe.getRecipe_id());




        }
    }
    private void onSubscribeObservers() {
        mRecipeByIdViewModel.getRecipeById().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {

                if(recipe != null) {

                    if(recipe.getRecipe_id().equals(mRecipeByIdViewModel.getRecipeId())) {
                        setRecipeProperties(recipe);
                        mRecipeByIdViewModel.setDidRetrieveRecipe(true);
                    }
                }


            }
        });

        mRecipeByIdViewModel.getRecipeRequestTimeout().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !mRecipeByIdViewModel.getDidRetrieveRecipe()) {
                    Log.d(TAG, "onChanged: timeout...");
                    displayErrorScreen("Error retrieving data, check network connection");

                }
            }
        });
    }

    private void searchRecipeById(String rid) {

        mRecipeByIdViewModel.searchRecipeById(rid);
    }

    private void setRecipeProperties(Recipe recipe) {

        if(recipe != null) {
            Log.d(TAG, "onChanged: observe"+recipe.getTitle());
            mTitle.setText(recipe.getTitle());
            mPublisher.setText(recipe.getPublisher());
            mSocialRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(this).setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url()).into(mImage);


            //clear exisiting data in the linear layout container

            mRecipeIngredients.removeAllViews();

            for(String ingredient : recipe.getIngredients()) {

                TextView textView = new TextView(this);

                textView.setTextSize(15);
                textView.setText(ingredient);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredients.addView(textView);

            }

            showParent();
            showProgressBar(false);

        }

    }
    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }

    private void displayErrorScreen(String errorMessage) {

        showProgressBar(false);

        mTitle.setText("Error retrieving recipe");
        mSocialRank.setText("");
        TextView textView = new TextView(this);
        if(!errorMessage.equals("")) {
            textView.setText(errorMessage);
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecipeIngredients.removeAllViews();
        mRecipeIngredients.addView(textView);

        RequestOptions requestOptions = new RequestOptions();
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(R.drawable.ic_launcher_background)
                .into(mImage);

        showParent();

    }
}
