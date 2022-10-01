package com.example.recipesapplication.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.IMGRecipes;
import com.example.recipesapplication.Service.Models.LinkRecipes;
import com.example.recipesapplication.Service.Models.RecipeImgAdapter;
import com.example.recipesapplication.Service.Models.RecipeLinkAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class MyImgActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecipeImgAdapter recipeImgAdapter;
    public ArrayList<IMGRecipes> list = new ArrayList<>();
    private DatabaseReference RECIPES_Reference;
    private StorageReference storageReference;

    private Uri mImageUri;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_img);
        mImageView = findViewById(R.id.imageView2);
        recyclerView = (RecyclerView) findViewById(R.id.myimgs_recycle_view);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if(USER_ACCESS_TOKEN.TOKEN!=null){
            RECIPES_Reference = firebaseDatabase.getReference(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
            RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        if(dataSnapshot.getKey() != RECIPES_KEY.COUNT && Objects.equals(dataSnapshot.child(RECIPES_KEY.TYPE).getValue(String.class), "img")) {
                            IMGRecipes imgRecipes = dataSnapshot.getValue(IMGRecipes.class);

                            list.add(imgRecipes);
                        }
                    recipeImgAdapter = new RecipeImgAdapter(list,MyImgActivity.this);
                    recyclerView.setAdapter(recipeImgAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}