package com.itcraftsolution.raido.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentAgentDetailBinding;
import com.itcraftsolution.raido.databinding.FragmentAgentHomeBinding;

import java.util.Calendar;

public class AgentHomeFragment extends Fragment {

    private FragmentAgentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentHomeBinding.inflate(getLayoutInflater());

        binding.edAdminHomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int monthDate = i1 + 1;
                        binding.edAdminHomeDate.setText( i2 + "/" + monthDate + "/" + i);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        binding.btnAdminHomeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String carName = binding.AdminCarName.getText().toString();
//                int vehicleNumber = Integer.parseInt(binding.AdminVehicalNumber.getText().toString());
//                int phoneNumber = Integer.parseInt(binding.AdminNumber.getText().toString());
//                String date = binding.AdminDate.getText().toString();
//                int emptySeat = Integer.parseInt(binding.AdminSeats.getText().toString());
//                int totalJourney = Integer.parseInt(binding.AdminHours.getText().toString());
                getParentFragmentManager().beginTransaction().replace(R.id.frMainContainer, new AgentDetailFragment()).addToBackStack(null).commit();
            }
        });



        return binding.getRoot();
    }
}