package com.example.charles.opencv.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.R;

public class ResetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_confirm);
    }

    public void resetBirdBankOnClick(View v) {
        BirdBankDatabase db = new BirdBankDatabase(this);

        db.clearBirdsSeen();
        Toast.makeText(getApplicationContext(), "Bird Bank Reset", Toast.LENGTH_SHORT).show();
        super.finish();
    }

    public void backOnClick(View v) {
        super.finish();
    }
}
