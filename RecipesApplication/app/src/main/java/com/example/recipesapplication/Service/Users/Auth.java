package com.example.recipesapplication.Service.Users;

import androidx.annotation.NonNull;

import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

/**
 * handling user authentication
 */
public class Auth {
    /**
     * this function generating uuid as access token for a user
     * @param email user email
     * @return user uuid
     */
    public String register(String email) {
        String token = this.getUUID();
        FirebaseDatabase.getInstance().getReference().child(SCHEMA.USERS).child(email.replace(".com", "")).setValue(token);
        FirebaseDatabase.getInstance().getReference().child(SCHEMA.INGREDIENT).child(token).setValue(null);
        FirebaseDatabase.getInstance().getReference().child(SCHEMA.RECIPES).child(token).setValue(null);
        return token;
    }

    /**
     * generating a uuid
     * @return uuid as string
     */
    private String getUUID() { return UUID.randomUUID().toString(); }

    /**
     *
     * @param token
     */
    public void setAccessToken(String token) {
        USER_ACCESS_TOKEN.TOKEN = token;
    }
}
