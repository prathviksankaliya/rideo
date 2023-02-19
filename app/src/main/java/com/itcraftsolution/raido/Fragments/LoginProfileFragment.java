package com.itcraftsolution.raido.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itcraftsolution.raido.Activity.AgentRideActivity;
import com.itcraftsolution.raido.Activity.MainActivity;
import com.itcraftsolution.raido.Activity.UserRideActivity;
import com.itcraftsolution.raido.Models.LoginDetails;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.FragmentLoginProfileBinding;
import com.itcraftsolution.raido.spf.SpfUserData;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class LoginProfileFragment extends Fragment {

    private FragmentLoginProfileBinding binding;
    private ActivityResultLauncher<String> getImageLauncher;
    private Uri photoUri;
    private static final int PERMISSION_ID = 44;
    private String destPath, encodedImageString, userName, userEmail, userPhone, userType;
    private String gender = "Male";
    private Bitmap bitmap;
    private boolean checkImage = false;
    private GoogleSignInAccount account;
    private SpfUserData spfUserData;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LoginDetails loginDetails;
    private FirebaseAuth auth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressDialog dialog;
    private String spLoginUser[] = {"Car Rider", "Car Agent"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginProfileBinding.inflate(getLayoutInflater());
        spfUserData = new SpfUserData(requireContext());

        spinnerProfile();

        displayLoginDetails();
        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("UserImages");
        auth = FirebaseAuth.getInstance();
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
                    dialog.show();
                    userName = binding.edLoginName.getText().toString();
                    userPhone = binding.edLoginPhoneNumber.getText().toString();
                    userEmail = binding.edLoginEmail.getText().toString();
                    spfUserData.setSpfUserLoginDetails(userName, encodedImageString, userEmail, userPhone, userType,gender);
                    addImageToStorage();
                }
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
                        bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), photoUri);
//                        encodeBitmapImage(bitmap);
                        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
                        binding.igLoginPic.setImageBitmap(bitmap);
                        checkImage = true;
                    } catch (IOException e) {
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

    private void encodeBitmapImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        binding.igLoginPic.setImageBitmap(bitmap);
        byte [] bytesOfImage =byteArrayOutputStream.toByteArray();
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
    private void addImageToStorage()
    {
        storageReference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .putFile(photoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        storageReference.child(auth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                loginDetails = new LoginDetails(userName, String.valueOf(uri), userEmail, userPhone,userType,0, gender);
                                addDataToFirebaseDatabase(userType);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "Image download fail: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                        dialog.setMessage("Uploaded: " + (int)percent + " %");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Storage: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void addDataToFirebaseDatabase(String userType)
    {

        if(Objects.equals(userType, "Car Agent")){
            firebaseDatabase.getReference("LoginDetails").child("AgentLoginDetails").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(loginDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(requireContext(), "Login Successfully!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        startActivity(new Intent(requireContext(), AgentRideActivity.class));
                        requireActivity().finishAffinity();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "Database: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }else{
            firebaseDatabase.getReference("LoginDetails").child("UserLoginDetails").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(loginDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(requireContext(), "Login Successfully!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        startActivity(new Intent(requireContext(), UserRideActivity.class));
                        requireActivity().finishAffinity();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "Database: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }

    }

    private void spinnerProfile(){
        ArrayAdapter adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spLoginUser);
        binding.spUserProfile.setAdapter(adapter);
        binding.spUserProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userType = spLoginUser[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}