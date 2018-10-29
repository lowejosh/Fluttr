package com.example.charles.opencv.FeatureActivity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.charles.opencv.AI.AI;
import com.example.charles.opencv.AI.GoogleAI;

import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.Gallery.GalleryActivity;
import com.example.charles.opencv.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.lang.String;

public class AIActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_REQUEST_CODE=1000;
    private final int SELECT_PHOTO = 1;
    final int PIC_CROP = 2;
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

        if(Build.VERSION.SDK_INT>=24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

                    Toast.makeText(AIActivity.this, "Please hold your phone in landscape when taking photos", Toast.LENGTH_LONG).show();

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
        Uri uri = FileProvider.getUriForFile(                          // get uri
                AIActivity.this,
                "com.example.charles.opencv.provider",
                img);
        imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);              // save image
        imgIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        imgIntent.putExtra("uri",uri.toString());
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
                if (img != null) {
                    Uri uri = Uri.fromFile(img);
                    crop(uri);
                }
            }
                /*
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
            */
        } else if (requestCode == SELECT_PHOTO) {
            /*
             try {
             */
                 final Uri imageUri = data.getData();
                 if (imageUri == null) {
                     Toast.makeText(AIActivity.this, "Error in uploading image.", Toast.LENGTH_LONG).show();
                 } else {
                     crop(imageUri);
                     /*
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
                     */
                 }
             /* } catch (FileNotFoundException e) {
                 e.printStackTrace();
                 Toast.makeText(AIActivity.this, "Error in uploading image.", Toast.LENGTH_LONG).show();
             }
             */
        } else if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                AI ai = new AI(this);
                Integer birdID = ai.identify(selectedBitmap);
                Database db = new Database(this);
                if(birdID != null) {
                    Toast.makeText(AIActivity.this, "You have identified a " + db.getBird(birdID).getName() + ". Adding to Bird Bank", Toast.LENGTH_LONG).show();
                    db.addData(birdID.toString());
                } else {
                    Toast.makeText(AIActivity.this, "Sorry, we could not identify a bird in this image. Please try again.", Toast.LENGTH_LONG).show();
                }
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

    private void crop(Uri picUri) {
        try {
            // Use android cropping intent
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            // Retrieve the values and place them in the returned data
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch (ActivityNotFoundException e) {
            String errorMessage = "Error: your device doesn't support image cropping";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
