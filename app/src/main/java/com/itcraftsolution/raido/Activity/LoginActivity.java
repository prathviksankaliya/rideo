package com.itcraftsolution.raido.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.itcraftsolution.raido.Fragments.SplashFragment;
import com.itcraftsolution.raido.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.frLoginContainer,
                new SplashFragment()).addToBackStack(null).commit();
    }
}