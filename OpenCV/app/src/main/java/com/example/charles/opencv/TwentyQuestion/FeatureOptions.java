package com.example.charles.opencv.TwentyQuestion;

import android.util.Log;
import android.util.SparseArray;

public final class FeatureOptions {
    private static SparseArray<String> OPTION = new SparseArray<>();
    public static final Integer UNKNOWN = -1;
    public static final Integer OTHER = 0;

    static {
        OPTION.put(-1, "Unknown");
        OPTION.put(0, "Other");
        OPTION.put(1, "Prominent");
        OPTION.put(2, "Curved");
        OPTION.put(3, "Long");
        OPTION.put(4, "Obvious Coloured Eye Ring");
        OPTION.put(5, "Obvious White Patch");
        OPTION.put(6, "Obvious Coloured Patch");
        OPTION.put(7, "Brightly Coloured");
        OPTION.put(8, "Shield");
        OPTION.put(9, "Distinctively Masked");
        OPTION.put(10, "Crest");
        OPTION.put(11, "Topknot");
        OPTION.put(12, "Knob");
        OPTION.put(13, "Lobe");
        OPTION.put(14, "Obvious Streaks");
        OPTION.put(15, "Spots");
        OPTION.put(16, "Showy");
        OPTION.put(17, "Bare Skin");
        OPTION.put(18, "Wattles");
        OPTION.put(19, "Tufts");
        OPTION.put(20, "Plumes");
        OPTION.put(21, "Forked");
        OPTION.put(22, "Fanned");
        OPTION.put(23, "Black");
        OPTION.put(24, "Blue");
        OPTION.put(25, "Brown");
        OPTION.put(26, "Green");
        OPTION.put(27, "Grey");
        OPTION.put(28, "Orange");
        OPTION.put(29, "Pink");
        OPTION.put(30, "Purple");
        OPTION.put(31, "Red");
        OPTION.put(32, "Glossy Sheen");
        OPTION.put(33, "Metallic Sheen");
        OPTION.put(34, "White");
        OPTION.put(35, "Yellow");
        OPTION.put(36, "Bird");
        OPTION.put(37, "Fowl");
        OPTION.put(38, "Heavyset");
        OPTION.put(39, "Heron");
        OPTION.put(40, "Honey Eater");
        OPTION.put(41, "Kingfisher");
        OPTION.put(42, "Medium Shorebird");
        OPTION.put(43, "Large Shorebird");
        OPTION.put(44, "Owl");
        OPTION.put(45, "Parrot");
        OPTION.put(46, "Pigeon");
        OPTION.put(47, "Raptor");
        OPTION.put(48, "Seagull");
        OPTION.put(49, "Small Bird with Tail Down");
        OPTION.put(50, "Small Bird with Tail Up");
    }

    /**
     * Get Value of Option, ie 0->"Other"
     *
     * @param option Feature Option
     * @return Value of the Feature
     */
    public static String valueOf(Integer option) {
        //If option is too large, return UNKNOWN
        if (option >= OPTION.size()) {
            Log.d("FeatureOptions", "Value Not Found");
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
        return feature.equals(UNKNOWN);
    }

    /**
     * Returns true if the feature is an "other" feature
     *
     * @param feature Index of feature in FeatureOption
     * @return True if the feature is an "other" feature
     */
    public static boolean isOther(Integer feature) {
        return feature.equals(OTHER);
    }
}
