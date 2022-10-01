package com.example.recipesapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.Ingredient;
import com.example.recipesapplication.Service.Models.LinkRecipes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addIngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addIngredientFragment extends Fragment {
    private EditText editTextNameIngredient ;
    private String ingredientName ;
    private String ingredientNameWithoutSpaces;
    private DatabaseReference INGREDIENT_Reference;
    private boolean flag = false;
    private int count;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addIngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addIngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addIngredientFragment newInstance(String param1, String param2) {
        addIngredientFragment fragment = new addIngredientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_ingredient, container, false);
        Button backBtn = (Button)view.findViewById(R.id.back_button);
        Button addBtn = (Button)view.findViewById(R.id.add_button);
        Button myIngredientsBtn = (Button)view.findViewById(R.id.ingredient_list);


        myIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_addIngredientFragment_to_ingredient);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_addIngredientFragment_to_homePageApp);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                editTextNameIngredient = view.findViewById(R.id.editText_name_ingredient);
                ingredientName = editTextNameIngredient.getText().toString();
                ingredientNameWithoutSpaces = ingredientName.replace(" ","");
                if(ingredientName==null ||ingredientName.isEmpty() || ingredientNameWithoutSpaces.isEmpty()) {
                    editTextNameIngredient.setBackgroundResource(R.drawable.edittextbackground);
                    Toast.makeText(getActivity(),"Please Enter Ingredient Name!",Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                INGREDIENT_Reference = firebaseDatabase.getReference(SCHEMA.INGREDIENT).child(USER_ACCESS_TOKEN.TOKEN);
                INGREDIENT_Reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(ingredientName==null ||ingredientName.isEmpty() || ingredientNameWithoutSpaces.isEmpty() || !flag) return;
                        count = Integer.parseInt((String)snapshot.child(RECIPES_KEY.COUNT).getValue());
                        count++;
                        Ingredient ingredient = new Ingredient(ingredientName);
                        INGREDIENT_Reference.child(String.valueOf(count)).setValue(ingredient);
                        INGREDIENT_Reference.child(RECIPES_KEY.COUNT).setValue(String.valueOf(count));
                        flag = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getActivity(),"Ingredient Added!",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}