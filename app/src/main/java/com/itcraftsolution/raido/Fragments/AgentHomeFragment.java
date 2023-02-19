package com.itcraftsolution.raido.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentAgentHomeBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.util.Calendar;

public class AgentHomeFragment extends Fragment {

    private FragmentAgentHomeBinding binding;
    private String [] spTime = {"Minutes ", "Hours", "Days"};
    private String time;
    private SpfUserData spfUserData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentHomeBinding.inflate(getLayoutInflater());

        spinnerTime();
        spfUserData = new SpfUserData(requireContext());
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
                if(binding.edAdminCarName.getText().toString().length() <= 2){
                    Snackbar.make(binding.agentHomeMainLayout,"Car Name must be Minimum 2 characters", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edAdminCarName.requestFocus();
                }else if(binding.edAdminVehicalNumber.getText().toString().length() <= 8){
                    Snackbar.make(binding.agentHomeMainLayout,"Car Number must be Minimum 10 characters", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edAdminVehicalNumber.requestFocus();
                }else if(binding.edAdminNumber.getText().toString().length() != 10){
                    Snackbar.make(binding.agentHomeMainLayout,"Phone Number must be Minimum 10 digits", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edAdminNumber.requestFocus();
                }else if(binding.edAdminHomeDate.getText().toString().isEmpty()){
                    Snackbar.make(binding.agentHomeMainLayout,"Set the Date", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edAdminHomeDate.requestFocus();
                }else if(binding.edAdminSeats.getText().toString().isEmpty() || Integer.parseInt(binding.edAdminSeats.getText().toString()) == 0 || Integer.parseInt(binding.edAdminSeats.getText().toString()) > 8){
                    Snackbar.make(binding.agentHomeMainLayout,"Empty Seat must be between 1 to 8 seats", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edAdminSeats.requestFocus();
                }else if(binding.edAdminHours.getText().toString().isEmpty() || Integer.parseInt(binding.edAdminSeats.getText().toString()) == 0){
                    Snackbar.make(binding.agentHomeMainLayout,"Please enter valid Journey Time", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edAdminHours.requestFocus();
                }else{
                    String carName = binding.edAdminCarName.getText().toString();
                    String vehicleNumber = binding.edAdminVehicalNumber.getText().toString();
                    String phoneNumber = binding.edAdminNumber.getText().toString();
                    String date = binding.edAdminHomeDate.getText().toString();
                    String emptySeat = binding.edAdminSeats.getText().toString();
                    String totalJourney = binding.edAdminHours.getText().toString();
//                    binding.txResult.setText("car Name : " + carName +"\n" +
//                            "Vehical Number : " + vehicleNumber +"\n" +
//                            "Phone number : " + phoneNumber +"\n" +
//                            "date  : " + date +"\n" +
//                            "Empty Seats : " + emptySeat +"\n" +
//                            "total Journey : " + totalJourney +"\n" +
//                            "time : " + time +"\n");
                    spfUserData.setSpfAgentRideDetails(carName, vehicleNumber, phoneNumber, date, emptySeat, totalJourney, time);
                    getParentFragmentManager().beginTransaction().replace(R.id.frMainContainer, new AgentDetailFragment()).addToBackStack(null).commit();

                }

            }
        });



        return binding.getRoot();
    }
    private void spinnerTime(){
        ArrayAdapter ad = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spTime);
        binding.spAdminTime.setAdapter(ad);
        binding.spAdminTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(requireContext(), ""+ spTime[i], Toast.LENGTH_SHORT).show();
                time = spTime[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}