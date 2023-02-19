package com.itcraftsolution.raido.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.raido.Activity.MainActivity;
import com.itcraftsolution.raido.databinding.FragmentLoginProfileBinding;

public class LoginProfileFragment extends Fragment {

    private FragmentLoginProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginProfileBinding.inflate(getLayoutInflater());
        binding.btnLoginSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        });
        return binding.getRoot();
    }
}