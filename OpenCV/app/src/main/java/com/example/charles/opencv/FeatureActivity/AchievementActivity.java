package com.example.charles.opencv.FeatureActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.charles.opencv.R;

import java.io.IOException;

public class AchievementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateImage((ImageView)findViewById(R.id.backButton), "Back Button.png");
    }

    public void goBack(View v) {
        super.finish();
    }

    /**
     * Updates the image view with a file stored in assets/icons
     * @param v ImageView
     * @param filename Filename and extension of the icon
     */
    private void updateImage(ImageView v, String filename) {
        //Update image for ImageView
        try {
            v.setImageBitmap(BitmapFactory.decodeStream(this.getAssets().open("icons/" + filename)));
        } catch (IOException | IllegalArgumentException unused) {
            Log.e("AIActivity", "Failed to load image: " + "icons/" + filename);
        }
    }
}
