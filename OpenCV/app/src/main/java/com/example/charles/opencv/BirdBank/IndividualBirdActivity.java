package com.example.charles.opencv.BirdBank;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.charles.opencv.R;
import com.example.charles.opencv.Tables.Bird;

/**
 * Displays detailed information about the bird_finder on the app screen.
 */
public class IndividualBirdActivity extends AppCompatActivity {
    TextView mBirdName;
    TextView mBirdSeen;
    TextView mBirdMinSize;
    TextView mBirdMaxSize;
    TextView mBirdDescription;
    ImageView mBirdImage;
    LinearLayout mBirdSound;
    Bird bird;
    String date;
    MediaPlayer m;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bb_individual_bird_new);

        m = new MediaPlayer();

        mBirdName = findViewById(R.id.bird_name);
        mBirdSeen = findViewById(R.id.identification_date);
        mBirdMinSize = findViewById(R.id.min_size);
        mBirdMaxSize = findViewById(R.id.max_size);
        mBirdImage = findViewById(R.id.bird_image);
        mBirdDescription = findViewById(R.id.bird_description);
        mBirdSound = findViewById(R.id.sound_layout);

        updateBirdPage();
        loadSound();
    }

    /**
     * Updates the bird_finder page with stored information about the bird_finder
     */
    private void updateBirdPage() {
        bird = ListBirdAdapter.mBirdClicked;
        date = ListBirdAdapter.mDateOfBirdClicked;

        mBirdName.setText(bird.getName());
        mBirdSeen.setText("Identified on " + date);
        mBirdMinSize.setText("Min size " + bird.getMinSize() + "cm");
        mBirdMaxSize.setText("Max Size " + bird.getMaxSize() + "cm");
        mBirdImage.setImageBitmap(bird.getImage(this));

        if (bird.getDescription() == null || bird.getDescription().trim() == "") {
            mBirdDescription.setText("No description found");
            mBirdDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            mBirdDescription.setText(bird.getDescription());
            mBirdDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    public void goBack(View v) {
        super.finish();
    }

    private void loadSound() {
        AssetFileDescriptor descriptor = bird.getSound(this);

        if (descriptor == null) {
            mBirdSound.setVisibility(View.GONE);
            return;
        }

        try {
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(false);
        } catch (Exception e) {
            Log.e("loadSound", "Failure to play bird_finder call");
            e.printStackTrace();
        }
    }

    /**
     * Play bird_finder song on click
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
}