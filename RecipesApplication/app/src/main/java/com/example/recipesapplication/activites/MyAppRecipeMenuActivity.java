package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.CUR_RECIPES;
import com.example.recipesapplication.Service.Constant.FILTERED_RECIPES;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.IMGRecipes;
import com.example.recipesapplication.Service.Models.LinkRecipes;
import com.example.recipesapplication.Service.Models.MyRecipeAppAdapter;
import com.example.recipesapplication.Service.Models.RecipeImgAdapter;
import com.example.recipesapplication.Service.Models.RecipeLinkAdapter;
import com.example.recipesapplication.Service.Models.Recipes;
import com.example.recipesapplication.Service.Models.ReguolarRecipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyAppRecipeMenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MyRecipeAppAdapter myRecipeAppAdapter;
    public ArrayList<ReguolarRecipes> list = new ArrayList<>();
    private DatabaseReference RECIPES_Reference;
    private Button filterBth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app_recipe_menu);
        recyclerView = (RecyclerView) findViewById(R.id.myrecipeapp_recycle_view);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        filterBth = (Button)findViewById(R.id.filterButton);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if(USER_ACCESS_TOKEN.TOKEN!=null){
            RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
            RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        if(dataSnapshot.getKey() != RECIPES_KEY.COUNT && Objects.equals(dataSnapshot.child(RECIPES_KEY.TYPE).getValue(String.class), "regular")) {
                            ReguolarRecipes reguolarRecipes = new ReguolarRecipes(
                                    dataSnapshot.child(RECIPES_KEY.NAME).getValue(String.class),
                                    dataSnapshot.getKey());
                            list.add(reguolarRecipes);
                        }

                    if(FILTERED_RECIPES.FLAG){
                        myRecipeAppAdapter = new MyRecipeAppAdapter(FILTERED_RECIPES.RECIPES,MyAppRecipeMenuActivity.this);
                        FILTERED_RECIPES.FLAG=false;
                    }else {
                        myRecipeAppAdapter = new MyRecipeAppAdapter(list,MyAppRecipeMenuActivity.this);
                    }
                    recyclerView.setAdapter(myRecipeAppAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        filterBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAppRecipeMenuActivity.this, FilterByIngredientsActivity.class);
                startActivity(intent);
            }
        });

    }
}