package com.example.recipesapplication.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.INGREDIENT_KEY;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.LinkRecipes;
import com.example.recipesapplication.activites.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addLinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addLinkFragment extends Fragment {

    private  EditText recipeNameEditText;
    private String recipeName ;
    private EditText recipeLinkEditText;
    private String recipeLink ;
    private DatabaseReference RECIPES_Reference;
    private int count;
    private boolean flag = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addLinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addLinkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addLinkFragment newInstance(String param1, String param2) {
        addLinkFragment fragment = new addLinkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isValidLink(String link){
        boolean isValid = Pattern.compile("((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)").matcher(link).matches();
        return isValid;
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
        View view = inflater.inflate(R.layout.fragment_add_link, container, false);
        Button sendButton = (Button)view.findViewById(R.id.addlinkbutton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                flag = true;
                recipeNameEditText = view.findViewById(R.id.recipename);
                recipeName = recipeNameEditText.getText().toString();
                recipeLinkEditText = view.findViewById(R.id.recipelink);
                recipeLink = recipeLinkEditText.getText().toString();

                if(recipeName.isEmpty()) recipeNameEditText.setBackgroundResource(R.drawable.edittextbackground);
                if(recipeLink.isEmpty())recipeLinkEditText.setBackgroundResource(R.drawable.edittextbackground);
                if(!isValidLink(recipeLink)){
                    Toast.makeText(getActivity(),"Link is not valid",Toast.LENGTH_LONG).show();
                    recipeLinkEditText.setBackgroundResource(R.drawable.edittextbackground);
                }
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
                RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(((recipeName.isEmpty() || recipeLink.isEmpty() || !isValidLink(recipeLink)) || !flag)) return;
                        count = Integer.parseInt((String)snapshot.child(RECIPES_KEY.COUNT).getValue());
                        count++;
                        LinkRecipes linkRecipes = new LinkRecipes(recipeName, recipeLink);
                        RECIPES_Reference.child(String.valueOf(count)).setValue(linkRecipes);
                        RECIPES_Reference.child(RECIPES_KEY.COUNT).setValue(String.valueOf(count));
                        flag = false;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if((recipeName.isEmpty() || recipeLink.isEmpty() || !isValidLink(recipeLink))) return;
                Toast.makeText(getActivity(),"Recipe link was added",Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_addLinkFragment_to_homePageApp);
            }

        });

        return view;
    }
}