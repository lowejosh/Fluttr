package com.example.charles.opencv;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.charles.opencv.Gallery.BaseFragment;
import com.example.charles.opencv.Gallery.GalleryFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {
    //code for Activity result
    private static final int CAPTURE_IMAGE_REQUEST_CODE=1000;
    private static final int  IMAGE=2000;
    // button to click image
    Button clickimage;
    Button uploadphoto;
    Button viewgallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickimage=(Button)findViewById(R.id.clickimage);
        uploadphoto=(Button)findViewById(R.id.uploadphoto);
        viewgallery=(Button)findViewById(R.id.viewgallery);
        clickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfCameraAvailable())
                {
                    clickImage();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Camera Not Available",Toast.LENGTH_SHORT).show();
                }
            }
        });
        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });
        viewgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, GalleryFragment.newInstance(0))
                        .commit();
            }
        });
    }

    //method to click image
    private void clickImage()
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=getOutputMediaFile(IMAGE, this);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        //intent to get Image intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }
    //check if camera is available in device
    public boolean checkIfCameraAvailable()
    {
        if(MainActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            //camera is available
            return true;
        }
        else
        {
            //camera is not available
            return false;
        }
    }
    //method to get the path for video and image
    private static File getOutputMediaFile(int type, Context context) {
        // get path of pictures directory and name a folder in it as fluttr
        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Fluttr");
        String mediaStoragePath = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(mediaStoragePath, "fluttr");
        // create directory
        //if (!mediaStorageDir.isDirectory()) {
         //   mediaStorageDir.mkdirs();
        //}

        //check if directory not exists and if not then create directory
        Log.d("Fluttr", "attempting to add to fluttr directory");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Fluttr", "failed to create directory");
                return null;
            }
        }
        // get current date and time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //if type is image then create path to store image and name it as currentdateandtime.jpg
        if(type==IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "FLUTTR_IMG_" + timeStamp + ".png");
        }
        else
        {
            Log.d("Fluttr", "failed to put image in directory");
            return null;
        }

        String imagePath =  mediaFile.getAbsolutePath();
        Log.d("Fluttr", mediaFile.getAbsolutePath());
        //scan the image so show up in album
        MediaScannerConnection.scanFile(context,
                new String[] { imagePath }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("Fluttr", "rereeeee to create directory");
                    }
                });
        return mediaFile;
    }
    //getting activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if capturing image
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            // successfully captured
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Image Captured Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Error Capturing Image! Please Retry", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Handle Incoming messages from contained fragments.
     */

    public void onFragmentInteraction(Uri uri) {

    }

    public void onFragmentInteraction(String id) {

    }

    public void onFragmentInteraction(int actionId) {

    }

}