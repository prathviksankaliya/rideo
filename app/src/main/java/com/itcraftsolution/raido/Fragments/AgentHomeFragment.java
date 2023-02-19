package com.itcraftsolution.raido.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentAgentHomeBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AgentHomeFragment extends Fragment {

    private FragmentAgentHomeBinding binding;
    private List<String> spTime = Arrays.asList("Hours ", "Minutes", "Days");
    private String time;
    private SpfUserData spfUserData;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgentHomeBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("LoginDetails");
        spfUserData = new SpfUserData(requireContext());

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Loading Profile...");
        dialog.setCancelable(false);
        dialog.show();
        loadProfileData();


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
                }else if(binding.edAdminHomeDate.getText().toString().length() < 8){
                    Snackbar.make(binding.agentHomeMainLayout,"Click and Set the Date", Snackbar.LENGTH_LONG)
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
                }
//                else if(binding.edAdminHours.getText().toString().isEmpty() || Integer.parseInt(binding.edAdminSeats.getText().toString()) == 0){
//                    Snackbar.make(binding.agentHomeMainLayout,"Please enter valid Journey Time", Snackbar.LENGTH_LONG)
//                            .setBackgroundTint(getResources().getColor(R.color.red))
//                            .setTextColor(getResources().getColor(R.color.white))
//                            .show();
//                    binding.edAdminHours.requestFocus();
//                }
                else{
                    String carName = binding.edAdminCarName.getText().toString();
                    String vehicleNumber = binding.edAdminVehicalNumber.getText().toString();
                    String phoneNumber = binding.edAdminNumber.getText().toString();
                    String date = binding.edAdminHomeDate.getText().toString();
                    String emptySeat = binding.edAdminSeats.getText().toString();
//                    binding.txResult.setText("car Name : " + carName +"\n" +
//                            "Vehical Number : " + vehicleNumber +"\n" +
//                            "Phone number : " + phoneNumber +"\n" +
//                            "date  : " + date +"\n" +
//                            "Empty Seats : " + emptySeat +"\n" +
//                            "total Journey : " + totalJourney +"\n" +
//                            "time : " + time +"\n");
                    spfUserData.setSpfAgentRideDetails(carName, vehicleNumber, phoneNumber, date, emptySeat, null,time, null, null, null, null, null, null, null);
                    getParentFragmentManager().beginTransaction().replace(R.id.frMainContainer, new AgentDetailFragment()).addToBackStack(null).commit();

                }

            }
        });



        return binding.getRoot();
    }
//    private void spinnerTime(){
//        ArrayAdapter<String> ad = new ArrayAdapter<String>(requireContext(), R.layout.spinner_layout, spTime);
//        binding.spAdminTime.setAdapter(ad);
//        binding.spAdminTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                time = spTime.get(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        String currentDate = simpleDateFormat.format(new Date());
//        binding.edAdminHomeDate.setText(currentDate);
//    }

    private void loadProfileData(){
        databaseReference.child("AgentLoginDetails").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginDetails loginDetails = snapshot.getValue(LoginDetails.class);
                if(loginDetails != null){
                    Glide.with(requireContext()).load(loginDetails.getUserImage()).into(binding.igAdminHomePic);
                    binding.txAdminHomeName.setText(loginDetails.getUserName());
                    binding.txAdminHomeRate.setText(String.valueOf(loginDetails.getUserRating()));

                    spfUserData.setSpfUserLoginDetails(loginDetails.getUserName(), loginDetails.getUserImage(),
                            loginDetails.getUserEmail(), loginDetails.getUserPhone(), loginDetails.getUserType(), loginDetails.getUserGender());
                            dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());
        binding.edAdminHomeDate.setText(currentDate);

//        spinnerTime();

    }
}