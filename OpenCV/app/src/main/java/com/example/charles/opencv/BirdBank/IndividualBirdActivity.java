package com.example.charles.opencv.BirdBank;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charles.opencv.R;
import com.example.charles.opencv.Tables.Bird;

import java.io.IOException;

/**
 * Displays detailed information about the bird on the app screen.
 */
public class IndividualBirdActivity extends AppCompatActivity {
    TextView mBirdName;
    TextView mBirdSeen;
    TextView mBirdMinSize;
    TextView mBirdMaxSize;
    ImageView mBirdImage;
    Bird bird;
    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bb_individual_bird_new);

        updateImage((ImageView)findViewById(R.id.backButton), "Back Button.png");
        updateImage((ImageView)findViewById(R.id.bird_call), "Sound.png");

        mBirdName = (TextView) findViewById(R.id.bird_name);
        mBirdSeen = (TextView) findViewById(R.id.identification_date);
        mBirdMinSize = (TextView) findViewById(R.id.min_size);
        mBirdMaxSize = (TextView) findViewById(R.id.max_size);
        mBirdImage = (ImageView) findViewById(R.id.bird_image);

        updateBirdPage();
    }

    /**
     * Updates the bird page with stored information about the bird
     */
    private void updateBirdPage() {
        bird = ListBirdAdapter.mBirdClicked;
        date = ListBirdAdapter.mDateOfBirdClicked;

        mBirdName.setText(bird.getName());
        mBirdSeen.setText("Identified on " + date + "\n");
        mBirdMinSize.setText("Min size " + bird.getMinSize());
        mBirdMaxSize.setText("Max Size " + bird.getMaxSize());
        mBirdImage.setImageBitmap(bird.getImage(this));
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