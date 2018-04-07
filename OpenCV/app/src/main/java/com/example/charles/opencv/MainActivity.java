package com.example.charles.opencv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    static boolean flag;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Do something which
        } else {
            //When initialised
            flag = true;
        }
    }

    JavaCameraView javaCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView = (JavaCameraView)findViewById(R.id.camera_view);

        if (flag) {
            javaCameraView.setCameraIndex(0); // Zero for rear camera
            javaCameraView.setCvCameraViewListener(this);
            javaCameraView.enableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (javaCameraView.isEnabled()) {
            javaCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}
