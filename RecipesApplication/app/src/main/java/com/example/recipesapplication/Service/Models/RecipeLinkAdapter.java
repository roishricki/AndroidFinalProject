package com.example.recipesapplication.Service.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapplication.R;

import java.util.ArrayList;

public class RecipeLinkAdapter extends RecyclerView.Adapter<RecipeLinkAdapter.MyViewHolder> {
    private ArrayList<LinkRecipes> recipesList;
    private Context context;
    public RecipeLinkAdapter(ArrayList<LinkRecipes> recipesList,Context context) {
        this.recipesList = recipesList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.my_links_layout , parent ,false);

        RecipeLinkAdapter.MyViewHolder myViewHolder = new RecipeLinkAdapter.MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TextView textViewName = holder.textViewName;
        String linkText = "Visit the <a href=http://"+recipesList.get(position).getLinkURL()+">"+ recipesList.get(position).getName() +"</a> recipe web page.";
        textViewName.setText(Html.fromHtml(linkText));
        textViewName.setMovementMethod(LinkMovementMethod.getInstance());

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = recipesList.get(position).getLinkURL();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.recipesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        CardView cardView;
        TextView textViewName;

        public MyViewHolder (View itemView )
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textViewName = ( TextView) itemView.findViewById(R.id.textViewName);

        }

    }
}
