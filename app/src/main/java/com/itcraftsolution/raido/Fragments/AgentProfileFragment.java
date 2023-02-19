package com.itcraftsolution.raido.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.raido.databinding.FragmentAgentProfileBinding;

public class AgentProfileFragment extends Fragment {

    private FragmentAgentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentProfileBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}