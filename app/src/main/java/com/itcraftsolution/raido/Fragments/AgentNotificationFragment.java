package com.itcraftsolution.raido.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Adapter.RvAgentNotificationAdapter;
import com.itcraftsolution.raido.Adapter.RvUserActiveRideAdapter;
import com.itcraftsolution.raido.Models.AgentDetails;
import com.itcraftsolution.raido.Models.DemoModel;
import com.itcraftsolution.raido.databinding.FragmentAgentNotificationBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class AgentNotificationFragment extends Fragment {

    private FragmentAgentNotificationBinding binding;
    private String currentState = "nothing_happen";
    private DatabaseReference reqDbRef, friendRef;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String userID;
    private RvAgentNotificationAdapter adapter;


    private ArrayList<DemoModel> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentNotificationBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reqDbRef = database.getReference("Requests");
        friendRef = database.getReference("Friends");
        list = new ArrayList<>();

        checkUserExistance();

        return binding.getRoot();
    }

    private void checkUserExistance(){

        reqDbRef.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()){
                    DemoModel model = sp.getValue(DemoModel.class);
                    list.add(model);
                }
                adapter = new RvAgentNotificationAdapter(requireContext(), list);
                binding.rvAdminNotification.setAdapter(adapter);
                binding.rvAdminNotification.setLayoutManager(new LinearLayoutManager(requireContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}