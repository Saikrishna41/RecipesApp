package com.devsai.recipeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devsai.recipeapp.models.Recipe;
import com.devsai.recipeapp.requests.RecipeApi;
import com.devsai.recipeapp.requests.ServiceGenerator;
import com.devsai.recipeapp.requests.responses.RecipeResponse;
import com.devsai.recipeapp.requests.responses.RecipeSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devsai.recipeapp.utils.Constants.API_KEY;

public  class Testing extends AppCompatActivity {

    private static final String TAG = "Testing";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_btn);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                testRetrofit();
            }
        });
    }

    private void testRetrofit() {

        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        Call<RecipeResponse> responseCall = recipeApi.getRecipe(
                API_KEY,"35382"
        );

        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if(response.code() == 200) {

                   Recipe recipe = response.body().getRecipe();
                   if(recipe != null) {

                       Log.d(TAG, "onResponse: "+recipe.getTitle());
                   }
                   else {
                       Log.d(TAG, "onResponse: null"+response.body().toString());
                   }
                }
                else {
                    Log.d(TAG, "onResponse: response code not 200");
                }

            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });
    }
}
