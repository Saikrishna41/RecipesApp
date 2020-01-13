package com.devsai.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.devsai.recipeapp.adapters.RecipeAdapters;
import com.devsai.recipeapp.adapters.RecipeListListener;
import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.requests.RecipeApi;
import com.devsai.recipeapp.requests.ServiceGenerator;
import com.devsai.recipeapp.requests.responses.RecipeSearchResponse;
import com.devsai.recipeapp.utils.VerticalItemSpaceDecorator;
import com.devsai.recipeapp.viewmodels.RecipeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devsai.recipeapp.utils.Constants.API_KEY;

public  class MainActivity extends BaseActivity implements RecipeListListener {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private RecipeAdapters mAdapters;
    private List<Recipe> mRecipes;
    private SearchView mSearch;

    private RecipeViewModel mRecipeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecyclerView = findViewById(R.id.recyclerview);
        onSubscribersObserved();
        initRecyclerView();
        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle("RecipeList");
        mSearch = findViewById(R.id.search);
        initSearch();

        if(!mRecipeViewModel.getisViewingRecipe()) {
            displaySearchCategories();

        }
    }

    private void searchRecipe(String query, int page) {

            mRecipeViewModel.searchRecipe(query,page);
        Log.d(TAG, "searchRecipe: "+query);

    }

    private void initSearch() {

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapters.displayLoading();
                mRecipeViewModel.searchRecipe(query,1);
                mRecipeViewModel.setisQuerying(true);
                mSearch.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private void onSubscribersObserved() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null) {
                    if(mRecipeViewModel.getisViewingRecipe()) {

                        mRecipeViewModel.setisQuerying(false);
                        mAdapters.setRecipes(recipes);

                    }

                }
            }
        });
        
        mRecipeViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    Log.d(TAG, "onChanged: query is exhausted");
                    mAdapters.setQueryExhausted();
                }
            }
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapters = new RecipeAdapters(this);

        VerticalItemSpaceDecorator verticalItemSpaceDecorator = new VerticalItemSpaceDecorator(15);

        mRecyclerView.addItemDecoration(verticalItemSpaceDecorator);

        mRecyclerView.setAdapter(mAdapters);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)) {

                    mRecipeViewModel.searchNextPage();
                }
            }
        });
    }
    public void displaySearchCategories() {
        mRecipeViewModel.setIsViewing(false);
        mAdapters.displaySearchCategory();
    }

    @Override
    public void onRecipeItemCLicked(int position) {


        Log.d(TAG, "onRecipeItemCLicked: ");

        Intent intent = new Intent(this,RecipeActivity.class);

        intent.putExtra("recipes",mAdapters.getSelectedRecipe(position));

        startActivity(intent);

    }

    @Override
    public void categoryClicked(String category) {
        mAdapters.displayLoading();
        searchRecipe(category,1);
        Log.d(TAG, "categoryClicked: "+category);
        mSearch.clearFocus();

    }

    @Override
    public void onBackPressed() {

        if(mRecipeViewModel.onBackPressed()) {
            super.onBackPressed();
        }
        else {
            displaySearchCategories();
        }
    }


}
