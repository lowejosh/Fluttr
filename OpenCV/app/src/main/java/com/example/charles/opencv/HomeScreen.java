package com.example.charles.opencv;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.charles.opencv.FeatureActivity.BirdBankActivity;
import com.example.charles.opencv.FeatureActivity.BirdFinderActivity;
import com.example.charles.opencv.FeatureActivity.SettingActivity;

import java.io.IOException;

/**
 * Main Menu Controls
 */
public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        updateImage((ImageView)findViewById(R.id.bird_ID_image), "Brain.png");
        updateImage((ImageView)findViewById(R.id.bird_finder_image), "Bird Finder.png");
        updateImage((ImageView)findViewById(R.id.bird_bank_image), "Bank 2.png");
        updateImage((ImageView)findViewById(R.id.bird_news_image), "News.png");
        updateImage((ImageView)findViewById(R.id.settings_image), "Settings 1.png");
        updateImage((ImageView)findViewById(R.id.achievement_image), "Achievements.png");
        updateImage((ImageView)findViewById(R.id.profile_image), "Profile.png");

        /*ViewFlipper slideShow = findViewById(R.id.slide_show);

        for (int birdID = 1; birdID < 6; birdID++) {
            Bird bird = new BirdFinderDatabase(this).getBird(birdID);
            ImageView ivBird = new ImageView(slideShow.getContext());
            ivBird.setImageBitmap(bird.getImage(this));
            slideShow.addView(ivBird);
        }

        slideShow.setFlipInterval(5000);
        slideShow.startFlipping();*/
    }

    private void updateImage(ImageView v, String filename) {
        //Update image for ImageView
        try {
            v.setImageBitmap(BitmapFactory.decodeStream(this.getAssets().open("icons/" + filename)));
        } catch (IOException | IllegalArgumentException unused) {
            Log.e("MainActivity", "Failed to load image: " + "icons/" + filename);
        }
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
