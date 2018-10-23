package com.example.charles.opencv.AI;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.example.charles.opencv.Database.BirdFinderDatabase;
import com.example.charles.opencv.Tables.Bird;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GoogleAI {
    /**
     * Determines using the local API whether a bitmap image contains a bird_finder.
     * @param bitmap_image Image to be processed
     * @return True if a bird_finder is found
     */
    static boolean isBird(Bitmap bitmap_image) {
        FirebaseVisionLabelDetector detector = FirebaseVision.getInstance().getVisionLabelDetector();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap_image);
        Task<List<FirebaseVisionLabel>> results =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionLabel> labels) {
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

        while (!results.isSuccessful()) { }

        for (FirebaseVisionLabel label : results.getResult())
            if (label.getEntityId().equals("/m/015p6")) {
                return true;
            }
        return false;
    }
}
