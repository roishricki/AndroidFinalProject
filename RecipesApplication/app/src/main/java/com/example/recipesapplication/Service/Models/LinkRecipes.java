package com.example.recipesapplication.Service.Models;

public class LinkRecipes extends Recipes{

    private String linkURL;

    public LinkRecipes(String name, String linkURL) {
        super(name,"link");
        this.linkURL = linkURL;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }
}
