package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.Ingredient;
import com.example.recipesapplication.Service.Models.IngredientAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class IngredientActivity extends AppCompatActivity {
    private IngredientAdapter ingredientAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    public ArrayList<Ingredient> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

//       addIngredientButton = (Button)findViewById(R.id.add_ingredient_button);
        recyclerView = (RecyclerView) findViewById(R.id.ingredient_recycle_view);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        addIngredientButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setContentView(R.layout.fragment_add_ingredient);
//            }
//        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ingredient_reference = firebaseDatabase.getReference().child(SCHEMA.INGREDIENT);//.child("sys");
        //DatabaseReference user__ingredient_reference = firebaseDatabase.getReference().child(SCHEMA.INGREDIENT).child(USER_ACCESS_TOKEN.TOKEN);
        ingredient_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot sys_ingredient_snapshot = snapshot.child("sys");
                DataSnapshot user_ingredient_snapshot = snapshot.child(USER_ACCESS_TOKEN.TOKEN);
                int user_ingredient_count = Integer.parseInt(Objects.requireNonNull(snapshot.child(USER_ACCESS_TOKEN.TOKEN).child(INGREDIENT_KEY.COUNT).getValue(String.class)));
                for(DataSnapshot sys_snapshot : sys_ingredient_snapshot.getChildren()) {
                    Ingredient ingredient =  new Ingredient(sys_snapshot.child(INGREDIENT_KEY.NAME).getValue(String.class));// sys_snapshot.getValue(Ingredient.class);
                    list.add(ingredient);
                }
                if(user_ingredient_count != 0) {
                    for(DataSnapshot user_snapshot : user_ingredient_snapshot.getChildren()) {
                        if(user_snapshot.getKey() != INGREDIENT_KEY.COUNT) {
                            Ingredient ingredient = new Ingredient(user_snapshot.child(INGREDIENT_KEY.NAME).getValue(String.class));//user_snapshot.getValue(Ingredient.class);
                            if(ingredient.getName()!=null) list.add(ingredient);
                        }
                    }
                }
                ingredientAdapter = new IngredientAdapter(list);
                recyclerView.setAdapter(ingredientAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}