package com.itcraftsolution.raido.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Adapter.RvDemoAdapter;
import com.itcraftsolution.raido.Models.AgentDetails;
import com.itcraftsolution.raido.Models.DemoModel;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.ActivityDemoBinding;

public class DemoActivity extends AppCompatActivity {

    private ActivityDemoBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("AgentRideDetails").child("Roots");


        databaseReference.child(  "Ahmedabad_Amreli" ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DemoModel model = snapshot.getValue(DemoModel.class);
                Toast.makeText(DemoActivity.this, ""+model.getAgentId().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<DemoModel> options =
                new FirebaseRecyclerOptions.Builder<DemoModel>().setQuery(databaseReference.child(  "Ahmedabad_Amreli" ), DemoModel.class)
                        .build();

        RvDemoAdapter adapter = new RvDemoAdapter(options, DemoActivity.this);
        binding.rvDemo.setAdapter(adapter);
        binding.rvDemo.setLayoutManager(new LinearLayoutManager(DemoActivity.this));


    }
}