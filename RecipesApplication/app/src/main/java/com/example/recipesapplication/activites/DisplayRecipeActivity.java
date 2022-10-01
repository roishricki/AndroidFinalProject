package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.CUR_RECIPES;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.DisplayRecipeAdapter;
import com.example.recipesapplication.Service.Models.IMGRecipes;
import com.example.recipesapplication.Service.Models.IngredientForRecipe;
import com.example.recipesapplication.Service.Models.LinkRecipes;
import com.example.recipesapplication.Service.Models.RecipeLinkAdapter;
import com.example.recipesapplication.Service.Models.ReguolarRecipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DisplayRecipeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DisplayRecipeAdapter displayRecipeAdapter;
    public ReguolarRecipes recipesToDisplay;
    private DatabaseReference RECIPES_Reference;
    private TextView textViewRecipeName;
    private TextView textViewRecipeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        recyclerView = (RecyclerView) findViewById(R.id.description_recycleview);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textViewRecipeName =(TextView)findViewById(R.id.textViewRecipeName);
        textViewRecipeDescription = (TextView)findViewById(R.id.textViewDescription);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (USER_ACCESS_TOKEN.TOKEN != null) {
            RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN).child(CUR_RECIPES.ID);
            RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot OverallSnapshot) {
                    //snapshot.getValue(ReguolarRecipes.class);
                    OverallSnapshot.child(INGREDIENT_KEY.INGREDIENT_FOR_RECIPE).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<IngredientForRecipe> ingredients = new ArrayList<>();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    IngredientForRecipe ingredient = new IngredientForRecipe(
                                            dataSnapshot.child(INGREDIENT_KEY.NAME).getValue(String.class),
                                            dataSnapshot.child(INGREDIENT_KEY.QUANTITY).getValue(String.class));
                                    ingredients.add(ingredient);
                                }
                            recipesToDisplay = new ReguolarRecipes(
                                    OverallSnapshot.child(RECIPES_KEY.NAME).getValue(String.class),
                                    OverallSnapshot.child(RECIPES_KEY.DESCRIPTION).getValue(String.class),
                                    ingredients
                            );
                            textViewRecipeName.setText(recipesToDisplay.getName());
                            textViewRecipeDescription.setText(recipesToDisplay.getDescription());
                            displayRecipeAdapter = new DisplayRecipeAdapter(recipesToDisplay.getIngredientForRecipeArrayList(), DisplayRecipeActivity.this);
                            recyclerView.setAdapter(displayRecipeAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}