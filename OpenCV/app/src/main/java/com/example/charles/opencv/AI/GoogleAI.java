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
    private FirebaseVisionLabelDetector localDetector;
    private FirebaseVisionCloudLabelDetector cloudDetector;
    private SparseArray<String> entityTypes;

    public GoogleAI() {
        entityTypes = new SparseArray<>();
        entityTypes.put(1, "/m/09ddx");
        entityTypes.put(2, "/m/09ddx");
        entityTypes.put(3, "/m/09ddx");
        entityTypes.put(4, "/m/09ddx");
        entityTypes.put(5, "/m/0gnx_");
        entityTypes.put(6, "/m/012shk");
        entityTypes.put(7, "/m/09ddx");
        entityTypes.put(8, "/m/01295w");
        entityTypes.put(9, "/m/0dbvp");
        entityTypes.put(10, "/m/09ddx");
        entityTypes.put(11, "/m/09ddx");
        entityTypes.put(12, "/m/09ddx");
        entityTypes.put(13, "/m/01fmbp");
        entityTypes.put(14, "/m/09ddx");
        entityTypes.put(15, "/m/09ddx");
        entityTypes.put(17, "/m/09ddx");
        entityTypes.put(18, "/m/09ddx");
        entityTypes.put(19, "/m/09ddx");
        entityTypes.put(20, "/m/09ddx");
        entityTypes.put(22, "/m/09ddx");
        entityTypes.put(23, "/m/01295w");
        entityTypes.put(24, "/m/09ddx");
        entityTypes.put(30, "/m/0k64b");
        entityTypes.put(78, "/m/01jgnh");
        entityTypes.put(88, "/m/0h02z");
    }

    public List<Integer> identify(Bitmap bitmap_image) {
        List<Integer> matchingBirds = new ArrayList<>();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap_image);
        cloudDetector = FirebaseVision.getInstance().getVisionCloudLabelDetector();
        localDetector = FirebaseVision.getInstance().getVisionLabelDetector();

        if (!isBird(image))
            return matchingBirds;

        List<FirebaseVisionCloudLabel> labels = cloudLabel(image);

        //Sort list by highest confidence
        Collections.sort(labels, new Comparator<FirebaseVisionCloudLabel>() {
            @Override
            public int compare(FirebaseVisionCloudLabel o1, FirebaseVisionCloudLabel o2) {
                return Float.compare(o2.getConfidence(), o1.getConfidence());
            }
        });

        boolean labelMatch = false;

        //Find the first matching label
        for (FirebaseVisionCloudLabel label : labels) {
            //Find any birds matching that label
            for (int i = 0; i < entityTypes.size(); i++) {
                if (entityTypes.valueAt(i).equals(label.getEntityId())) {
                    Log.i("Identify", "Confidence: " + String.valueOf(label.getConfidence()) + " Match Detected: " + label.getLabel());

                    matchingBirds.add(entityTypes.keyAt(i));
                    labelMatch = true;
                }
            }

            //If a matching label has been found, return list
            if (labelMatch)
                break;
        }

        return matchingBirds;
    }

    public void searchBirds(Context context) {
        cloudDetector = FirebaseVision.getInstance().getVisionCloudLabelDetector();

        BirdFinderDatabase db = new BirdFinderDatabase(context);
        List<Integer> birds = db.getBirdIDs();

        for (int i = 4; i < birds.size(); i++) {
            Bird bird = db.getBird(birds.get(i));

            try {
                //cloudLabel(bird.getName(), bird.getBirdID(), bird.getImage(context));
            } catch (Exception | Error ex) {
                Log.i("searchBirds", String.format("%s Failed", bird.getName()));
            }
        }
    }

    private List<FirebaseVisionCloudLabel> cloudLabel(FirebaseVisionImage image) {
        Task<List<FirebaseVisionCloudLabel>> results =
                cloudDetector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

        while (!results.isSuccessful()) {
            //Do Nothing
        }

        //for (FirebaseVisionCloudLabel label : results.getResult())
        //    Log.d("cloudlabel", String.format("Bird (%s) (%d) | Label: %s | Entity: %s | Confidence: %f", bird, id, label.getLabel(), label.getEntityId(), label.getConfidence()));

        return results.getResult();
    }

    /**
     * Determines using the local API whether a bitmap image contains a bird.
     * @param image Image to be processed
     * @return True if a bird is found
     */
    private boolean isBird(FirebaseVisionImage image) {
        Task<List<FirebaseVisionLabel>> results =
                localDetector.detectInImage(image)
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
