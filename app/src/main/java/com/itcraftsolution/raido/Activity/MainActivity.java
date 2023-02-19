package com.itcraftsolution.raido.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itcraftsolution.raido.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean isUserAgent = getIntent().getBooleanExtra("LoginUserOrAgent", false);


        if(isUserAgent){
            intent = new Intent(MainActivity.this, UserRideActivity.class);
        }else{
            intent = new Intent(MainActivity.this, AgentRideActivity.class);
        }
        startActivity(intent);
    }
}