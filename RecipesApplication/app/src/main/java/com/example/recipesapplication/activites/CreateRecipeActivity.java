package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.CreateRecipeAdapter;
import com.example.recipesapplication.Service.Models.Ingredient;
import com.example.recipesapplication.Service.Models.IngredientForRecipe;
import com.example.recipesapplication.Service.Models.ReguolarRecipes;
import com.example.recipesapplication.fragments.HomePageApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CreateRecipeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private CreateRecipeAdapter createRecipeAdapter;
    public ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    public ArrayList<IngredientForRecipe> ingredientForRecipeArrayList = new ArrayList<>();
    public IngredientForRecipe ingredientToAdd;
    private Button addBtn;
    private Button removeBtn;
    private Button submitBtn;
    private EditText editTextQuantity;
    private EditText EditTextDescription;
    private EditText editTextRecipeName;
    private DatabaseReference RECIPES_Reference;
    private boolean flag = false;
    private int count;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // perform your action here

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycle_view);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addBtn = (Button) findViewById(R.id.button_add);
        removeBtn = (Button) findViewById(R.id.remove_button);
        submitBtn = (Button) findViewById(R.id.button_submit);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                EditTextDescription = (EditText) findViewById(R.id.editTextDescription);
                String description = EditTextDescription.getText().toString();
                editTextRecipeName = (EditText)findViewById(R.id.recipename);
                String recipeName = editTextRecipeName.getText().toString();

                RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!flag||recipeName.trim().isEmpty() || description.trim().isEmpty()||ingredientForRecipeArrayList.isEmpty()) return;
                        count = Integer.parseInt((String)snapshot.child(RECIPES_KEY.COUNT).getValue());
                        count++;
                        ReguolarRecipes reguolarRecipes = new ReguolarRecipes(recipeName, description, ingredientForRecipeArrayList);
                        RECIPES_Reference.child(String.valueOf(count)).setValue(reguolarRecipes);
                        RECIPES_Reference.child(RECIPES_KEY.COUNT).setValue(String.valueOf(count));
                        flag = false;
                        Intent intent = new Intent(CreateRecipeActivity.this.getApplicationContext(), MyAppRecipeMenuActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if((recipeName.trim().isEmpty() || description.trim().isEmpty()||ingredientForRecipeArrayList.isEmpty())) {
                   if(recipeName.trim().isEmpty()) editTextRecipeName.setBackgroundResource(R.drawable.edittextbackground);
                   if(description.trim().isEmpty()) EditTextDescription.setBackgroundResource(R.drawable.edittextbackground);
                   return;
                }
                Toast.makeText(CreateRecipeActivity.this,recipeName +" Recipe was added",Toast.LENGTH_LONG).show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextQuantity = (EditText)findViewById(R.id.editTextQuantity);
                String quantityEditText = editTextQuantity.getText().toString();
                if(quantityEditText== null || quantityEditText.trim()=="" || quantityEditText=="" ){
                    Toast.makeText(CreateRecipeActivity.this,"Please provide quantity!",Toast.LENGTH_LONG).show();
                    editTextQuantity.setBackgroundResource(R.drawable.edittextbackground);
                    return;
                }
                if(ingredientToAdd==null || ingredientToAdd.getName()=="Select Ingredient"){
                    Toast.makeText(CreateRecipeActivity.this,"Please select ingredient!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(ingredientToAdd.getQuantity()==null){
                    ingredientToAdd = new IngredientForRecipe(ingredientToAdd.getName(),quantityEditText);
                }
                ingredientForRecipeArrayList.add(ingredientToAdd);
                ingredientToAdd=null;
                editTextQuantity.setText("");
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                spinner.setSelection(0);
                closeKeyboard();
            }
        });

        initIngreditent();
        createRecipeAdapter = new CreateRecipeAdapter(ingredientForRecipeArrayList);
        recyclerView.setAdapter(createRecipeAdapter);
    }

    public void initIngreditent(){
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
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CreateRecipeActivity.this,android.R.layout.simple_spinner_item,stringIngredientsArray);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String name = stringIngredientsArray.get(position);
                        ingredientToAdd = new IngredientForRecipe(name);
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

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(CreateRecipeActivity.this.getApplicationContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

}