package com.example.charles.opencv.FeatureActivity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.charles.opencv.AI.AI;
import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.Gallery.GalleryActivity;
import com.example.charles.opencv.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AIActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_REQUEST_CODE=1000;
    private final int SELECT_PHOTO = 1;
    Button takePhoto;
    Button uploadPhoto;
    Button viewGallery;
    File img;

    public static int screenHeight;
    public static int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto = findViewById(R.id.clickimage);
        uploadPhoto = findViewById(R.id.uploadphoto);
        viewGallery = findViewById(R.id.viewgallery);

        // for getting screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // start camera
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfCameraAvailable()) {


                    // permissions
                    String storagePerm = "android.permission.WRITE_EXTERNAL_STORAGE";
                    String cameraPerm = "android.permission.CAMERA";
                    int checkVal = getApplicationContext().checkCallingOrSelfPermission(storagePerm);
                    int checkVal2 = getApplicationContext().checkCallingOrSelfPermission(cameraPerm);
                    if (checkVal == PackageManager.PERMISSION_GRANTED && checkVal2 == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        Toast.makeText(AIActivity.this,"Storage and Camera Permission Required",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AIActivity.this,"Camera Not Available",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // start photo picker intent
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto , SELECT_PHOTO);
            }
        });
        // start view gallery activity
        viewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // permissions
                String storagePerm = "android.permission.WRITE_EXTERNAL_STORAGE";
                String cameraPerm = "android.permission.CAMERA";
                int checkVal = getApplicationContext().checkCallingOrSelfPermission(storagePerm);
                int checkVal2 = getApplicationContext().checkCallingOrSelfPermission(cameraPerm);
                if (checkVal == PackageManager.PERMISSION_GRANTED && checkVal2 == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AIActivity.this, "Loading ...", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AIActivity.this, GalleryActivity.class));
                } else {
                    Toast.makeText(AIActivity.this,"Storage and Camera Permission Required",Toast.LENGTH_SHORT).show();
                }
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
        img = new File(fluttrFolder, "Fluttr_" + timeStamp + ".png");  // create image
        Uri uriSavedImage = FileProvider.getUriForFile(                          // get uri
                AIActivity.this,
                "com.example.charles.opencv.provider",
                img);
        imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);              // save image
        startActivityForResult(imgIntent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    // checks if camera is available
    public boolean checkIfCameraAvailable() {
        return AIActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    // gets the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if we are capturing an image
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            // success
            if (resultCode == RESULT_OK) {
                //Toast.makeText(AIActivity.this, "Image Captured Successfully", Toast.LENGTH_SHORT).show();

                // AI STUFF
                if (img == null) {
                    Toast.makeText(AIActivity.this, "Error Processing Image! Please Retry", Toast.LENGTH_LONG).show();
                } else {
                    String imgPath = img.getAbsolutePath();
                    Bitmap image = null;

                    try {
                        image = BitmapFactory.decodeFile(imgPath);
                    } catch(Exception e) {
                        Log.i("OnCreate", "Image didn't load.");
                    }

                    AI ai = new AI(this);
                    Integer birdID = ai.identify(image);
                    Database db = new Database(this);
                    if(birdID != null) {
                        Toast.makeText(AIActivity.this, "You have identified a " + db.getBird(birdID).getName() + ". Adding to Bird Bank", Toast.LENGTH_LONG).show();
                        db.addData(birdID.toString());
                    } else {
                        Toast.makeText(AIActivity.this, "Sorry, we could not identify a bird_finder in this image. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            // failure
            } else {
                Toast.makeText(AIActivity.this, "Error Capturing Image! Please Retry", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_PHOTO) {
             try {
                 final Uri imageUri = data.getData();
                 if (imageUri == null) {
                     Toast.makeText(AIActivity.this, "Error in uploading image.", Toast.LENGTH_LONG).show();
                 } else {
                     final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                     final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                     AI ai = new AI(this);
                     Integer birdID = ai.identify(selectedImage);
                     Database db = new Database(this);
                     if(birdID != null) {
                         Toast.makeText(AIActivity.this, "You have identified a " + db.getBird(birdID).getName() + ". Adding to Bird Bank", Toast.LENGTH_LONG).show();
                         db.addData(birdID.toString());
                     } else {
                         Toast.makeText(AIActivity.this, "Sorry, we could not identify a bird in this image. Please try again.", Toast.LENGTH_LONG).show();
                     }
                 }
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
                 Toast.makeText(AIActivity.this, "Error in uploading image.", Toast.LENGTH_LONG).show();
             }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * OnClick function for bf_failure button and bf_topresults button
     * @param v View attached to the onclick
     */
    public void goBack(View v) {
        super.finish();
    }

}