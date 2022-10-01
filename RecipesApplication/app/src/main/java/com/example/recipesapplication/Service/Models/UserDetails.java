package com.example.recipesapplication.Service.Models;

public class UserDetails {
    private String Token;
    private String name;
    private String phone;

    public UserDetails(String token, String name, String phone) {
        Token = token;
        this.name = name;
        this.phone = phone;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
