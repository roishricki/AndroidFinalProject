package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.CUR_RECIPES;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.Key.USERS_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.UUID;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public boolean isLoggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        if(USER_ACCESS_TOKEN.TOKEN==null ){
//            Bundle extras = getIntent().getExtras();
//            if(extras != null){
//                USER_ACCESS_TOKEN.TOKEN = extras.getString("USER_TOKEN");
//                CUR_RECIPES.ID = extras.getString("recipe_id");
//            }
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean validateEmail(String email) {
        boolean isValid = Pattern.compile("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+[.]+[a-zA-Z].+[a-zA-Z]$").matcher(email).matches();
        return isValid;
    }

    public void registerFunc(View view, String email, String password, UserDetails userDetails) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                            String token = UUID.randomUUID().toString();
                            userDetails.setToken(token);
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference USERS_Reference = firebaseDatabase.getReference(SCHEMA.USERS);
                            USERS_Reference.child(email.replace(".com", "")).setValue(userDetails);
                            DatabaseReference RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES);
                            RECIPES_Reference.child(token).child(RECIPES_KEY.COUNT).setValue("0");
                            DatabaseReference INGREDIENT_Reference = firebaseDatabase.getReference(SCHEMA.INGREDIENT);
                            INGREDIENT_Reference.child(token).child(INGREDIENT_KEY.COUNT).setValue("0");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"register not succeed",Toast.LENGTH_LONG).show();
                            System.out.println(task.getException());
                        }
                    }
                });
    }

    public void loginFunc(View view, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isLoggedin = true;
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this,"LOGGED IN!",Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homePageApp);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"User not found!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(SCHEMA.USERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child(email.replace(".com", "")).child(USERS_KEY.TOKEN).getValue(String.class);
                USER_ACCESS_TOKEN.TOKEN = token;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed To Set Token");
            }
        });
    }

}