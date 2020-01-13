package com.devsai.recipeapp.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devsai.recipeapp.AppExecutors;
import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.requests.responses.RecipeResponse;
import com.devsai.recipeapp.requests.responses.RecipeSearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.devsai.recipeapp.utils.Constants.API_KEY;
import static com.devsai.recipeapp.utils.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private MutableLiveData<List<Recipe>> mRecipes;

    private MutableLiveData<Recipe> mRecipe;

    private retrieveRunnable mRetrieveRunnable;
    private retrieveRunnableRecipe mRetrieveRunnableSingle;

    private MutableLiveData<Boolean> mRecipeRequestTimeout;


    private static RecipeApiClient instance;

    private RecipeApiClient() {

        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
        mRecipeRequestTimeout = new MutableLiveData<>();
    }

    public static RecipeApiClient getInstance() {
        if(instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }


    public void searchRecipeApi(String query, int page) {

        if(mRetrieveRunnable != null) {
            mRetrieveRunnable = null;
        }
        mRetrieveRunnable = new retrieveRunnable(query, page);
        final Future handler = AppExecutors.getInstance().getmNetworkIO().submit(mRetrieveRunnable);

        AppExecutors.getInstance().getmNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        },NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);

    }

    public void searchRecipeById(String rid) {
        if(mRetrieveRunnableSingle != null) {
            mRetrieveRunnableSingle = null;
        }
        mRetrieveRunnableSingle = new retrieveRunnableRecipe(rid);

        final Future handler = AppExecutors.getInstance().getmNetworkIO().submit(mRetrieveRunnableSingle);
        mRecipeRequestTimeout.postValue(false);
        AppExecutors.getInstance().getmNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the  user know its time out
                mRecipeRequestTimeout.postValue(true);
                handler.cancel(true);
            }
        },NETWORK_TIMEOUT,TimeUnit.MILLISECONDS);
    }

    private class retrieveRunnableRecipe implements Runnable {

        private String rid;
        boolean isCancelled;

        public retrieveRunnableRecipe(String recipeId) {
            this.rid = recipeId;
            isCancelled = false;
        }

        @Override
        public void run() {
            try {

                Response response = searchSingleRecipe(rid).execute();

                if(response.code() == 200) {
                    Recipe recipe = (((RecipeResponse)response.body()).getRecipe());
                    Log.d(TAG, "run: recipe by id"+recipe.getTitle());
                    mRecipe.postValue(recipe);

                }
                else {
                    mRecipe.postValue(null);
                    Log.d(TAG, "run: null");
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        private Call<RecipeResponse> searchSingleRecipe(String rid) {


            return ServiceGenerator.getRecipeApi().getRecipe(API_KEY,rid);
        }

        private void cancelRequest() {
           isCancelled = true;
        }
    }
    private class retrieveRunnable implements Runnable {

        private String mquery;
        private int page;
        boolean isCancelled;

        public retrieveRunnable(String query, int page) {
            this.mquery = query;
            this.page = page;
            isCancelled = false;
        }

        @Override
        public void run() {
            try {

                Response response = searchRecipe(mquery,page).execute();

                if(response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if(page == 1) {
                        mRecipes.postValue(list);

                    }
                    else {
                        List<Recipe> curList = mRecipes.getValue();
                        curList.addAll(list);
                        mRecipes.postValue(curList);
                    }

                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        private Call<RecipeSearchResponse> searchRecipe(String query, int page) {


            return ServiceGenerator.getRecipeApi().searchRecipe(API_KEY,query, String.valueOf(page));
        }

        private void cancelRequest() {
            isCancelled = true;
        }
    }


    public LiveData<List<Recipe>> getRecipe() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipeSingle() {
        return mRecipe;
    }
    public void cancelRequest() {
        if(mRetrieveRunnable != null) {
            mRetrieveRunnable.cancelRequest();
        }
        if(mRetrieveRunnableSingle != null) {
            mRetrieveRunnableSingle.cancelRequest();
        }

    }

    public LiveData<Boolean> getRecipeRequestTimeout() {

        return mRecipeRequestTimeout;
    }

}
