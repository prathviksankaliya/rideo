package com.itcraftsolution.raido.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itcraftsolution.raido.Models.AgentDetails;
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.databinding.FragmentAgentProfileBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.util.Objects;

public class AgentProfileFragment extends Fragment {

    private FragmentAgentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SpfUserData spfUserData;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentProfileBinding.inflate(getLayoutInflater());

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
        databaseReference.child("AgentLoginDetails").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginDetails loginDetails = snapshot.getValue(LoginDetails.class);
                if(loginDetails != null){
                    Glide.with(requireContext()).load(loginDetails.getUserImage()).into(binding.igAdminProfilePic);
                    binding.edAdminUserName.setText(loginDetails.getUserName());
                    binding.edAdminEmail.setText(loginDetails.getUserEmail());
                    binding.edAdminPhone.setText(loginDetails.getUserPhone());
                    binding.txAdminUserName.setText(loginDetails.getUserName());
                    binding.txAdminRating.setText(String.valueOf(loginDetails.getUserRating()));

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