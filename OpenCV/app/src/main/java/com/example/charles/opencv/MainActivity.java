package com.example.charles.opencv;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //code for Activity result
    private static final int CAPTURE_IMAGE_REQUEST_CODE=1000;
    private static final int  IMAGE=2000;
    // button to click image and record video
    Button clickimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickimage=(Button)findViewById(R.id.clickimage);
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
                    return;
                }
            }
        });
    }
    //method to click image
    private void clickImage()
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=getOutputMediaFile(IMAGE);
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
    private static File getOutputMediaFile(int type) {
        // get path of pictures directory and name a folder in it as MyCameraApp
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        //check if directory not exists and if not then create directory
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // get current date and time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //if type is image then create path to store image and name it as currentdateandtime.jpg
        if(type==IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        }
        else
        {
            return null;
        }
        return mediaFile;
    }
    //getting activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if capturing image
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            // successfully captured
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, " Image Captured Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Error Capturing Image! Please Retry", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}