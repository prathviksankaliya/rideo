package com.itcraftsolution.raido.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.itcraftsolution.raido.databinding.ActivityAgentRideBinding;

public class AgentRideActivity extends AppCompatActivity {

    private ActivityAgentRideBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgentRideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}