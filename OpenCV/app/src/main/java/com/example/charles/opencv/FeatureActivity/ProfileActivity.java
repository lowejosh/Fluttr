package com.example.charles.opencv.FeatureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.charles.opencv.R;
import com.example.charles.opencv.Profile.LogInActivity;
import com.example.charles.opencv.Profile.SignUpActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
    }

    public void logInOnClick(View v) {
        startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
    }

    public void signUpOnClick(View v) {
        startActivity(new Intent(ProfileActivity.this, SignUpActivity.class));
    }

    public void homeOnClick(View v) {
        super.finish();
    }

}
