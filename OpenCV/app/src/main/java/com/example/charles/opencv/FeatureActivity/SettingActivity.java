package com.example.charles.opencv.FeatureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    /**
     * Sends the user to ReferenceActivity.Class
     * @param v View
     */
    public void referencesOnClick(View v) {
        startActivity(new Intent(SettingActivity.this, ReferenceActivity.class));
    }

    /**
     * Sends the user to ResetActivity.Class
     * @param v View
     */
    public void resetBirdBankOnClick(View v) {
        startActivity(new Intent(SettingActivity.this, ResetActivity.class));
    }

    /**
     * Back on click function for the "GO BACK" button
     *
     * @param v View of the button
     */
    public void homeOnClick(View v) {
        super.finish();
    }
}
