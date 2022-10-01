package com.example.recipesapplication.Service.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.CUR_RECIPES;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.activites.CreateRecipeActivity;
import com.example.recipesapplication.activites.DisplayRecipeActivity;
import com.example.recipesapplication.activites.MainActivity;
import com.example.recipesapplication.activites.MyAppRecipeMenuActivity;
import com.example.recipesapplication.activites.MyImgActivity;
import com.example.recipesapplication.fragments.HomePageApp;

import java.util.ArrayList;

public class MyRecipeAppAdapter extends RecyclerView.Adapter<MyRecipeAppAdapter.MyViewHolder> {

    private ArrayList<ReguolarRecipes> recipesList;
    private Context context;
    private int position;

    public MyRecipeAppAdapter(ArrayList<ReguolarRecipes> recipesList, Context context) {
        this.recipesList = recipesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.fragment_my_recipes , parent ,false);

        MyRecipeAppAdapter.MyViewHolder myViewHolder = new MyRecipeAppAdapter.MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewItemNumber.setText(this.recipesList.get(position).getId());
        holder.textViewNameRecipe.setText(this.recipesList.get(position).getName());
        holder.recipeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUR_RECIPES.ID = recipesList.get(holder.getAdapterPosition()).getId();
                Intent intent = new Intent(MyRecipeAppAdapter.this.context, DisplayRecipeActivity.class);
                MyRecipeAppAdapter.this.context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewItemNumber;
        TextView textViewNameRecipe;
        CardView recipeCardView;

        public MyViewHolder (View itemView )
        {
            super(itemView);
            recipeCardView = (CardView)itemView.findViewById(R.id.recipe_cardview);
            textViewItemNumber = (TextView)itemView.findViewById(R.id.textViewItemNumber);
            textViewNameRecipe = (TextView)itemView.findViewById(R.id.textViewNameRecipe);


        }

    }
}
