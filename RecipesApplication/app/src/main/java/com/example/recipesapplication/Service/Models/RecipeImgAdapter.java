package com.example.recipesapplication.Service.Models;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeImgAdapter extends RecyclerView.Adapter<RecipeImgAdapter.MyViewHolder> {
    private ArrayList<IMGRecipes> recipesList;
    private Context context;

    public RecipeImgAdapter(ArrayList<IMGRecipes> recipesList, Context context) {
        this.recipesList = recipesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.my_imgs_layout , parent ,false);

        RecipeImgAdapter.MyViewHolder myViewHolder = new RecipeImgAdapter.MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText(this.recipesList.get(position).getName());
        Uri imgUri=Uri.parse(this.recipesList.get(position).getImgURL());
        Picasso.with(this.context.getApplicationContext()).load(imgUri).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return this.recipesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewName;
        ImageView imageView;

        public MyViewHolder (View itemView )
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView2);
            textViewName = ( TextView) itemView.findViewById(R.id.textViewRecipeName);

        }

    }
}
