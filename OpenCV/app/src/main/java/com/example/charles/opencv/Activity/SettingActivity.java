package com.example.charles.opencv.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.charles.opencv.Database.Database;
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

    public void resetBirdBankOnClick(View v) {
        Database db = new Database(this);

        db.clearBirdsSeen();
        Toast.makeText(getApplicationContext(), "Bird Bank Reset", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.settings);
    }

    public void referencesOnClick(View v) {
        setContentView(R.layout.set_references);
    }

    public void confirmOnClick(View v) {
        setContentView(R.layout.set_confirm);
    }

    public void backOnClick(View v) {
        setContentView(R.layout.settings);
    }

    public void homeOnClick(View v) {
        super.finish();
    }
}
