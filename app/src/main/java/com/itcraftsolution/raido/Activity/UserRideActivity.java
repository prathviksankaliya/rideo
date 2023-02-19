package com.itcraftsolution.raido.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.itcraftsolution.raido.Fragments.UserCarBookFragment;
import com.itcraftsolution.raido.Fragments.UserHomeFragment;
import com.itcraftsolution.raido.Fragments.UserNotificationFragment;
import com.itcraftsolution.raido.Fragments.UserProfileFragment;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.ActivityUserRideBinding;

import java.util.Objects;

public class UserRideActivity extends AppCompatActivity {

    private ActivityUserRideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRideBinding.inflate(getLayoutInflater());

        Objects.requireNonNull(getSupportActionBar()).hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.frUserMainContainer, new UserHomeFragment()).addToBackStack(null).commit();
        binding.bNavUser.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.userNavHome:
                        temp = new UserHomeFragment();
                        break;

                    case R.id.userNavNotification:
                        temp = new UserNotificationFragment();
                        break;

                    case R.id.userNavAddCar:
                        temp = new UserCarBookFragment();
                        break;

                    case R.id.userNavProfile:
                        temp = new UserProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frUserMainContainer, temp).addToBackStack(null).commit();
                return true;
            }
        });
        setContentView(binding.getRoot());

    }
}