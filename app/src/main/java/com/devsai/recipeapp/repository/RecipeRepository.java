package com.devsai.recipeapp.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.requests.RecipeApi;
import com.devsai.recipeapp.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";
    private static RecipeApiClient mRecipeClient;
    private static RecipeRepository instance;
    private String mQuery;
    private int mPage;

    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();

    private RecipeRepository() {

        mRecipeClient = RecipeApiClient.getInstance();
        initMediator();
    }

    public static RecipeRepository getInstance() {
        if(instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private void initMediator() {

        LiveData<List<Recipe>> recipeApiSource = mRecipeClient.getRecipe();

        mRecipes.addSource(recipeApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null) {
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }
                else {
                    //database cache
                }
            }
        });
    }

    public void doneQuery(List<Recipe> list) {
        if(list != null) {
            if(list.size() < 30) {
                mIsQueryExhausted.setValue(true);
            }
        }
        else {
            mIsQueryExhausted.setValue(true);
        }


    }

public LiveData<Boolean> getIsQueryexhausted() {
        return mIsQueryExhausted;
}


    public void searchRecipe(String query, int page) {
        if(page == 0) {
            page = 1;
        }
        mQuery = query;
        mPage = page;
        mRecipeClient.searchRecipeApi(mQuery, mPage);
        mIsQueryExhausted.setValue(false);

    }
    public void searchRecipeById(String rid){
        mRecipeClient.searchRecipeById(rid);
    }
    public LiveData<List<Recipe>> getRecipe() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipeById() {
        return mRecipeClient.getRecipeSingle();
    }

    public void searchNextPage() {

        mRecipeClient.searchRecipeApi(mQuery,mPage+1);
    }


    public void cancelRequest() {
        mRecipeClient.cancelRequest();
    }

    public LiveData<Boolean> getRecipeRequestTimeout() {
        return mRecipeClient.getRecipeRequestTimeout();
    }

}
