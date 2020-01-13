package com.devsai.recipeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {

    private String title;
    private String image_url;
    private String publisher_url;
    private String publisher;
    private String recipe_id;
    private String[] ingredients;
    private double social_rank;

    public Recipe() {
    }

    public Recipe(String title, String image_url, String publisher_url, String publisher, String recipe_id, String[] ingredients, double social_rank) {
        this.title = title;
        this.image_url = image_url;
        this.publisher_url = publisher_url;
        this.publisher = publisher;
        this.recipe_id = recipe_id;
        this.ingredients = ingredients;
        this.social_rank = social_rank;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        image_url = in.readString();
        publisher_url = in.readString();
        publisher = in.readString();
        recipe_id = in.readString();
        ingredients = in.createStringArray();
        social_rank = in.readDouble();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public double getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(double social_rank) {
        this.social_rank = social_rank;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                ", publisher_url='" + publisher_url + '\'' +
                ", publisher='" + publisher + '\'' +
                ", recipe_id='" + recipe_id + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", social_rank=" + social_rank +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image_url);
        dest.writeString(publisher_url);
        dest.writeString(publisher);
        dest.writeString(recipe_id);
        dest.writeStringArray(ingredients);
        dest.writeDouble(social_rank);
    }
}
