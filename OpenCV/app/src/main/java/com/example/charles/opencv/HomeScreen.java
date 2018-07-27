package com.example.charles.opencv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.charles.opencv.Database.BirdFinderDatabase;
import com.example.charles.opencv.FeatureActivity.BirdBankActivity;
import com.example.charles.opencv.FeatureActivity.BirdFinderActivity;
import com.example.charles.opencv.FeatureActivity.SettingActivity;
import com.example.charles.opencv.Tables.Bird;

/**
 * Main Menu Controls
 */
public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ViewFlipper slideShow = findViewById(R.id.slide_show);

        for (int birdID = 1; birdID < 6; birdID++) {
            Bird bird = new BirdFinderDatabase(this).getBird(birdID);
            ImageView ivBird = new ImageView(slideShow.getContext());
            ivBird.setImageBitmap(bird.getBirdImage(this));
            slideShow.addView(ivBird);
        }

        slideShow.setFlipInterval(5000);
        slideShow.startFlipping();

        // Switch to camera activity upon click of button
        /*Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                startActivity(new Intent(HomeScreen.this, MainActivity.class));
            }
        });*/ // currently removed
    }

    /**
     * On Button Press, Change Screen to Twenty Questions
     * @param v Attached View Object (Unused)
     */
    public void finderOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, BirdFinderActivity.class));
    }

    /**
     * On Button Press, Change Screen to Search AI
     * @param v Attached View Object (Unused)
     */
    public void identifierOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, MainActivity.class));
    }

    /**
     * On Button Press, Change Screen to Bird Bank
     * @param v Attached View Object (Unused)
     */
    public void bankOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, BirdBankActivity.class));
    }

    /**
     * On Button Press, Change Screen to Settings
     * @param v Attached View Object (Unused)
     */
    public void settingOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, SettingActivity.class));
    }
}
