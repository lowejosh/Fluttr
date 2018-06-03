package com.example.charles.opencv.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;

public class IndividualBirdActivity extends AppCompatActivity {
    TextView mBirdName;
    TextView mBirdSeen;
    TextView mBirdMinSize;
    TextView mBirdMaxSize;
    ImageView mBirdImage;
    Bird bird;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.individual_bird);

        mBirdName = (TextView) findViewById(R.id.textView6);
        mBirdSeen = (TextView) findViewById(R.id.textView5);
        mBirdMinSize = (TextView) findViewById(R.id.textView3);
        mBirdMaxSize = (TextView) findViewById(R.id.textView2);
        mBirdImage = (ImageView) findViewById(R.id.bird_image);

        updateBirdPage ();
    }

    private void updateBirdPage() {
        bird = BirdBankActivity.mBirdClicked;

        mBirdName.setText(bird.getName());
        mBirdSeen.setText("Seen on dd/mm/yyyy (placeholder");
        mBirdMinSize.setText("Min size: " + bird.getMinSize());
        mBirdMaxSize.setText("Max Size: " + bird.getMaxSize());
        mBirdImage.setImageBitmap(bird.getBirdImage(this));
    }
}