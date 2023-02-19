package com.itcraftsolution.raido.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Models.AgentDetails;
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentUserHomeBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserHomeFragment extends Fragment {

    private FragmentUserHomeBinding binding;
    private SpfUserData spfUserData;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, agentDatabaseRef;
    private ProgressDialog dialog;
    private String selectedDistrictFrom = null, selectedDistrictTo = null;
    private ArrayAdapter<CharSequence> districtAdapterFrom, districtAdapterTo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(getLayoutInflater());

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("LoginDetails");
        agentDatabaseRef = firebaseDatabase.getReference("AgentRideDetails");
        spfUserData = new SpfUserData(requireContext());

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Loading Profile...");
        dialog.setCancelable(false);
        dialog.show();

        loadProfileData();

        initSpinnerAdapters();

        binding.btnSearchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedDistrictFrom.equals("From")){
                    Snackbar.make(binding.mainUserLayout,"Please Select Source City!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edUserFrom.requestFocus();
                }else if(selectedDistrictTo.equals("To")){
                    Snackbar.make(binding.mainUserLayout,"Please Select Destination City!!", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edUserTO.requestFocus();
                }else if(binding.edUserDate.getText().toString().length() < 8){
                    Snackbar.make(binding.mainUserLayout,"Click and Set the Date", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edUserDate.requestFocus();
                }else if(binding.edUserMember.getText().toString().isEmpty() ||Integer.parseInt(binding.edUserMember.getText().toString()) == 0 || Integer.parseInt(binding.edUserMember.getText().toString()) > 8){
                    Snackbar.make(binding.mainUserLayout,"Member must be between 1 to 8", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edUserMember.requestFocus();
                }else{
                    String fromCity = selectedDistrictFrom;
                    String toCity = selectedDistrictTo;
                    String date = binding.edUserDate.getText().toString();
                    String members = binding.edUserMember.getText().toString();
                    Toast.makeText(requireContext(), "Source city: "+ fromCity + "To city: "+ toCity + "date : "+date + "members: "+members, Toast.LENGTH_SHORT).show();
                    spfUserData.setSpfUserSearchRide(fromCity, toCity, date, members);
                    getParentFragmentManager().beginTransaction().replace(R.id.frUserMainContainer,
                            new UserDetailRideFragment()).addToBackStack(null).commit();
                }
            }
        });

        binding.spUserFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistrictFrom = binding.spUserFrom.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spUserTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistrictTo = binding.spUserTo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.edUserDate.setOnClickListener(new View.OnClickListener() {
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
                        binding.edUserDate.setText( i2 + "/" + monthDate + "/" + i);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        return binding.getRoot();
    }
    private void loadProfileData(){
        databaseReference.child("UserLoginDetails").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginDetails loginDetails = snapshot.getValue(LoginDetails.class);
                if(loginDetails != null){
                    Glide.with(requireContext()).load(loginDetails.getUserImage()).into(binding.igUserHomePic);
                    binding.txUserHomeName.setText(loginDetails.getUserName());

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
        binding.edUserDate.setText(currentDate);


    }

    private void initSpinnerAdapters(){
        districtAdapterFrom = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_from, R.layout.spinner_layout);
        binding.spUserFrom.setAdapter(districtAdapterFrom);
        districtAdapterTo = ArrayAdapter.createFromResource(requireContext(), R.array.array_gujarat_districts_to, R.layout.spinner_layout);
        binding.spUserTo.setAdapter(districtAdapterTo);
    }

    private void searchRideIntoFirebase(){
        FirebaseRecyclerOptions<AgentDetails> options = new FirebaseRecyclerOptions.Builder<AgentDetails>()
                .setQuery(agentDatabaseRef.child(auth.getCurrentUser().getUid()).orderByChild("journeySource").startAt(selectedDistrictFrom).endAt(selectedDistrictFrom + "\uf8ff"), AgentDetails.class)
                .build();
    }
}