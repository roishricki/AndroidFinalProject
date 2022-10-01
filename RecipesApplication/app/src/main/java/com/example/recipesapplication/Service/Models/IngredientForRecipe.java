package com.example.recipesapplication.Service.Models;

public class IngredientForRecipe extends Ingredient{

    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public IngredientForRecipe(String name) {
        super(name);
        this.quantity = null;
    }

    public IngredientForRecipe(String name, String quantity) {
        super(name);
        this.quantity = quantity;
    }
}
