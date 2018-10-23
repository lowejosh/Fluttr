package com.example.charles.opencv.AI;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.charles.opencv.Database.BirdFinderDatabase;
import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.Tables.Bird;

import java.util.List;

public class AI {
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/bird_tensor.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";

    private static Classifier classifier;
    private Context mContext;

    public AI(Context context){
        mContext = context;
        classifier = TensorFlowImageClassifier.create(
                context.getAssets(),
                MODEL_FILE,
                LABEL_FILE,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME);
    }

    /**
     * Returns the bird identified in the image. If not bird is found, null is returned.
     * @param bitmap_image Bitmap image containing the bird
     * @return Bird or null
     */
    public Integer identify(Bitmap bitmap_image) {
        Bitmap croppedBitmap = Bitmap.createScaledBitmap(bitmap_image, INPUT_SIZE, INPUT_SIZE, false);

        //Determine if the image contains a bird
        if (!GoogleAI.isBird(croppedBitmap)) {
            return null;
        }

        //Return the bird from custom bird identifier
        List<Classifier.Recognition> results = classifier.recogniseImage(croppedBitmap);

        //Retrieve top value
        Classifier.Recognition result = results.get(0);

        //Convert result to bird id
        Database db = new Database(mContext);
        return db.getBird(result.getTitle()).getBirdID();
    }
}
