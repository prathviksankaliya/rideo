package com.itcraftsolution.raido.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.itcraftsolution.raido.Activity.MainActivity;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentLoginProfileBinding;
import com.itcraftsolution.raido.spf.SpfUserData;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class LoginProfileFragment extends Fragment {

    private FragmentLoginProfileBinding binding;
    private ActivityResultLauncher<String> getImageLauncher;
    private Uri photoUri;
    private static final int PERMISSION_ID = 44;
    private String destPath, encodedImageString;
    private String gender = "Male";
    private Bitmap bitmap;
    private boolean checkImage = false;
    private GoogleSignInAccount account;
    private SpfUserData spfUserData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginProfileBinding.inflate(getLayoutInflater());
        spfUserData = new SpfUserData(requireContext());
        displayLoginDetails();

        binding.btnLoginSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.requireNonNull(binding.edLoginName.getText()).toString().length() <= 2)
                {
                    Snackbar.make(binding.loginProfileMainLayout,"Name must be Minimum 3 characters", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edLoginName.requestFocus();
                }else if(!checkImage || binding.igLoginPic.getDrawable() == null)
                {
                    Snackbar.make(binding.loginProfileMainLayout,"Please set your profile picture", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.igLoginPic.requestFocus();
                }else if(Objects.requireNonNull(binding.edLoginPhoneNumber.getText()).toString().length() != 10)
                {
                    Snackbar.make(binding.loginProfileMainLayout,"Please 10 digit Number", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.edLoginPhoneNumber.requestFocus();
                }else if(!validEmail(Objects.requireNonNull(binding.edLoginEmail.getText()).toString()))
                {
                    Snackbar.make(binding.loginProfileMainLayout,"Please Enter Valid Email", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    binding.igLoginPic.requestFocus();
                }else {
                    String userName = binding.edLoginName.getText().toString();
                    String userPhone = binding.edLoginPhoneNumber.getText().toString();
                    String userEmail = binding.edLoginEmail.getText().toString();
                    spfUserData.setSpfUserLoginDetails(userName, encodedImageString, userEmail, userPhone, gender);
//                    getParentFragmentManager().beginTransaction().replace(R.id.frLoginContainer, new demoFragment()).commit();

                }

//                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        });

        binding.loginRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkRadioBtnId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = radioGroup.findViewById(checkRadioBtnId);
                if(radioButton.isChecked())
                {
                    gender = radioButton.getText().toString();
                }
            }
        });

        binding.igLoginEditPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()) {
                    getImageLauncher.launch("image/*");
                }else{
                    requestStoragePermission();
                }
            }
        });

        getImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result.getPath() != null) {
                    photoUri = result;
                    destPath = UUID.randomUUID().toString() + ".png";

                    UCrop.Options options = new UCrop.Options();

                    options.setCompressionFormat(Bitmap.CompressFormat.PNG);
                    options.setCircleDimmedLayer(true);
                    options.setCompressionQuality(90);


                    UCrop.of(photoUri, Uri.fromFile(new File(requireContext().getCacheDir(), destPath)))
                            .withOptions(options)
                            .withAspectRatio(0, 0)
                            .useSourceImageAspectRatio()
                            .withMaxResultSize(1080, 720)
                            .start(requireContext(), LoginProfileFragment.this);

                    try {
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(photoUri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        encodeBitmapImageString(bitmap);


                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return binding.getRoot();
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_ID);
    }

    private void encodeBitmapImageString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        binding.igLoginPic.setImageBitmap(bitmap);
        byte[] bytesOfImage =byteArrayOutputStream.toByteArray();
        encodedImageString = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
        checkImage = true;
    }

    private boolean validEmail(String email)
    {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void displayLoginDetails()
    {
        GoogleSignIn.getLastSignedInAccount(requireContext());
        account = GoogleSignIn.getLastSignedInAccount(requireContext());

        String loginPhoneNumber = spfUserData.getSpfUserLoginDetails().getString("userPhone", null);
        if(account != null)
        {
            binding.edLoginName.setText(account.getDisplayName());
            binding.edLoginEmail.setText(account.getEmail());
            binding.textInputLayout8.setFocusable(false);
        }else{
            binding.edLoginPhoneNumber.setText(loginPhoneNumber);
            binding.textInputLayout.setFocusable(false);
        }
    }
    private void encodeImageStringBitmap(String encodeImageString)
    {
        byte[] encodeBytes = android.util.Base64.decode(encodeImageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeBytes, 0, encodeBytes.length);
        binding.igLoginPic.setImageBitmap(bitmap);
    }
}