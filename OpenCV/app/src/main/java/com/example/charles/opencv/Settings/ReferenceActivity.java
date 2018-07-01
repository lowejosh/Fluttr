package com.example.charles.opencv.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.charles.opencv.R;

/**
 * Shows the references used to create this project.
 */
public class ReferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_references);
    }

    /**
     * Back on click function for the "GO BACK" button
     *
     * @param v View of the button
     */
    public void backOnClick(View v) {
        super.finish();
    }
}
