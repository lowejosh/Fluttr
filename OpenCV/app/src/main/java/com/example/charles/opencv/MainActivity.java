package com.example.charles.opencv;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.charles.opencv.Gallery.GalleryActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_REQUEST_CODE=1000;
    Button takePhoto;
    Button uploadPhoto;
    Button viewGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto = findViewById(R.id.clickimage);
        uploadPhoto = findViewById(R.id.uploadphoto);
        viewGallery = findViewById(R.id.viewgallery);
        // start camera
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfCameraAvailable()) {
                    takePhoto();
                } else {
                    Toast.makeText(MainActivity.this,"Camera Not Available",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // start photo picker intent
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });
        // start view gallery activity
        viewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });
    }

    //method to click image
    private void takePhoto()
    {
        // camera
        Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyddMM_HHmmss").format(new Date());

        // folder
        File fluttrFolder = new File(Environment.getExternalStorageDirectory(), "Fluttr");
        fluttrFolder.mkdirs();

        // image
        File img = new File(fluttrFolder, "Fluttr_" + timeStamp + ".png");  // create image
        Uri uriSavedImage = FileProvider.getUriForFile(                          // get uri
                MainActivity.this,
                "com.example.charles.opencv.provider",
                img);
        imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);              // save image
        startActivityForResult(imgIntent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    // checks if camera is available
    public boolean checkIfCameraAvailable() {
        return MainActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    // gets the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if we are capturing an image
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            // success
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Image Captured Successfully", Toast.LENGTH_SHORT).show();
            // failure
            } else {
                Toast.makeText(MainActivity.this, "Error Capturing Image! Please Retry", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}