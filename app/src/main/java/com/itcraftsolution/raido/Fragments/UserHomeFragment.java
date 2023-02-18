package com.itcraftsolution.raido.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentUserHomeBinding;

public class UserHomeFragment extends Fragment {

    private FragmentUserHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(getLayoutInflater());

        binding.btnSearchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.frUserMainContainer,
                        new UserDetailRideFragment()).addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }
}