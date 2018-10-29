package com.example.charles.opencv.AI;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

public class TensorFlowImageClassifier implements Classifier {
    private static final String TAG = "TensorFlowImageClassifier";

    private static final int MAX_RESULTS = 3;
    private static final float THRESHOLD = 0.1f;

    private String inputName;
    private String outputName;
    private int inputSize;
    private int imageMean;
    private float imageStd;

    private Vector<String> labels = new Vector<>();
    private int[] intValues;
    private float[] floatValues;
    private float[] outputs;
    private String[] outputNames;

    private boolean logStats = false;

    private TensorFlowInferenceInterface inferenceInterface;

    private TensorFlowImageClassifier() {}

    /**
     * Initialises a native TensorFlow model from the assets folder
     * @param assetManager Application Asset Manager
     * @param modelFilename Filepath of GraphDef protocol buffer
     * @param labelFilename Filepath of label file for classifier
     * @param inputSize Input size, a square image of inputSize x inputSize is assumed
     * @param imageMean Assumed mean of the image values
     * @param imageStd Assumed std of the image values
     * @param inputName Label of the image input node
     * @param outputName Label of the image output node
     * @throws IOException
     */
    public static Classifier create (
            AssetManager assetManager,
            String modelFilename,
            String labelFilename,
            int inputSize,
            int imageMean,
            float imageStd,
            String inputName,
            String outputName) {

        TensorFlowImageClassifier c = new TensorFlowImageClassifier();
        c.inputName = inputName;
        c.outputName = outputName;

        String filePath = labelFilename.split("file:///android_asset/")[1];
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open(filePath)));
            String line;
            while ((line = br.readLine()) != null) {
                c.labels.add(line);
            }
            br.close();
        } catch (IOException ex) {
            throw new RuntimeException("Problem reading label file!", ex);
        }

        c.inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);

        final Operation operation = c.inferenceInterface.graphOperation(outputName);
        final int numClasses = (int) operation.output(0).shape().size(1);

        c.inputSize = inputSize;
        c.imageMean = imageMean;
        c.imageStd = imageStd;
        c.outputNames = new String[] {outputName};
        c.intValues = new int[inputSize * inputSize];
        c.floatValues = new float[inputSize * inputSize * 3];
        c.outputs = new float[numClasses];

        return c;
    }

    /**
     * Returns a list containing the three most confident results from the Tensorflow model that have
     * a confidence level greater than the THRESHOLD
     * @param bitmap Bitmap image to analyse
     * @return List of top results
     */
    @Override
    public List<Recognition> recogniseImage(final Bitmap bitmap) {
        //Convert bitmap to pixel array
        bitmap.getPixels(intValues,
                0, bitmap.getWidth(),
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight());
        for (int i = 0; i < intValues.length; i++) {
            final int val = intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
        }

        //Calculate outputs
        inferenceInterface.feed(inputName, floatValues, 1, inputSize, inputSize, 3);
        inferenceInterface.run(outputNames, logStats);
        inferenceInterface.fetch(outputName, outputs);

        //Create priority queue
        PriorityQueue<Recognition> pq =
            new PriorityQueue<>(
                new Comparator<Recognition>() {
                    @Override
                    public int compare(Recognition o1, Recognition o2) {
                        return Float.compare(o2.getConfidence(), o1.getConfidence());
                    }
                }
            );

        //Populate queue with outputs higher than threshold
        for (int i = 0; i < outputs.length; ++i) {
            if (outputs[i] > THRESHOLD) {
                pq.add(new Recognition(String.valueOf(i),
                        labels.size() > i ? labels.get(i) : "unknown",
                        outputs[i], null));
            }
        }

        //Find the top three results
        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }

        return recognitions;
    }

    @Override
    public void enableStatLogging(boolean logStats) {
        this.logStats = logStats;
    }

    @Override
    public String getStatString() {
        return inferenceInterface.getStatString();
    }

    @Override
    public void close() {
        inferenceInterface.close();
    }
}
