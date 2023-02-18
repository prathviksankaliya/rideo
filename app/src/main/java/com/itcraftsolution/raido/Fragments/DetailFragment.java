package com.itcraftsolution.raido.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentDetailBinding;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(getLayoutInflater());

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chip chip = new Chip(requireContext());
                ChipDrawable drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0,
                        com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry);
                chip.setChipDrawable(drawable);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setChipIconResource(R.drawable.baseline_location_on_24);
                chip.setPadding(60, 10, 60, 10);
                chip.setText(binding.AdminJourneyLocChips.getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.AdminLocationChip.removeView(chip);
                    }
                });
                binding.AdminLocationChip.addView(chip);
                binding.AdminJourneyLocChips.setText("");
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelections();
            }
        });
        return binding.getRoot();
    }

    private void showSelections()
    {
        int count = binding.AdminLocationChip.getChildCount();

        String s = null;
        for(int i = 0; i < count; i++)
        {
            Chip chip = (Chip) binding.AdminLocationChip.getChildAt(i);
            if(s == null)
            {
                    s = chip.getText().toString();
            }else{
                s += ", " + chip.getText().toString();
            }
        }
        Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
    }
}