package com.itcraftsolution.raido.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.itcraftsolution.raido.Fragments.AgentHomeFragment;
import com.itcraftsolution.raido.Fragments.AgentNotificationFragment;
import com.itcraftsolution.raido.Fragments.AgentProfileFragment;
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

        getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, new AgentHomeFragment()).addToBackStack(null).commit();
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.navHome:
                        temp = new AgentHomeFragment();
                        break;

                    case R.id.navNotification:
                        temp = new AgentNotificationFragment();
                        break;

                    case R.id.navProfile:
                        temp = new AgentProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, temp).addToBackStack(null).commit();
                return true;
            }
        });

    }
}