package com.example.recipesapplication.Service.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapplication.R;

import java.util.ArrayList;

public class FilterByIngredientsAdapter extends RecyclerView.Adapter<FilterByIngredientsAdapter.MyViewHolder> {
    private ArrayList<Ingredient> ingredientArrayList;

    public FilterByIngredientsAdapter(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.ingredient_list_layout , parent ,false);

        FilterByIngredientsAdapter.MyViewHolder myViewHolder = new FilterByIngredientsAdapter.MyViewHolder(view);
        return  myViewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText(this.ingredientArrayList.get(position).getName());
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientArrayList.remove(holder.getAdapterPosition());
                notifyItemChanged(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), ingredientArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.ingredientArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewName;
        Button removeBtn;
        public MyViewHolder (View itemView )
        {
            super(itemView);
            textViewName = ( TextView) itemView.findViewById(R.id.card_name);
            removeBtn = (Button) itemView.findViewById(R.id.remove_button);

        }

    }
}
