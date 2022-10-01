package com.example.recipesapplication.Service.Models;

import java.util.ArrayList;

public class ReguolarRecipes extends Recipes {

    private String description;
    private String id;
    private ArrayList<IngredientForRecipe> ingredientForRecipeArrayList;

    public ReguolarRecipes() {
        super();
    }

    public ReguolarRecipes(String name, String id){
        super(name,"regular");
        this.id = id;
    }

    public ReguolarRecipes(String name, String description, ArrayList<IngredientForRecipe> ingredientForRecipeArrayList) {
        super(name, "regular");
        this.description = description;
        this.ingredientForRecipeArrayList = ingredientForRecipeArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<IngredientForRecipe> getIngredientForRecipeArrayList() {
        return ingredientForRecipeArrayList;
    }

    public void setIngredientForRecipeArrayList(ArrayList<IngredientForRecipe> ingredientForRecipeArrayList) {
        this.ingredientForRecipeArrayList = ingredientForRecipeArrayList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
