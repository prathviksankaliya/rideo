package com.itcraftsolution.raido.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.itcraftsolution.raido.databinding.ActivityUserRideBinding;

public class UserRideActivity extends AppCompatActivity {

    private ActivityUserRideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}