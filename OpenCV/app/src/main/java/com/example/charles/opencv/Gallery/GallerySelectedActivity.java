package com.example.charles.opencv.Gallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.charles.opencv.R;

public class GallerySelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_selected);

        ImageView imageview = findViewById(R.id.imageView1);
        imageview.setImageBitmap(GalleryActivity.imgBmp);
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
