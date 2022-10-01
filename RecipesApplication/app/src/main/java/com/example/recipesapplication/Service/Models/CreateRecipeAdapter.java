package com.example.recipesapplication.Service.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapplication.R;
import java.util.ArrayList;

public class CreateRecipeAdapter extends RecyclerView.Adapter<CreateRecipeAdapter.MyViewHolder> {
    private ArrayList<IngredientForRecipe> ingredientForRecipeArrayList;

    public CreateRecipeAdapter(ArrayList<IngredientForRecipe> ingredientForRecipeArrayList) {
        this.ingredientForRecipeArrayList = ingredientForRecipeArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.create_recipe_layout , parent ,false);

        CreateRecipeAdapter.MyViewHolder myViewHolder = new CreateRecipeAdapter.MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            TextView quantity = holder.textViewQuantity;
            TextView name = holder.textViewName;
            Button removeBtn = holder.removeBtn;

            name.setText(this.ingredientForRecipeArrayList.get(position).getName());
            quantity.setText(this.ingredientForRecipeArrayList.get(position).getQuantity());

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ingredientForRecipeArrayList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
    }

    @Override
    public int getItemCount() {
        return ingredientForRecipeArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewName;
        TextView textViewQuantity;
        Button removeBtn;
        public MyViewHolder (View itemView )
        {
            super(itemView);
            removeBtn = (Button)itemView.findViewById(R.id.remove_button);
            textViewQuantity = (TextView) itemView.findViewById(R.id.card_quantity);
            textViewName = ( TextView) itemView.findViewById(R.id.card_name);

        }

    }
}
