package com.example.charles.opencv.FeatureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.HomeScreen;
import com.example.charles.opencv.R;
import com.example.charles.opencv.Settings.ReferenceActivity;
import com.example.charles.opencv.Settings.ResetActivity;

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

    public void referencesOnClick(View v) {
        startActivity(new Intent(SettingActivity.this, ReferenceActivity.class));
    }

    public void confirmOnClick(View v) {
        startActivity(new Intent(SettingActivity.this, ResetActivity.class));
    }
    public void homeOnClick(View v) {
        super.finish();
    }
}
