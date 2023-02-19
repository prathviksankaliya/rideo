package com.itcraftsolution.raido.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentAgentDetailBinding;

import java.util.ArrayList;

public class AgentDetailFragment extends Fragment {

    private FragmentAgentDetailBinding binding;
    private ArrayList<String> arrayList;
    private Dialog dialog;
    private String selectedDistrictSource, selectedJourneyLoc, selectedDistrictDestination;
    private ArrayAdapter<CharSequence> districtAdapterSource, districtAdapterDestination, districtAdapterJourneyLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentDetailBinding.inflate(getLayoutInflater());

        arrayList = new ArrayList<>();

        initSpinnerAdapters();

        binding.spAdminSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistrictSource = binding.spAdminSource.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spAdminDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistrictDestination = binding.spAdminDestination.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spAdminJourneyLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedJourneyLoc = binding.spAdminJourneyLoc.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnAdminJourneyAdd.setOnClickListener(new View.OnClickListener() {
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
                chip.setText(selectedJourneyLoc);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.chGpAdminLocationChip.removeView(chip);
                    }
                });
                binding.chGpAdminLocationChip.addView(chip);
                binding.txAdminJourneyLocChips.setText("");
            }
        });

        binding.btnAdminJourneySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelections();
            }
        });
        return binding.getRoot();
    }

    private void showSelections()
    {
        int count = binding.chGpAdminLocationChip.getChildCount();

        String s = null;
        for(int i = 0; i < count; i++)
        {
            Chip chip = (Chip) binding.chGpAdminLocationChip.getChildAt(i);
            if(s == null)
            {
                    s = chip.getText().toString();
            }else{
                s += ", " + chip.getText().toString();
            }
        }
        Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
    }

    private void initSpinnerAdapters(){
        districtAdapterSource = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_source, R.layout.spinner_layout);
        binding.spAdminSource.setAdapter(districtAdapterSource);
        districtAdapterDestination = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_destination, R.layout.spinner_layout);
        binding.spAdminDestination.setAdapter(districtAdapterDestination);
        districtAdapterJourneyLoc = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_Journey_location, R.layout.spinner_layout);
        binding.spAdminJourneyLoc.setAdapter(districtAdapterJourneyLoc);
    }
}