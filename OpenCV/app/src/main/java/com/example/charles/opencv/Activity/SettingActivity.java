package com.example.charles.opencv.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.charles.opencv.R;

/**
 * Shows the settings available to the user, this currently only displays the references for information
 * gathered in the database and images.
 */
public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
}
