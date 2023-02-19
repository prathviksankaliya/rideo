package com.itcraftsolution.raido.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(getLayoutInflater());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getParentFragmentManager().beginTransaction().replace(R.id.frLoginContainer,
                        new LoginFragment()).addToBackStack(null).commit();
            }
        }, 1100);
        return binding.getRoot();
    }
}