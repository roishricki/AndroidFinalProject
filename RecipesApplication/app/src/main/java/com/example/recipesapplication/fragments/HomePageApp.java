package com.example.recipesapplication.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.CUR_RECIPES;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.activites.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageApp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageApp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageApp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageApp.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageApp newInstance(String param1, String param2) {
        HomePageApp fragment = new HomePageApp();
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
        View view = inflater.inflate(R.layout.fragment_home_page_app, container, false);
        CardView addRecipesCardView = (CardView)view.findViewById(R.id.addrecipes_card_view);
        CardView ingredientCardView = (CardView)view.findViewById(R.id.ingredients_card_view);
        CardView myRecipesCardView = (CardView)view.findViewById(R.id.myrecipes_card_view);
        Button addIngredientBtn = (Button)view.findViewById(R.id.add_ingredient_button);


        myRecipesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homePageApp_to_myRecipesMenu);
            }
        });

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homePageApp_to_addIngredientFragment);
            }
        });


        addRecipesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homePageApp_to_addRecipesMenu);
            }
        });

        ingredientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homePageApp_to_ingredient);
            }
        });

        return view;
    }

//    public void getActivityParams(){
//        if(USER_ACCESS_TOKEN.TOKEN==null){
//            Bundle extras = getActivity().getIntent().getExtras();
//            if(extras == null) return;
//            USER_ACCESS_TOKEN.TOKEN = extras.getString("USER_TOKEN");
//            CUR_RECIPES.ID = extras.getString("recipe_id");
//        }
//    }
}