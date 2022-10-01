package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.Key.USERS_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.Ingredient;
import com.example.recipesapplication.Service.Models.IngredientAdapter;
import com.example.recipesapplication.Service.Models.LinkRecipes;
import com.example.recipesapplication.Service.Models.RecipeLinkAdapter;
import com.example.recipesapplication.Service.Models.Recipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyLinksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecipeLinkAdapter recipeLinkAdapter;
    public ArrayList<LinkRecipes> list = new ArrayList<>();
    private DatabaseReference RECIPES_Reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_links);

        recyclerView = (RecyclerView) findViewById(R.id.mylinks_recycle_view);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if(USER_ACCESS_TOKEN.TOKEN!=null){
            RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
            RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        if(dataSnapshot.getKey() != RECIPES_KEY.COUNT && Objects.equals(dataSnapshot.child(RECIPES_KEY.TYPE).getValue(String.class), "link")) {
                            LinkRecipes linkRecipes = new LinkRecipes(
                                    dataSnapshot.child(RECIPES_KEY.NAME).getValue(String.class),
                                    dataSnapshot.child(RECIPES_KEY.LINK_URL).getValue(String.class));
                            list.add(linkRecipes);
                        }
                    recipeLinkAdapter = new RecipeLinkAdapter(list,MyLinksActivity.this);
                    recyclerView.setAdapter(recipeLinkAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
