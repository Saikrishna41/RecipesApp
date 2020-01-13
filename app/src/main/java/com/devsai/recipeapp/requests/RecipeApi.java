package com.devsai.recipeapp.requests;

import com.devsai.recipeapp.requests.responses.RecipeResponse;
import com.devsai.recipeapp.requests.responses.RecipeSearchResponse;
import com.google.gson.annotations.Expose;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipeId
    );

    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

}
