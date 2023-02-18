package com.itcraftsolution.raido.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.itcraftsolution.raido.Fragments.HomeFragment;
import com.itcraftsolution.raido.Fragments.NotificationFragment;
import com.itcraftsolution.raido.Fragments.ProfileFragment;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.ActivityAgentRideBinding;

import java.util.Objects;

public class AgentRideActivity extends AppCompatActivity {

    private ActivityAgentRideBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgentRideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, new HomeFragment()).addToBackStack(null).commit();
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.navHome:
                        temp = new HomeFragment();
                        break;

                    case R.id.navNotification:
                        temp = new NotificationFragment();
                        break;

                    case R.id.navProfile:
                        temp = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, temp).addToBackStack(null).commit();
                return true;
            }
        });

    }
}