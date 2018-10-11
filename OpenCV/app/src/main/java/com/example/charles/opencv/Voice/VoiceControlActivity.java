package com.example.charles.opencv.Voice;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.charles.opencv.FeatureActivity.BirdBankActivity;
import com.example.charles.opencv.R;

import java.util.ArrayList;

/***
 * Activity for handling voice control input
 */
public class VoiceControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
    }

    /**
     * Receives the recognized speech from the user, validates and checks for matches, navigates accordingly
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3432) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> matches = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (matches.size() == 0) {
                    Toast.makeText(this, "No matches, please try again", Toast.LENGTH_LONG).show();
                } else {
                    String mostLikelyThingHeard = matches.get(0);
                    if (mostLikelyThingHeard.toUpperCase().equals("BANK")) {
                        startActivity(new Intent(this, BirdBankActivity.class));
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
