package com.example.recipesapplication.Service.Models;

public class IMGRecipes extends Recipes{

    private String imgURL;

    public IMGRecipes() {
        super();
    }

    public IMGRecipes(String name) {
        super(name,"img");
    }

    public IMGRecipes(String name, String imgURL) {
        super(name, "img");
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
