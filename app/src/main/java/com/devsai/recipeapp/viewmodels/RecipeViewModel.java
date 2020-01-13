package com.devsai.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private boolean mIsQuerying;

    private boolean isViewingRecipe;

    private RecipeRepository mRepo;

    public RecipeViewModel() {
        mRepo = RecipeRepository.getInstance();
        mIsQuerying = false;
    }

    public void searchRecipe(String query, int page) {

        mRepo.searchRecipe(query, page);
        isViewingRecipe = true;
        mIsQuerying = true;
    }

    public LiveData<List<Recipe>> getRecipe() {
        return mRepo.getRecipe();
    }
    public void setisQuerying(boolean isQuerying) {
        mIsQuerying = isQuerying;
    }
    public boolean getisQuerying() {
        return  mIsQuerying;
    }

    public void setIsViewing(boolean misViewingRecipe) {

        isViewingRecipe = misViewingRecipe;
    }

    public boolean getisViewingRecipe() {
        return isViewingRecipe;
    }

    public void searchNextPage() {

        if(!mIsQuerying && isViewingRecipe && !isQueryExhausted().getValue()) {
            mRepo.searchNextPage();
        }
    }
    public boolean onBackPressed() {
        if(mIsQuerying) {

            mRepo.cancelRequest();
            mIsQuerying = false;
        }
        if(isViewingRecipe) {
            isViewingRecipe = false;
            return false;
        }
        return true;
    }


    public LiveData<Boolean> isQueryExhausted() {
        return mRepo.getIsQueryexhausted();
    }

 }
