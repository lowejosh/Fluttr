package com.example.charles.opencv.BirdBank;

import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
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
    TextView mBirdDescription;
    ImageView mBirdImage;
    ImageView mBirdSound;
    Bird bird;
    String date;
    MediaPlayer m;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bb_individual_bird_new);

        updateImage((ImageView)findViewById(R.id.backButton), "Back Button.png");
        updateImage((ImageView)findViewById(R.id.bird_call), "Sound.png");

        m = new MediaPlayer();
        loadSound();

        mBirdName = findViewById(R.id.bird_name);
        mBirdSeen = findViewById(R.id.identification_date);
        mBirdMinSize = findViewById(R.id.min_size);
        mBirdMaxSize = findViewById(R.id.max_size);
        mBirdImage = findViewById(R.id.bird_image);
        mBirdDescription = findViewById(R.id.bird_description);
        mBirdSound = findViewById(R.id.bird_call);

        updateBirdPage();
    }

    /**
     * Updates the bird page with stored information about the bird
     */
    private void updateBirdPage() {
        bird = ListBirdAdapter.mBirdClicked;
        date = ListBirdAdapter.mDateOfBirdClicked;

        mBirdName.setText(bird.getName());
        mBirdSeen.setText("Identified on " + date);
        mBirdMinSize.setText("Min size " + bird.getMinSize());
        mBirdMaxSize.setText("Max Size " + bird.getMaxSize());
        mBirdImage.setImageBitmap(bird.getImage(this));
        mBirdDescription.setText(bird.getDescription());
    }

    public void goBack(View v) {
        super.finish();
    }

    private void loadSound() {
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("sound/Australasian Shoveler.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Play bird song on click
     * @param v
     */
    public void playSound(View v) {
        mBirdSound.setOnClickListener(null);

        if (m.isPlaying()) {
            m.stop();
        }

        m.start();

        mBirdSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(v);
            }
        });
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