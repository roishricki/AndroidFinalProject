package com.example.recipesapplication.Service.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapplication.R;

import java.util.ArrayList;

public class DisplayRecipeAdapter extends RecyclerView.Adapter<DisplayRecipeAdapter.MyViewHolder> {
    public DisplayRecipeAdapter(ArrayList<IngredientForRecipe>  ingredientsList, Context context) {
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    private ArrayList<IngredientForRecipe> ingredientsList;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.ingredient_display_recipe , parent ,false);

        DisplayRecipeAdapter.MyViewHolder myViewHolder = new DisplayRecipeAdapter.MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText(ingredientsList.get(position).getName());
        holder.textViewQuantity.setText(ingredientsList.get(position).getQuantity());
    }


    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewName;
        TextView textViewQuantity;
        public MyViewHolder (View itemView )
        {
            super(itemView);
            textViewQuantity = (TextView) itemView.findViewById(R.id.card_quantity);
            textViewName = ( TextView) itemView.findViewById(R.id.card_name);

        }

    }
}
