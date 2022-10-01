package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.FILTERED_RECIPES;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.FilterByIngredientsAdapter;
import com.example.recipesapplication.Service.Models.Ingredient;
import com.example.recipesapplication.Service.Models.MyRecipeAppAdapter;
import com.example.recipesapplication.Service.Models.ReguolarRecipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class FilterByIngredientsActivity extends AppCompatActivity {

    private Button addBtn;
    private Button filterBtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private FilterByIngredientsAdapter filterByIngredientsAdapter;
    public ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    public ArrayList<Ingredient> ingredientsToFilterBy = new ArrayList<>();
    public ArrayList<String> ingredientsToFilterBySTR = new ArrayList<>();
    private Ingredient ingredientToAdd;
    public TreeMap<Integer, ArrayList<ReguolarRecipes>> list = new TreeMap<Integer, ArrayList<ReguolarRecipes>>();
    private int count = 0;

    private DatabaseReference RECIPES_Reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_ingredients);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycle_view);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addBtn = (Button) findViewById(R.id.button_add);
        filterBtn = (Button) findViewById(R.id.button);


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                if(USER_ACCESS_TOKEN.TOKEN!=null){
                    RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
                    RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot OverallSnapshot : snapshot.getChildren())
                                if(OverallSnapshot.getKey() != RECIPES_KEY.COUNT && Objects.equals(OverallSnapshot.child(RECIPES_KEY.TYPE).getValue(String.class), "regular")) {
                                    OverallSnapshot.child(INGREDIENT_KEY.INGREDIENT_FOR_RECIPE).getRef().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(ingredientsToFilterBy.isEmpty()) return;
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                count = 0;
                                                if(ingredientsToFilterBySTR.contains(dataSnapshot.child(INGREDIENT_KEY.NAME).getValue(String.class))) {
                                                    count++;
                                                }
                                                if((count / ingredientsToFilterBySTR.size()) * 100 >= 75) {
                                                    ReguolarRecipes reguolarRecipes = new ReguolarRecipes(
                                                            OverallSnapshot.child(RECIPES_KEY.NAME).getValue(String.class),
                                                            OverallSnapshot.getKey());
                                                    ArrayList<ReguolarRecipes> tempList;
                                                    if(list.containsKey(count)) {
                                                        tempList = list.get(count);
                                                    } else {
                                                        tempList = new ArrayList<>();
                                                    }
                                                    tempList.add(reguolarRecipes);
                                                    list.put(count, tempList);
                                                }
                                            }
                                            FILTERED_RECIPES.RECIPES = new ArrayList<>();
                                            for (Map.Entry<Integer, ArrayList<ReguolarRecipes>> entry : list.entrySet()) {
                                                for(ReguolarRecipes recipe : entry.getValue()) {
                                                    FILTERED_RECIPES.RECIPES.add(recipe);
                                                }
                                            }
                                            FILTERED_RECIPES.FLAG=true;
                                            Intent intent = new Intent(FilterByIngredientsActivity.this.getApplicationContext(), MyAppRecipeMenuActivity.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ingredientToAdd==null || ingredientToAdd.getName()=="Select Ingredient"){
                    Toast.makeText(FilterByIngredientsActivity.this,"Please select ingredient!",Toast.LENGTH_LONG).show();
                    return;
                }
                ingredientsToFilterBySTR.add(ingredientToAdd.getName());
                ingredientsToFilterBy.add(ingredientToAdd);
                ingredientToAdd=null;
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                spinner.setSelection(0);
                filterByIngredientsAdapter = new FilterByIngredientsAdapter(ingredientsToFilterBy);
                recyclerView.setAdapter(filterByIngredientsAdapter);
            }
        });
        initIngredients();
        filterByIngredientsAdapter = new FilterByIngredientsAdapter(ingredientsToFilterBy);
        recyclerView.setAdapter(filterByIngredientsAdapter);
    }

    public void initIngredients() {
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
                    ingredientArrayList.add(ingredient);
                }
                if(user_ingredient_count != 0) {
                    for(DataSnapshot user_snapshot : user_ingredient_snapshot.getChildren()) {
                        if(user_snapshot.getKey() != INGREDIENT_KEY.COUNT) {
                            Ingredient ingredient = new Ingredient(user_snapshot.child(INGREDIENT_KEY.NAME).getValue(String.class));//user_snapshot.getValue(Ingredient.class);
                            if(ingredient.getName()!=null) ingredientArrayList.add(ingredient);
                        }
                    }
                }

                ArrayList<String> stringIngredientsArray = new ArrayList<>();
                stringIngredientsArray.add("Select Ingredient");
                for (Ingredient ingredient : ingredientArrayList) {
                    stringIngredientsArray.add(ingredient.getName());
                }

                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(FilterByIngredientsActivity.this,android.R.layout.simple_spinner_item,stringIngredientsArray);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String name = stringIngredientsArray.get(position);
                        ingredientToAdd = new Ingredient(name);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
