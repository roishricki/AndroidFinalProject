package com.example.recipesapplication.Service.Models;

public abstract class Recipes {
    private String name;
    private String type;

    public Recipes() {
    }

    public Recipes(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
