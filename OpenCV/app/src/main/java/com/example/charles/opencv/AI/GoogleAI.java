package com.example.charles.opencv.AI;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.charles.opencv.Database.BirdFinderDatabase;
import com.example.charles.opencv.Tables.Bird;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

public class GoogleAI {
    private FirebaseVisionCloudLabelDetector detector;

    public void searchBirds(Context context) {
        detector = FirebaseVision.getInstance().getVisionCloudLabelDetector();

        BirdFinderDatabase db = new BirdFinderDatabase(context);
        List<Integer> birds = db.getBirdIDs();

        for (Integer birdID: birds) {
            Bird bird = db.getBird(birdID);

            try {
                cloudLabel(bird.getName(), bird.getBirdID(), bird.getImage(context));
            } catch (Exception | Error ex) {
                Log.i("searchBirds", String.format("%s Failed", bird.getName()));
            }
        }
    }

    private void cloudLabel(String bird, int id, Bitmap bitmap_image) {
        //Cloud API
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap_image);

        Task<List<FirebaseVisionCloudLabel>> results =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
                                        // Task completed successfully
                                        // ...
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

        while (!results.isSuccessful()) {
            //Do Nothing
        }

        for (FirebaseVisionCloudLabel label : results.getResult())
            Log.d("cloudlabel", String.format("Bird (%s) (%d) | Label: %s | Entity: %s | Confidence: %f", bird, id, label.getLabel(), label.getEntityId(), label.getConfidence()));
    }
}
