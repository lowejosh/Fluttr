package com.example.charles.opencv.Voice;

import android.annotation.TargetApi;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.charles.opencv.FeatureActivity.BirdBankActivity;
import com.example.charles.opencv.HomeScreen;
import com.example.charles.opencv.R;

import java.util.ArrayList;

public class VoiceControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 3432){
            if (resultCode == RESULT_OK){
                ArrayList<String> matches = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (matches.size() == 0) {
                    // didn't hear anything
                } else {
                    String mostLikelyThingHeard = matches.get(0);
                    // toUpperCase() used to make string comparison equal
                    if(mostLikelyThingHeard.toUpperCase().equals("BANK")){
                        startActivity(new Intent(this, BirdBankActivity.class));
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
