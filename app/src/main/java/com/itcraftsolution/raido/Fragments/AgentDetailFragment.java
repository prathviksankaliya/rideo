package com.itcraftsolution.raido.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itcraftsolution.raido.Models.AgentDetails;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentAgentDetailBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class
AgentDetailFragment extends Fragment {

    private FragmentAgentDetailBinding binding;
    private ArrayList<String> arrayList;
    private Dialog dialog;
    private SpfUserData spfUserData;
    private String selectedDistrictSource = null, selectedDistrictDestination = null, selectedJourneyLoc = null;
    private ArrayAdapter<CharSequence> districtAdapterSource, districtAdapterDestination, districtAdapterJourneyLoc;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private AgentDetails agentDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentDetailBinding.inflate(getLayoutInflater());

        arrayList = new ArrayList<>();

        initSpinnerAdapters();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("AgentRideDetails");
        spfUserData = new SpfUserData(requireContext());

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

        binding.txAdminRideArrivalTIme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        binding.txAdminRideArrivalTIme.setText(timePicker.getHour() + " : " + timePicker.getMinute());
                    }
                }, hour, min, false);

                timePickerDialog.show();
            }
        });

        binding.txAdminRideDepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        binding.txAdminRideDepTime.setText(i + " : " + i1);

                    }
                }, hour, min, false);

                timePickerDialog.show();
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

                if(selectedDistrictSource.equals("Source")){
                    Snackbar.make(binding.mainAgentDetails,"Please Select Source City!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                        binding.txAdminJourneySource.requestFocus();
                }else if(selectedDistrictDestination.equals("Destination")){
                    Snackbar.make(binding.mainAgentDetails,"Please Select Destination City!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.txAdminJourneyDestination.requestFocus();
                }else if( binding.txAdminRidePrice.getText().toString().isEmpty() || Integer.parseInt(binding.txAdminRidePrice.getText().toString()) <= 0 || Integer.parseInt(binding.txAdminRidePrice.getText().toString()) >= 5000){
                    Snackbar.make(binding.mainAgentDetails,"Please Enter Price between 0 to 5000!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.txAdminRidePrice.requestFocus();
                }else if(binding.txAdminRideArrivalTIme.getText().toString().length() < 3){
                    Snackbar.make(binding.txAdminRideArrivalTIme,"Please set Arrival Time!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.txAdminRideArrivalTIme.requestFocus();
                }else if(binding.txAdminRideDepTime.getText().toString().length() < 3){
                    Snackbar.make(binding.txAdminRideDepTime,"Please set Departure Time!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.txAdminRideDepTime.requestFocus();
                }else{
                    String carName = spfUserData.getSpfAgentRideDetails().getString("agentCarName", null);
                    String vehicalNumber = spfUserData.getSpfAgentRideDetails().getString("agentVehicalNumber", null);
                    String phoneNumber = spfUserData.getSpfAgentRideDetails().getString("agentPhoneNumber", null);
                    String journeyDate = spfUserData.getSpfAgentRideDetails().getString("agentDate", null);
                    String emptySeats = spfUserData.getSpfAgentRideDetails().getString("agentEmptySeats", null);
                    String time = spfUserData.getSpfAgentRideDetails().getString("agentTime", null);
                    String ridePrice = binding.txAdminRidePrice.getText().toString();
                    String arrivalTime = binding.txAdminRideArrivalTIme.getText().toString();
                    String depTime = binding.txAdminRideDepTime.getText().toString();
                    String totalJourney = totalJourneyHours(arrivalTime, depTime);
                    String journeyLocs = showSelections();
                    spfUserData.setSpfAgentRideDetails(carName, vehicalNumber, phoneNumber, journeyDate, emptySeats, totalJourney, time,
                            selectedDistrictSource, selectedDistrictDestination,arrivalTime, depTime,  ridePrice, journeyLocs, Objects.requireNonNull(auth.getCurrentUser()).getUid());
                    agentDetails = new AgentDetails(carName, vehicalNumber, phoneNumber, journeyDate, emptySeats, totalJourney, time,
                            selectedDistrictSource, selectedDistrictDestination,arrivalTime, depTime,ridePrice, journeyLocs, Objects.requireNonNull(auth.getCurrentUser()).getUid());
                    addDataIntoFirebaseDatabase();
                }

            }
        });
        return binding.getRoot();
    }

    private String showSelections()
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
        return s;
    }

    private void initSpinnerAdapters(){
        districtAdapterSource = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_source, R.layout.spinner_layout);
        binding.spAdminSource.setAdapter(districtAdapterSource);
        districtAdapterDestination = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_destination, R.layout.spinner_layout);
        binding.spAdminDestination.setAdapter(districtAdapterDestination);
        districtAdapterJourneyLoc = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_Journey_location, R.layout.spinner_layout);
        binding.spAdminJourneyLoc.setAdapter(districtAdapterJourneyLoc);
        selectedJourneyLoc = null;
        selectedDistrictDestination = null;
        selectedDistrictSource = null;
    }

//    roots --
    private void addDataIntoFirebaseDatabase(){
        databaseReference.child("Roots").child(selectedDistrictSource + "_" + selectedDistrictDestination).child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(agentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(requireContext(), "Journey Added", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction().replace(R.id.frMainContainer, new AgentHomeFragment()).commit();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String totalJourneyHours(String arrival, String dep){
        Date date1, date2;
        int days, hours, min;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh : mm");

        try {
             date1 = simpleDateFormat.parse(arrival);
             date2 = simpleDateFormat.parse(dep);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        long difference = date2.getTime() - date1.getTime();
        days = (int) (difference / (1000*60*60*24));
        hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        hours = (hours < 0 ? -hours : hours);
        if(min < 0){
            hours = 23 - hours;
            min = 60 - Math.abs(min);
        }
        return hours +" : "+ min;

    }
}