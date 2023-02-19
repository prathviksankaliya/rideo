package com.itcraftsolution.raido.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Activity.MainActivity;
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentUserHomeBinding;
import com.itcraftsolution.raido.databinding.FragmentUserProfileBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.util.Objects;

public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SpfUserData spfUserData;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("LoginDetails");
        spfUserData = new SpfUserData(requireContext());

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        loadProfileData();

        return binding.getRoot();
    }

    private void loadProfileData(){
        databaseReference.child("UserLoginDetails").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginDetails loginDetails = snapshot.getValue(LoginDetails.class);
                if(loginDetails != null){
                    Glide.with(requireContext()).load(loginDetails.getUserImage()).into(binding.igUserProfilePic);
                    binding.edUserName.setText(loginDetails.getUserName());
                    binding.edUserEmail.setText(loginDetails.getUserEmail());
                    binding.edUserNumber.setText(loginDetails.getUserPhone());
                    binding.txAdminUserName.setText(loginDetails.getUserName());

                    spfUserData.setSpfUserLoginDetails(loginDetails.getUserName(), loginDetails.getUserImage(),
                            loginDetails.getUserEmail(), loginDetails.getUserPhone(), loginDetails.getUserType(), loginDetails.getUserGender());
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}