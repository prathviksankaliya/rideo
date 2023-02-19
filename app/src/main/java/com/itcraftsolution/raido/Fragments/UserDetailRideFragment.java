package com.itcraftsolution.raido.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Adapter.RvUserActiveRideAdapter;
import com.itcraftsolution.raido.Models.AgentDetails;
import com.itcraftsolution.raido.Models.DemoModel;
import com.itcraftsolution.raido.databinding.FragmentUserDetailRideBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.util.ArrayList;

public class UserDetailRideFragment extends Fragment {

    private FragmentUserDetailRideBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private SpfUserData spfUserData;
    private RvUserActiveRideAdapter adapter;
    private ArrayList<AgentDetails> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailRideBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("AgentRideDetails").child("Roots");
        spfUserData = new SpfUserData(requireContext());

        list = new ArrayList<>();
        getDataFromFirebaseDatabase();

        return binding.getRoot();
    }

    private void getDataFromFirebaseDatabase(){

        String fromCity = spfUserData.getSpfUserSearchRide().getString("userSearchFrom", null);
        String toCity = spfUserData.getSpfUserSearchRide().getString("userSearchTo", null);
        String date = spfUserData.getSpfUserSearchRide().getString("userSearchDate", null);
        String members = spfUserData.getSpfUserSearchRide().getString("userSearchMember", null);

//        FirebaseRecyclerOptions<AgentDetails> options =
//                new FirebaseRecyclerOptions.Builder<AgentDetails>().setQuery(databaseReference.child(  "Ahmedabad_Amreli" ), AgentDetails.class)
//                        .build();

//        Toast.makeText(requireContext(), ""+options.getSnapshots().get(0), Toast.LENGTH_SHORT).show();
//        adapter = new RvUserActiveRideAdapter(options);

//        ArrayList<AgentDetails> list = new ArrayList<>();
//        list.add(new AgentDetails("Bmw 7 g", "kjbr", "7308713087", "18/02/2023",
//                "3", "2.30", "", "Limbdi", "Rajkot", "10.20", "12.30", "350", "Ahm, Limbdi, Rajkot"));
//        adapter = new RvUserActiveRideAdapter(options);
//        binding.rvActiveRide.setAdapter(adapter);
//        binding.rvActiveRide.setLayoutManager(new LinearLayoutManager(requireContext()));



        databaseReference.child(fromCity + "_" + toCity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()){
                        AgentDetails model = sp.getValue(AgentDetails.class);
                        list.add(model);
                }
                adapter = new RvUserActiveRideAdapter(requireContext(), list);
                binding.rvActiveRide.setAdapter(adapter);
                binding.rvActiveRide.setLayoutManager(new LinearLayoutManager(requireContext()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}