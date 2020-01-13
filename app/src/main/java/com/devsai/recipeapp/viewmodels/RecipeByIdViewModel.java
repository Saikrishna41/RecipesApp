package com.devsai.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.repository.RecipeRepository;

public class RecipeByIdViewModel extends ViewModel {


    private RecipeRepository mRepo;

    private String mRecipeId;

    private Boolean mDidRetrieveRecipe;


    public String getRecipeId() {
        return mRecipeId;
    }

    public RecipeByIdViewModel() {

        mRepo = RecipeRepository.getInstance();

        mDidRetrieveRecipe = false;

    }

    public void searchRecipeById(String rid) {
        mRecipeId = rid;
        mRepo.searchRecipeById(rid);
    }

    public LiveData<Recipe> getRecipeById() {

        return mRepo.getRecipeById();
    }

    public LiveData<Boolean> getRecipeRequestTimeout() {
        return mRepo.getRecipeRequestTimeout();
    }

    public Boolean getDidRetrieveRecipe() {
        return mDidRetrieveRecipe;
    }

    public void setDidRetrieveRecipe(Boolean DidRetrieveRecipe) {
        this.mDidRetrieveRecipe = DidRetrieveRecipe;
    }
}
