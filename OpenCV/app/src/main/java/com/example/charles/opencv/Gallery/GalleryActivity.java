package com.example.charles.opencv.Gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.charles.opencv.FeatureActivity.AIActivity;
import com.example.charles.opencv.R;

import java.io.File;


public class GalleryActivity extends Activity
{
    private String[] filePaths;
    private File[] files;
    GridView gridView;
    GalleryAdapter galleryAdapter;
    File file;
    public static Bitmap imgBmp = null;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_picture);
        // if there is no SD card
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD Card Not Found", Toast.LENGTH_LONG).show();
        }
        // if there is an SD card
        else
        {
            // Locate the fluttr folder
            file = new File(Environment.getExternalStorageDirectory().getPath() + "/Fluttr");
        }

        // if the directory exists
        if (file.isDirectory())
        {
            // list the files
            files = file.listFiles();

            // get file paths if they exist
            if (files == null) {
                Toast.makeText(this, "No Images Found", Toast.LENGTH_LONG).show();
                return;
            } else {
                filePaths = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    filePaths[i] = files[i].getAbsolutePath();
                }
            }

        } else {
            // make the directory
            File fluttrFolder = new File(Environment.getExternalStorageDirectory(), "Fluttr");
            fluttrFolder.mkdirs();
            Toast.makeText(this, "No Images Found", Toast.LENGTH_LONG).show();
            return;
        }
        gridView = findViewById(R.id.gridview);
        galleryAdapter = new GalleryAdapter(this, filePaths);
        gridView.setAdapter(galleryAdapter);

        // go to full screen version on click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view,
                                     int position, long id)
            {
                int width = AIActivity.screenWidth;
                int height = AIActivity.screenHeight;

                BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
                bmpOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePaths[position],bmpOptions);

                // current width and height
                int cHeight = bmpOptions.outHeight;
                int cWidth = bmpOptions.outWidth;

                // scale it
                int sampleSize = 1;
                if (cHeight > height || cWidth > width)
                {
                    if (cWidth > cHeight)
                        sampleSize = Math.round((float)cHeight / (float)height);
                    else
                        sampleSize = Math.round((float)cWidth / (float)width);
                }

                // create bmp
                bmpOptions.inSampleSize = sampleSize;
                bmpOptions.inJustDecodeBounds = false;
                imgBmp = BitmapFactory.decodeFile(filePaths[position], bmpOptions);

                // go to full screen activity
                startActivity(new Intent(GalleryActivity.this, GallerySelectedActivity.class));
            }
        });

    }
}