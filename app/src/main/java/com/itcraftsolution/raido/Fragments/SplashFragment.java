package com.itcraftsolution.raido.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.itcraftsolution.raido.Activity.AgentRideActivity;
import com.itcraftsolution.raido.Activity.UserRideActivity;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentSplashBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private FirebaseAuth auth;
    private SpfUserData spfUserData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        spfUserData = new SpfUserData(requireContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userType = spfUserData.getSpfUserLoginDetails().getString("userType", null);
                if (userType != null && auth.getCurrentUser() != null) {
                    if (userType.equals("Car Agent")) {
                        startActivity(new Intent(requireContext(), AgentRideActivity.class));
                        requireActivity().finishAffinity();
                    } else {
                        startActivity(new Intent(requireContext(), UserRideActivity.class));
                        requireActivity().finishAffinity();
                    }
                } else {
                    getParentFragmentManager().beginTransaction().replace(R.id.frLoginContainer,
                            new LoginFragment()).addToBackStack(null).commit();
                }
            }
        }, 1100);
        return binding.getRoot();
    }
}