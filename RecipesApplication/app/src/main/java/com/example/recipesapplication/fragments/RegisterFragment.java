package com.example.recipesapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipesapplication.R;
import com.example.recipesapplication.Service.Models.UserDetails;
import com.example.recipesapplication.activites.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        Button regButton = view.findViewById(R.id.registerUser);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = view.findViewById(R.id.mailReg);
                String email = emailEditText.getText().toString();
                EditText passEditText = view.findViewById(R.id.passwordReg);
                String password = passEditText.getText().toString();
                EditText nameEditText = view.findViewById(R.id.nameReg);
                String name = nameEditText.getText().toString();
                EditText phoneEditText = view.findViewById(R.id.phoneReg);
                String phone = phoneEditText.getText().toString();
                if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()){
                    Toast.makeText(getActivity(),"All field must be populated",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(getActivity(),"password must be 6 digits",Toast.LENGTH_LONG).show();

                }
                if(!((MainActivity)getActivity()).validateEmail(email)){
                    Toast.makeText(getActivity(),"Email address is invalid",Toast.LENGTH_LONG).show();
                    return;
                }
                UserDetails userDetails = new UserDetails("", name, phone);
                ((MainActivity)getActivity()).registerFunc(view,email,password, userDetails);
            }
        });
        return view;
    }
}