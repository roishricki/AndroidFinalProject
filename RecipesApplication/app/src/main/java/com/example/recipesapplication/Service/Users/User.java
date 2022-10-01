package com.example.recipesapplication.Service.Users;

import com.example.recipesapplication.Service.Models.Ingredient;
import com.example.recipesapplication.Service.Models.Recipes;

import java.util.List;

public class User {

    private String name;
    private String email;
    private String phone;
    private List<Recipes> recipes;
    private List<Ingredient> ingredients;

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public User(String name, String email, String phone, List<Recipes> recipes, List<Ingredient> ingredients) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.recipes = recipes;
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Recipes> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
    }
}
