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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.databinding.FragmentUserRideDeatilsBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.util.HashMap;

public class UserRideDeatilsFragment extends Fragment {

    private FragmentUserRideDeatilsBinding binding;
    private SpfUserData spfUserData;
    private DatabaseReference databaseReference, reqDbRef, friendRef;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String phone, agentId, date;
    private String currentState = "nothing_happen";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserRideDeatilsBinding.inflate(getLayoutInflater());

        spfUserData = new SpfUserData(requireContext());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("LoginDetails").child("AgentLoginDetails");
        reqDbRef = database.getReference("Requests");
        friendRef = database.getReference("Friends");
        loadData();

        binding.btnRideDetailCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + phone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        binding.btnRideDetailConfirmSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAction(agentId);
            }
        });
        checkUserExistance(agentId);


        return binding.getRoot();
    }

    private void loadData(){
        String arrivalTime = spfUserData.getSpfAgentRideDetails().getString("agentArrivalTime", null);
        String carName = spfUserData.getSpfAgentRideDetails().getString("agentCarName", null);
        date = spfUserData.getSpfAgentRideDetails().getString("agentDate", null);
        String depTime = spfUserData.getSpfAgentRideDetails().getString("agentDepTime", null);
        String emptySeats = spfUserData.getSpfAgentRideDetails().getString("agentEmptySeats", null);
        String journeySource = spfUserData.getSpfAgentRideDetails().getString("agentSource", null);
        String journeyDestination = spfUserData.getSpfAgentRideDetails().getString("agentDestination", null);
        String journeyLoc = spfUserData.getSpfAgentRideDetails().getString("agentJourneyLoc", null);
        String phoneNumber = spfUserData.getSpfAgentRideDetails().getString("agentPhoneNumber", null);
        String ridePrice = spfUserData.getSpfAgentRideDetails().getString("agentRidePrice", null);
        String totalJourney = spfUserData.getSpfAgentRideDetails().getString("agentTotalJourney", null);
        String vehicalNumber = spfUserData.getSpfAgentRideDetails().getString("agentVehicalNumber", null);
        agentId = spfUserData.getSpfAgentRideDetails().getString("agentId", null);


        binding.txRideDetailCarName.setText(carName);
        binding.txRideDetailCarNumber.setText(vehicalNumber);
        binding.txUserRideDetailSource.setText(journeySource);
        binding.txUserRideDetailDestination.setText(journeyDestination);
        binding.txUserRideDetailArrive.setText(arrivalTime);
        binding.txUserRideDetailDep.setText(depTime);
        binding.chGpRideDetailLocationChips.setText(journeyLoc);

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

    private void performAction(String agentID){
        if(currentState.equals("nothing_happen")){

            HashMap hashMap = new HashMap();
            hashMap.put("status", "pending");
            hashMap.put("userid", auth.getUid());
            hashMap.put("userimage", spfUserData.getSpfUserLoginDetails().getString("userImage", null));
            hashMap.put("username", spfUserData.getSpfUserLoginDetails().getString("userName", null));
            hashMap.put("userbookseat", spfUserData.getSpfUserSearchRide().getString("userSearchMember", null));
            hashMap.put("userdate",date );
            hashMap.put("usersource", binding.txUserRideDetailSource.getText().toString());
            hashMap.put("userdestination", binding.txUserRideDetailDestination.getText().toString());
            reqDbRef.child(agentID).child(auth.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(requireContext(), "Send The Ride Request to Agent", Toast.LENGTH_SHORT).show();
                        currentState = "i_sent_pending";
                    }else{
                        Toast.makeText(requireContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(currentState.equals("i_sent_pending") || currentState.equals("i_sent_decline")){

            reqDbRef.child(agentID).child(auth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(requireContext(), "Cancel Ride Request!!", Toast.LENGTH_SHORT).show();
                        currentState = "nothing_happen";
                    }else{
                        Toast.makeText(requireContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(currentState.equals("he_sent_pending")){

            reqDbRef.child(agentID).child(auth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        String userName = spfUserData.getSpfUserLoginDetails().getString("userName", null);
                        String userImg = spfUserData.getSpfUserLoginDetails().getString("userImage", null);
                        HashMap hashMap = new HashMap();
                        hashMap.put("status", "friend");
                        hashMap.put("userid", auth.getUid());
                        hashMap.put("username", userName);
                        hashMap.put("userimage", userImg);

                        friendRef.child(agentID).child(auth.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if(task.isSuccessful()){
                                    friendRef.child(auth.getUid()).child(agentID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(requireContext(), "You added Friend", Toast.LENGTH_SHORT).show();
                                            currentState = "friend";
                                        }
                                    });
                                }
                            }
                        });
                    }

                }
            });
        }

        if(currentState.equals("friend")){
//
        }
    }

    private void checkUserExistance(String agentId){

        friendRef.child(agentId).child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentState = "friend";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friendRef.child(auth.getUid()).child(agentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentState = "friend";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reqDbRef.child(agentId).child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        currentState = "i_sent_pending";
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        currentState = "i_sent_decline";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqDbRef.child(auth.getUid()).child(agentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        currentState = "he_sent_pending";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(currentState.equals("nothing_happen")){
                currentState = "nothing_happen";
        }
    }

}