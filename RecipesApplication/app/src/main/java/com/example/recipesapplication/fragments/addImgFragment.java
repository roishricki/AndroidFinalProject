package com.example.recipesapplication.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Constant.Key.RECIPES_KEY;
import com.example.recipesapplication.Service.Constant.SCHEMA;
import com.example.recipesapplication.Service.Constant.USER_ACCESS_TOKEN;
import com.example.recipesapplication.Service.Models.IMGRecipes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.xml.namespace.NamespaceContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addImgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addImgFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private DatabaseReference RECIPES_Reference;
    private StorageTask mUploadTask;
    private String filename;
    private String filenameAccessToken;
    private boolean flag = false;
    private int count;
    private View vView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public addImgFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addImgFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addImgFragment newInstance(String param1, String param2) {
        addImgFragment fragment = new addImgFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_img, container, false);
        vView = view;
        mButtonChooseImage = view.findViewById(R.id.button_choose_image);
        mButtonUpload = view.findViewById(R.id.button_upload);
        mEditTextFileName = view.findViewById(R.id.edit_text_file_name);
        mImageView = view.findViewById(R.id.image_view);
        mProgressBar = view.findViewById(R.id.progress_bar);
        storageReference = FirebaseStorage.getInstance().getReference("uploads"); //"uploads"
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(mEditTextFileName.getText().toString().trim().equals("")) {
            mEditTextFileName.setBackgroundResource(R.drawable.edittextbackground);
            Toast.makeText(getActivity(), "Please Provide a File Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImageUri != null) {
            flag = true;
            filename = USER_ACCESS_TOKEN.TOKEN + '-' + System.currentTimeMillis() +  "." + getFileExtension(mImageUri);
            StorageReference fileReference = storageReference.child(filename);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    filenameAccessToken = uri.toString();
                                    RECIPES_Reference = firebaseDatabase.child(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
                                    RECIPES_Reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(!flag) return;
                                            count = Integer.parseInt((String)snapshot.child(RECIPES_KEY.COUNT).getValue());
                                            count++;
                                            IMGRecipes pdfRecipes = new IMGRecipes(mEditTextFileName.getText().toString().trim(), filenameAccessToken);
                                            RECIPES_Reference.child(String.valueOf(count)).setValue(pdfRecipes);
                                            RECIPES_Reference.child(RECIPES_KEY.COUNT).setValue(String.valueOf(count));
                                            flag = false;
                                            Navigation.findNavController(vView).navigate(R.id.action_addImgFragment_to_addRecipesMenu);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
//                            RECIPES_Reference = firebaseDatabase.child(SCHEMA.RECIPES).child(USER_ACCESS_TOKEN.TOKEN);
//                            RECIPES_Reference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(!flag) return;
//                                    count = Integer.parseInt((String)snapshot.child(RECIPES_KEY.COUNT).getValue());
//                                    count++;
//                                    IMGRecipes pdfRecipes = new IMGRecipes(mEditTextFileName.getText().toString().trim(), filenameAccessToken);
//                                    RECIPES_Reference.child(String.valueOf(count)).setValue(pdfRecipes);
//                                    RECIPES_Reference.child(RECIPES_KEY.COUNT).setValue(String.valueOf(count));
//                                    flag = false;
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST //&& resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this.getContext()).load(mImageUri).into(mImageView);
        }
    }
}