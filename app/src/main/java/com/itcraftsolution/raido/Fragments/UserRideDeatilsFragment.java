package com.itcraftsolution.raido.Fragments;

import android.content.Intent;
import android.net.Uri;
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
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.databinding.FragmentUserRideDeatilsBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

public class UserRideDeatilsFragment extends Fragment {

    private FragmentUserRideDeatilsBinding binding;
    private SpfUserData spfUserData;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserRideDeatilsBinding.inflate(getLayoutInflater());

        spfUserData = new SpfUserData(requireContext());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("LoginDetails").child("AgentLoginDetails");

        loadData();


        return binding.getRoot();
    }

    private void loadData(){
        String arrivalTime = spfUserData.getSpfAgentRideDetails().getString("agentArrivalTime", null);
        String carName = spfUserData.getSpfAgentRideDetails().getString("agentCarName", null);
        String date = spfUserData.getSpfAgentRideDetails().getString("agentDate", null);
        String depTime = spfUserData.getSpfAgentRideDetails().getString("agentDepTime", null);
        String emptySeats = spfUserData.getSpfAgentRideDetails().getString("agentEmptySeats", null);
        String journeySource = spfUserData.getSpfAgentRideDetails().getString("agentSource", null);
        String journeyDestination = spfUserData.getSpfAgentRideDetails().getString("agentDestination", null);
        String journeyLoc = spfUserData.getSpfAgentRideDetails().getString("agentJourneyLoc", null);
        String phoneNumber = spfUserData.getSpfAgentRideDetails().getString("agentPhoneNumber", null);
        String ridePrice = spfUserData.getSpfAgentRideDetails().getString("agentRidePrice", null);
        String totalJourney = spfUserData.getSpfAgentRideDetails().getString("agentTotalJourney", null);
        String vehicalNumber = spfUserData.getSpfAgentRideDetails().getString("agentVehicalNumber", null);
        String agentId = spfUserData.getSpfAgentRideDetails().getString("agentId", null);


        binding.txRideDetailCarName.setText(carName);
        binding.txRideDetailCarNumber.setText(vehicalNumber);
        binding.txUserRideDetailSource.setText(journeySource);
        binding.txUserRideDetailDestination.setText(journeyDestination);
        binding.txUserRideDetailArrive.setText(arrivalTime);
        binding.txUserRideDetailDep.setText(depTime);
        binding.chGpRideDetailLocationChips.setText(journeyLoc);

        binding.btnRideDetailCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + phone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        Toast.makeText(requireContext(), ""+agentId, Toast.LENGTH_SHORT).show();
        databaseReference.child(agentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginDetails model = snapshot.getValue(LoginDetails.class);
                if(model != null){
                    binding.txRideDetailUserName.setText(model.getUserName());
                    binding.txRideDetailUserRating.setText(String.valueOf(model.getUserRating()));
                    Glide.with(requireContext()).load(model.getUserImage()).into(binding.igRideUserPic);
                    phone = model.getUserPhone();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}