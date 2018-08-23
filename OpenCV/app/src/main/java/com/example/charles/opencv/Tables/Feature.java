package com.example.charles.opencv.Tables;

import android.util.Log;
import android.util.SparseArray;

/**
 * This class allows the conversion from database ENUM values to their Meaning
 */
public final class Feature {
    private static SparseArray<String> OPTION;
    public static final Integer UNKNOWN = 1;
    public static final Integer OTHER = 2;
    private static boolean loaded = false;

    public Feature(SparseArray<String> features) {
        if (loaded)
            throw new RuntimeException("Feature [Feature(SparseArray<String> features)]: Do not load feature array multiple times.");

        OPTION = features;
        loaded = true;
    }

    /**
     * Get Value of Option, ie 0->"Other"
     *
     * @param option Feature Option
     * @return Value of the Feature
     */
    public static String valueOf(Integer option) {
        if (!loaded)
            throw new RuntimeException("Feature [valueOf(Integer option)]: Feature set not loaded yet.");

        //If option is too large, return UNKNOWN
        if (option >= OPTION.size()) {
            Log.d("Feature", "Value Not Found");
            return "Unknown";
        }

        //Return Value of the Feature, if None Exists Return UNKNOWN
        return OPTION.get(option, "Unknown");
    }

    /**
     * Returns true if the feature is an "unknown" feature
     *
     * @param feature Index of feature in FeatureOption
     * @return True if the feature is an "unknown" feature
     */
    public static boolean isUnknown(Integer feature) {
        if (!loaded)
            throw new RuntimeException("Feature [isUnknown(Integer feature)]: Feature set not loaded yet.");

        return feature.equals(UNKNOWN);
    }

    /**
     * Returns true if the feature is an "other" feature
     *
     * @param feature Index of feature in FeatureOption
     * @return True if the feature is an "other" feature
     */
    public static boolean isOther(Integer feature) {
        if (!loaded)
            throw new RuntimeException("Feature [isOther(Integer feature)]: Feature set not loaded yet.");

        return feature.equals(OTHER);
    }
}
